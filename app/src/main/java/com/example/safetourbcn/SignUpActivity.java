package com.example.safetourbcn;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignUpActivity extends AppCompatActivity {
    private LoginViewModel loginViewModel;
    BackEndRequests ber = BackEndRequests.getInstance();
    Session session = Session.getInstance();
    SharedPreferences sharedPreferences;

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
                    EditText passwordEditText = findViewById(R.id.passwordSignUp);
                    EditText passwordREditText = findViewById(R.id.passwordSignUpRepeat);
                    EditText emailEditText = findViewById(R.id.usernameSignUp);

                    boolean a = password1EditText.getText().toString().equals(password1EditText.getText().toString());
                    boolean b = userExisteix(emailEditText.getText().toString());
                    if(a && !b) signUp();
                    else if (!a)showErrorMatch();
                    else {
                        showUserAlreadyExists();
                    }
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
        ber.addUser(
                usernameEditText.getText().toString(),
                passwordEditText.getText().toString(),
                nameEditText.getText().toString());

        System.out.println(usernameEditText.getText().toString());

        SystemClock.sleep(1000);

        login(usernameEditText.getText().toString(), passwordEditText.getText().toString());
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





    public void login(String user, String pwd) {
        String url = ber.getServerAddress() + "/user/login";
        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
        JSONObject userJSON = new JSONObject();

        try {
            userJSON.put("email", user);
            userJSON.put("password", pwd);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(userJSON.toString(), JSON);

        Request request;

        if(session.getApiKey() == null) {
            request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
        }

        else {
            request = new Request.Builder()
                    .url(url)
                    .addHeader("AUTHORIZATION", session.getApiKey())
                    .post(body)
                    .build();
        }

        ber.getClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                ber.setErrorMsg("connection");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showErrorConnection();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.isSuccessful()) {
                    String bodyString = response.body().string();
                    if(bodyString != null) session.setApiKey(bodyString);
                    sharedPreferences = getSharedPreferences("token", MODE_PRIVATE);
                    sharedPreferences.edit().putString("token", bodyString).commit();

                    sharedPreferences = getSharedPreferences("email", MODE_PRIVATE);
                    sharedPreferences.edit().putString("email", user).commit();

                    getUser(user);
                }
            }

        });
    }

    public void getUser(String email) {
        String url = ber.getServerAddress() + "/users/" + email;
        Request request = new Request.Builder()
                .url(url)
                .build();

        ber.getClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                ber.setErrorMsg("connection");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showErrorConnection();
                    }
                });

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.isSuccessful()) {
                    String r = response.body().string();

                    try {
                        JSONArray ja = new JSONArray(r);
                        JSONObject userInfo = ja.getJSONObject(0);
                        ber.setErrorMsg("");
                        session.init(
                                userInfo.getString("NAME"),
                                userInfo.getString("PASSWORD"),
                                userInfo.getString("EMAIL"));
                        sharedPreferences = getSharedPreferences("token", MODE_PRIVATE);
                        String token = sharedPreferences.getString("token", "notoken");

                        session.setApiKey(token);
                        session.init(userInfo);

                        loginActivity();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        ber.setErrorMsg("connection");
                    }
                }
            }

        });
    }

    public void loginActivity() {
        // Do something in response to button
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }



    void showErrorMatch(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                View parentLayout = findViewById(R.id.sul);
                Snackbar.make(parentLayout, R.string.not_match, Snackbar.LENGTH_LONG)
                        .show();
            }
        });
    }



    void showUserAlreadyExists(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                View parentLayout = findViewById(R.id.sul);
                Snackbar.make(parentLayout, "R.string.not_match", Snackbar.LENGTH_LONG)
                        .show();
            }
        });
    }


    boolean userExisteix(String s) throws JSONException {
        if(ber.getUserInfo(s) == null) return false ;
        else return true;
    }
}