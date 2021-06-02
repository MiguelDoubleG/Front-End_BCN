package com.example.safetourbcn;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.model.Marker;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Valoraciones extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valoraciones);



        final TextView name_establish = findViewById(R.id.nombre_establecimiento);
        name_establish.setText(getIntent().getExtras().getString("establishment"));

        PlacesList pl = PlacesList.getInstance();
        int b = 0;
        String direccion = null;
        String horario = null;
        float rating = 0;
        for (int i = 0; i < pl.getLength() && b == 0; ++i) {
            Establishment place = pl.getEstablishment(i);
            if(place.getName().equals(name_establish.getText())){
                b = 1;
                direccion = place.getAddress();
                horario = place.getSchedule();
                rating = place.getRating();
            }

        }
        final TextView dir_establish = findViewById(R.id.direccion_establecimiento);
        dir_establish.setText(direccion);
        final Spinner spinner = findViewById(R.id.spinner);
        int d = 4;
        ArrayList<String> arrayList = new ArrayList<>();
        int horainici= 8;
        int horafinal = 20;
        for(int i = horainici; i<= horafinal; i++){
            arrayList.add(Integer.toString(i)+"h");
        }
        /*arrayList.add(Integer.toString(d)+"h");
        arrayList.add("9h");
        arrayList.add("10h");
        arrayList.add("11h");
        arrayList.add("12h");
         */
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        final CalendarView calendario= findViewById(R.id.calendar);
        final EditText number = findViewById(R.id.number_personas);
        final Button button = findViewById(R.id.boton_reservar);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String texto = spinner.getSelectedItem().toString();
                if(number.getText().toString().equals("")) {
                    specify_number();
                }
                else{

                }
            }
        });

    }
    public void hey() {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    public void specify_number(){
        AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle("You have to specify a number of people").setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {}
        }).show();
    }

}