package com.example.safetourbcn;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

/*
 * Main Activity class that loads {@link MainFragment}.
 */
public class EditProfileActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Session session = Session.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();

        // Capture the layout's TextView and set the string as its text
        //TextView textView = findViewById(R.id.textView);
        //textView.setText(message);
        final TextView correoTextView = findViewById(R.id.correo);
        final TextView nombreTextView = findViewById(R.id.nombre);
        /* correo tiene que ser el correo del usuario actual*/

        String correo = session.getEmail();
        int N = correo.length();
        correoTextView.setText(correo.toCharArray(), 0, N-1);
        /* usuario tiene que ser el nombre del usuario actual*/

        String usuario = session.getName();
        int M = usuario.length();
        nombreTextView.setText(usuario.toCharArray(), 0, M-1);
    }

    public void confirm(View view) {
        Intent intent = new Intent(this, EditProfileActivity.class);
        startActivity(intent);
    }
}