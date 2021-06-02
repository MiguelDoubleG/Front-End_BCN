package com.example.safetourbcn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class Valoraciones extends AppCompatActivity {

    private String insta = null, web = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valoraciones);



        final TextView name_establish = findViewById(R.id.nombre_establecimiento);
        name_establish.setText(getIntent().getExtras().getString("establishment"));

        PlacesList pl = PlacesList.getInstance();
        int b = 0;
        String direccion = null;
        Integer horaapertura = null, horacierre = null;
        for (int i = 0; i < pl.getLength() && b == 0; ++i) {
            Establishment place = pl.getEstablishment(i);
            if(place.getName().equals(name_establish.getText())){
                b = 1;
                direccion = place.getAddress();
                horaapertura = place.getHouropen();
                horacierre = place.getHourclose();
                web = place.getWebsite();
                insta = place.getInstagram();
            }

        }
        final TextView dir_establish = findViewById(R.id.direccion_establecimiento);
        dir_establish.setText(direccion);
        final Spinner spinner = findViewById(R.id.spinner);
        ArrayList<String> arrayList = new ArrayList<>();
        for(int i = horaapertura; i<= horacierre; i++){
            arrayList.add(Integer.toString(i)+"h");
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

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
}