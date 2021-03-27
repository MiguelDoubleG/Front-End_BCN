package com.example.safetourbcn.ui.login;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.safetourbcn.BackEndRequests;
import com.example.safetourbcn.MapsActivity;
import com.example.safetourbcn.R;
import com.example.safetourbcn.SignUpActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class LoginActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.SafeTourBCN.MESSAGE";
    private LoginViewModel loginViewModel;
    OkHttpClient client;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        client = new OkHttpClient();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final EditText usernameEditText = findViewById(R.id.usernameLogIn);
        final EditText passwordEditText = findViewById(R.id.passwordLogIn);
        final Button loginButton = findViewById(R.id.login);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);


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
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                //loginViewModel.login(usernameEditText.getText().toString(), passwordEditText.getText().toString());

                //EditText usernameEditText = findViewById(R.id.usernameLogIn);
                //EditText passwordEditText = findViewById(R.id.passwordLogIn);
                matchUser(usernameEditText.getText().toString(), passwordEditText.getText().toString());
            }
        });



    }

    /** Called when the user taps the Send button */
    public void registerButton(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    private void updateUiWithUser(LoggedInUserView model) {
        EditText usernameEditText = findViewById(R.id.usernameLogIn);
        EditText passwordEditText = findViewById(R.id.passwordLogIn);

        addUser(usernameEditText.getText().toString(), passwordEditText.getText().toString());


        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }





    //////////////////////////////////////////////
    //////////////Requests///////////////////////
    ////////////////////////////////////////////

    public void matchUser(String user, String pwd) {
        final boolean match = false;
        String url = "http://10.4.41.144:3000/users";

        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.isSuccessful()) {
                    String r = response.body().string();

                    try {
                        JSONArray usersList = new JSONArray(r);

                        System.out.println("Lista users y pwd:");

                        for(int i = 0; i < usersList.length(); ++i) {
                            JSONObject us = usersList.getJSONObject(i);
                            String userLogin = us.getString("EMAIL");
                            String pwdLogin = us.getString("PASSWORD");


                            System.out.println("user: " + userLogin + " " + user);
                            System.out.println("password: " + pwdLogin + " " + pwd);


                            if(user.equals(userLogin) && pwd.equals(pwdLogin)) {
                                System.out.println("It's a match!");
                                Intent intent = new Intent(LoginActivity.super.getApplicationContext(), MapsActivity.class);
                                startActivity(intent);
                            }

                            System.out.println(" ");
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }
        });
    }


    public void addUser(String user, String pwd) {
        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
        JSONObject newUser = new JSONObject();
        String url = "http://10.4.41.144:3000/registerIndividualUser";

        try {
            newUser.put("email", user);
            newUser.put("username", "test");
            newUser.put("password", pwd);
        } catch (JSONException e) {
            Log.d("OKHTTP3", "JSON Excepton");
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(newUser.toString(), JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body).
                        build();
        System.out.println(" ");
        System.out.println("user: " + user);
        System.out.println("pwd: " + pwd);
        System.out.println(" ");

        try {
            Response response = client.newCall(request).execute();
        } catch (IOException e) {
            System.out.println("ERROR//////////////////////////////////////////7");
            e.printStackTrace();
        }
    }










}