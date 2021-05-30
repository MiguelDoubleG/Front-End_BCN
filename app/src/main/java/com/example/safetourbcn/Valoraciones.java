package com.example.safetourbcn;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

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
        final TextView horari = findViewById(R.id.horario);
        horari.setText(horario);
    }
}