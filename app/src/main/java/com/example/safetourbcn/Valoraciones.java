package com.example.safetourbcn;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class Valoraciones extends AppCompatActivity {

    private String insta = null, web = null;
    private BackEndRequests backEndRequests;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valoraciones);

        session = Session.getInstance();
        backEndRequests = BackEndRequests.getInstance();

        final TextView name_establish = findViewById(R.id.nombre_establecimiento);
        name_establish.setText(getIntent().getExtras().getString("establishment"));

        PlacesList pl = PlacesList.getInstance();
        Establishment thisEstablishment = null;
        int b = 0;
        String direccion = null;
        Integer horaapertura = null, horacierre = null;
        Integer idEstablecimiento = null;
        for (int i = 0; i < pl.getLength() && b == 0; ++i) {
            Establishment place = pl.getEstablishment(i);
            if(place.getName().equals(name_establish.getText())){
                b = 1;
                idEstablecimiento = i;
                direccion = place.getAddress();
                horaapertura = place.getHouropen();
                horacierre = place.getHourclose();
                web = place.getWebsite();
                insta = place.getInstagram();
                thisEstablishment = place;
            }

        }
        final TextView dir_establish = findViewById(R.id.direccion_establecimiento);
        dir_establish.setText(direccion);
        final Spinner spinner = findViewById(R.id.spinner);
        ArrayList<String> arrayList = new ArrayList<>();
        for(int i = horaapertura; i< horacierre; i++){
            arrayList.add(Integer.toString(i)+"h");
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        final CalendarView calendario= findViewById(R.id.calendar);
        final EditText number = findViewById(R.id.number);
        final Button button = findViewById(R.id.button_reservar);
        Integer finalIdEstablecimiento = idEstablecimiento;
        Integer finalIdEstablecimiento1 = idEstablecimiento;
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String texto = spinner.getSelectedItem().toString();
                if(number.getText().toString().equals("")) {
                    specify_number();
                }
                else{
                    //if(Integer.parseInt(texto)<=getSpaceLeft(finalIdEstablecimiento)){
                        if(0==0){
                            String horareserva = "";
                            for(int i = 0; i < texto.length(); ++i){
                                if(Character.compare(texto.charAt(i), 'h') != 0){
                                    horareserva += texto.charAt(i);
                                }
                            }
                            Integer horaReserva = Integer.parseInt(horareserva);
                            //backEndRequests.guardaReserva(finalIdEstablecimiento1, session.getEmail(), Integer.parseInt(texto), calendario.getDate(), horaReserva);
                            Snackbar mySnack = Snackbar.make(v, Long.toString(calendario.getDate()), 2000);
                            mySnack.show();
                    }
                    else{
                        Snackbar mySnack = Snackbar.make(v, "There are only 4 places left", 2000);
                        mySnack.show();
                    }
                }
            }
        });

        if(thisEstablishment != null) getSpaceLeft(thisEstablishment.getId());

    }

    public void specify_number(){
        AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle("You have to specify a number of people").setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {}
        }).show();
    }

    public void goToInsta (View view) {
        if (insta.length()>1)
        {
            String url = insta;
            Uri uriUrl = Uri.parse(url);
            Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
            startActivity(launchBrowser);
        }
        else
        {
            Snackbar mySnack = Snackbar.make(view, "This establishment doesn't have an Instagram", 2000);
            mySnack.show();
        }
    }
    public void goToUrl (View view) {
        if(web.length()>1) {
            String url = web;
            Uri uriUrl = Uri.parse(url);
            Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
            startActivity(launchBrowser);
        }
        else
        {
            Snackbar mySnack = Snackbar.make(view, "This establishment doesn't have a Website", 2000);
            mySnack.show();
        }
    }






    void getSpaceLeft(Integer id) {
        String url = backEndRequests.getServerAddress()
                + "/Establishment/"
                + Integer.toString(id)
                + "/reserveSpaceLeft";

        Request request = new Request.Builder()
                .url(url)
                .addHeader("AUTHORIZATION", session.getApiKey())
                .build();

        backEndRequests.getClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                backEndRequests.setErrorMsg("connection");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.isSuccessful()) {
                    System.out.println("ddddddddddddddx");
                    String r = response.body().string();
                    System.out.println("response: " + r);

                    try {
                        JSONArray ja = new JSONArray(r);
                        for(int i = 0; i < ja.length(); ++i) {
                            JSONObject jo = ja.getJSONObject(i);
                            System.out.println(jo.toString());

                            //////////////////////////////////////
                            Integer spaceLeft = jo.isNull("Space_Left") ? 0 : jo.getInt("Space_Left");
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        backEndRequests.setErrorMsg("connection");
                    }
                }

                else {
                    String r = response.body().string();
                    System.out.println("response: " + r);
                    System.out.println("token " + "Bearer " + session.getApiKey());
                }
            }

        });
    }
}