package com.example.safetourbcn;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class PerfilUsuario extends AppCompatActivity {

    BackEndRequests ber = new BackEndRequests();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);

        final TextView correoTextView = findViewById(R.id.correo);
        final TextView nombreTextView = findViewById(R.id.nombre);
        /* correo tiene que ser el correo del usuario actual

        String correo;
        N = correo.length();
        correoTextView.setText(correo, 0, N-1);
        */
        /* usuario tiene que ser el nombre del usuario actual

        String usuario;
        M = usuario.length();
        nombreTextView.setText(usuario, 0, M-1);
        */

    }
}