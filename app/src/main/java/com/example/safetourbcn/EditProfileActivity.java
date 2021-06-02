package com.example.safetourbcn;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

public class EditProfileActivity extends AppCompatActivity {
    Session session = Session.getInstance();
    BackEndRequests ber = BackEndRequests.getInstance();
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
        correoTextView.setText(correo.toCharArray(), 0, N);
        /* user must be the name of the current user*/

        String usuario = session.getName();
        int M = usuario.length();
        nombreTextView.setText(usuario.toCharArray(), 0, M);
    }
    public void save(View view) {
        final TextView nombreTextView = findViewById(R.id.nombre);
        String newUsername = nombreTextView.getText().toString();
        session.editUserName(newUsername);
        ber.editUser(session.getEmail(),newUsername);


        final TextView passwordTextView = findViewById(R.id.password);
        String password = passwordTextView.getText().toString();
        final TextView newPasswordTextView = findViewById(R.id.new_password);
        String newPassword = newPasswordTextView.getText().toString();
        if(newPassword.length()>1) {
            if (password.equals(session.getPassword())) {
                session.editPassword(newPassword);
                //ber.editUserPass(session.getEmail(), newPassword);
            } else {
                Snackbar mySnack = Snackbar.make(view, "Incorrect Password - Password not edited", 2000);
                mySnack.show();
            }
        }
    }
}