package com.example.safetourbcn;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.safetourbcn.ui.login.LoginViewModel;
import com.example.safetourbcn.ui.login.LoginViewModelFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SignUpActivity extends AppCompatActivity {
    private LoginViewModel loginViewModel;
    BackEndRequests ber = BackEndRequests.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final EditText usernameEditText = findViewById(R.id.usernameSignUp);
        final EditText password1EditText = findViewById(R.id.passwordSignUp);
        final EditText password2EditText = findViewById(R.id.passwordSignUpRepeat);
        final Button signUpButton = findViewById(R.id.signupButton);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading2);
        final EditText nameEditText = findViewById(R.id.nameSignUp);


        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.signupDataChanged(usernameEditText.getText().toString(),
                        password1EditText.getText().toString(), password2EditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        password1EditText.addTextChangedListener(afterTextChangedListener);
        password2EditText.addTextChangedListener(afterTextChangedListener);
        /*passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });*/








        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                try {
                    signUp();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });




    }





    private void signUp() throws JSONException {
        EditText usernameEditText = findViewById(R.id.usernameSignUp);
        EditText passwordEditText = findViewById(R.id.passwordSignUp);
        EditText nameEditText = findViewById(R.id.nameSignUp);

        ber.addUser(usernameEditText.getText().toString(), passwordEditText.getText().toString(), nameEditText.toString());

        SystemClock.sleep(1000);

        if(matchUser(usernameEditText.getText().toString(), passwordEditText.getText().toString())) {
            logIn();
        }


        else {
            showErrorConnection();
        }
    }





    public boolean matchUser(String user, String pwd) throws JSONException {
        JSONArray usersList = new JSONArray();
        usersList = ber.getUsersList();

        for(int i = 0; i < usersList.length(); ++i) {
            JSONObject us = usersList.getJSONObject(i);
            String userLogin = us.getString("EMAIL");
            String pwdLogin = us.getString("PASSWORD");

            System.out.println("user: " + userLogin + " " + user);
            System.out.println("password: " + pwdLogin + " " + pwd);


            if(user.equals(userLogin) && pwd.equals(pwdLogin)) {
                System.out.println("It's a match!");
                return true;
            }

            System.out.println(" ");
        }

        return false;
    }



    public void logIn() {
        // Do something in response to button
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }



    void showErrorConnection () {
        new AlertDialog.Builder(this)
                .setTitle(R.string.connection_error)
                .setMessage("😅😅😅😅😅😳")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO: volver a llamar al server (o no)
                    }
                })
                .show();
    }

}