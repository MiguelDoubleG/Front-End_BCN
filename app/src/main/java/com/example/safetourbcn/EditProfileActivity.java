package com.example.safetourbcn;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class EditProfileActivity extends AppCompatActivity {
    Session session = Session.getInstance();
    //BackEndRequests ber = BackEndRequests.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        //final TextView correoTextView = findViewById(R.id.correo);
        final TextView correoTextView = findViewById(R.id.correo);
        final TextView nombreTextView = findViewById(R.id.nombre);
        /* email must be the user of the current user*/

        String correo = session.getEmail();
        int N = correo.length();
        correoTextView.setText(correo.toCharArray(), 0, N-1);
        /* user must be the name of the current user*/

        String usuario = session.getName();
        int M = usuario.length();
        nombreTextView.setText(usuario.toCharArray(), 0, M-1);
    }
    public void save(View view) {
        final TextView nombreTextView = findViewById(R.id.nombre);
        String newUsername = nombreTextView.getText().toString();
        session.editUserName(newUsername);
    }
}