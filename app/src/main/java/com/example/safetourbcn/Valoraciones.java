package com.example.safetourbcn;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Valoraciones extends AppCompatActivity {

    private String insta = null, web = null;
    private BackEndRequests backEndRequests;
    private Session session;
    private int dayReserva = 0, monthReserva = 0, yearReserva = 0;
    private String fecha = getCurrentDay();
    private String currDay = java.time.LocalTime.now().toString();
    private int spaceleft;
    private int idEstablecimiento = 0;
    private Boolean reservar = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        spaceleft = 10;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valoraciones);

        session = Session.getInstance();
        backEndRequests = BackEndRequests.getInstance();
        final EditText number = findViewById(R.id.number);
        final Button button = findViewById(R.id.button_reservar);
        final TextView name_establish = findViewById(R.id.nombre_establecimiento);
        name_establish.setText(getIntent().getExtras().getString("establishment"));

        PlacesList pl = PlacesList.getInstance();
        Establishment thisEstablishment = null;
        int b = 0;
        String direccion = null;
        Integer horaapertura = null, horacierre = null;
        for (int i = 0; i < pl.getLength() && b == 0; ++i) {
            Establishment place = pl.getEstablishment(i);
            if(place.getName().equals(name_establish.getText())){
                b = 1;
                idEstablecimiento = place.getId();
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


        CalendarView.OnDateChangeListener myCalendarListener = new CalendarView.OnDateChangeListener(){

            public void onSelectedDayChange(CalendarView view, int year, int month, int day){

                // add one because month starts at 0
                month = month + 1;
                // output to log cat **not sure how to format year to two places here**
                dayReserva = day;
                monthReserva = month;
                yearReserva = year;
                if(day < 10){
                    if(month<10){
                        fecha = "0" + day + "-" +"0" + month + "-" + year;
                    }
                    else {
                        fecha = "0" + day + "-" + month + "-" + year;
                    }
                }
                else{
                    if(month < 10){
                        fecha = day + "-" +"0" + month + "-" + year;
                    }
                    else{
                        fecha = day + "-" + month + "-" + year;
                    }
                }

                String texto = spinner.getSelectedItem().toString();
                String horareserva = "";
                for(int i = 0; i < texto.length(); ++i){
                    if(Character.compare(texto.charAt(i), 'h') != 0){
                        horareserva += texto.charAt(i);
                    }
                }
                try {
                    getSpaceLeft(idEstablecimiento, fecha, horareserva);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        calendario.setOnDateChangeListener(myCalendarListener);

        
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(number.getText().toString().equals("")) {
                    specify_number();
                }
                else{
                    if(!validDate(fecha)){
                        Snackbar mySnack = Snackbar.make(v, "Select a valid day", 2000);
                        mySnack.show();
                    }
                    else{
                        //if(Integer.parseInt(texto)<=getSpaceLeft(finalIdEstablecimiento)){
                        String texto = spinner.getSelectedItem().toString();
                        String horareserva = "";
                        for(int i = 0; i < texto.length(); ++i){
                            if(Character.compare(texto.charAt(i), 'h') != 0){
                                horareserva += texto.charAt(i);
                            }
                        }
                        try {
                            getSpaceLeft(idEstablecimiento, fecha, horareserva);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(Integer.parseInt(number.getText().toString()) <= spaceleft){

                            confirmar_reserva(idEstablecimiento, session.getEmail(), Integer.parseInt(number.getText().toString()), fecha, horareserva);

                        }
                        else{
                            Snackbar mySnack = Snackbar.make(v, "There are only " + spaceleft + " places left", 2000);
                            mySnack.show();
                        }
                    }
                }
            }
        });



    }

    public void confirmar_reserva(int id, String mail, int numper, String date, String hora){

        AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle("Confirm reserve?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences sharedPreferences = getSharedPreferences("token", MODE_PRIVATE);
                        String token = sharedPreferences.getString("token", "notoken");
                        guardaReserva(id, token, numper, date, hora);
                        reserva_confirmada();
                    }
                })
                .setNegativeButton("NO", null)

                .show();

    }

    public void reserva_confirmada(){
        AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle("Your reserve has been confirmed.")
                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
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

    public void gotoRatings(View view){
        Intent intent = new Intent(this, Ratings.class);
        intent.putExtra("ESTABLISHMENT_ID", Integer.toString(idEstablecimiento));
        startActivity(intent);
    }



    void getSpaceLeft(Integer id, String data, String time) throws JSONException {
        String url = backEndRequests.getServerAddress()
                + "/Establishment/"
                + Integer.toString(id)
                + "/reserveSpaceLeft"
                + "?reservation_date=" + data
                + "&reservation_hour=" + time;
        System.out.print(url);

        Request request = new Request.Builder()
                .url(url)
                .addHeader("AUTHORIZATION", session.getApiKey())
                .build();
        System.out.println(url);
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
                            Integer spaceLeft = jo.isNull("Space_Left") ? 10 : jo.getInt("Space_Left");
                            spaceleft = spaceLeft;
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



    public String getCurrentDay(){
        long millis=System.currentTimeMillis();
        java.sql.Date date=new java.sql.Date(millis);
        String fechaMal = date.toString();
        String nuevaFecha = "";
        String dia = "", mes = "", año = "";
        for(int i = 0, j=0, k=0; i<fechaMal.length(); ++i){
            if(j==0){
                if(Character.compare(fechaMal.charAt(i), '-') != 0){
                    año += fechaMal.charAt(i);
                }
                else{
                    j++;
                }
            }
            else if (j==1){
                if(Character.compare(fechaMal.charAt(i), '-') != 0){
                    mes += fechaMal.charAt(i);
                }
                else{
                    j++;
                }
            }
            else{
                dia += fechaMal.charAt(i);
            }
        }
        nuevaFecha = dia + "-" + mes + "-" + año;
        return nuevaFecha;
    }

    public boolean validDate(String date){
        String[] fecha = date.split("-");
        String[] currentFecha = getCurrentDay().split("-");
        if(fecha[2].compareTo(currentFecha[2]) > 0) {
            return true;
        }
        else if(fecha[2].compareTo(currentFecha[2]) == 0){
            if(fecha[1].compareTo(currentFecha[1]) > 0){
                return true;
            }
            else if (fecha[1].compareTo(currentFecha[1]) == 0){
                if(fecha[0].compareTo(currentFecha[0]) > 0){
                    return true;
                }
                else{
                    return false;
                }
            }
            else{
                return false;
            }
        }
        else{
            return false;
        }
    }

    public void guardaReserva(Integer id, String token, Integer count, String rd, String rh) {
        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
        JSONObject newName = new JSONObject();
        String url = backEndRequests.getServerAddress() + "/registerReservation";

        try {
            newName.put("id_establishment", id);
            newName.put("people_count", count);
            newName.put("reservation_date", rd);
            newName.put("reservation_hour", rh);
        } catch (JSONException e) {
            Log.d("OKHTTP3", "JSON Excepton");
            e.printStackTrace();
        }

        System.out.print(url);
        RequestBody body = RequestBody.create(newName.toString(), JSON);
        System.out.print("\n" + newName + "\n");
        Request request = new Request.Builder()
                .url(url)
                .addHeader("AUTHORIZATION", token)
                .post(body)
                .build();
        System.out.print("hey\n");
        backEndRequests.getClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                //errorMsg = "connection";
                System.out.print("No va");
                Log.d("hey", "heyyy");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.isSuccessful()) {
                    String TAG = "aa";
                    Log.d(TAG,response.message());
                    System.out.print("Deu");
                }
                else{
                    System.out.print("Hola");
                }
            }
        });
    }
}