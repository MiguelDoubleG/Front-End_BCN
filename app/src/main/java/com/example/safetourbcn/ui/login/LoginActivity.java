package com.example.safetourbcn.ui.login;

import android.app.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.safetourbcn.Session;
import com.example.safetourbcn.SignUpActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.Signature;

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
    private Session session;
    BackEndRequests ber = BackEndRequests.getInstance();
    GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN = 0;
    SharedPreferences sharedPreferences;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = Session.getInstance();
        setContentView(R.layout.activity_login);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final EditText usernameEditText = findViewById(R.id.usernameLogIn);
        final EditText passwordEditText = findViewById(R.id.passwordLogIn);
        final Button loginButton = findViewById(R.id.login);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);




        /////GOOGLE SIGN IN/////////////////////////////////
        final SignInButton signInGoogleButton = findViewById(R.id.log_in_google_button);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                                        .requestEmail()
                                                        .build();
        
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        signInGoogleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.log_in_google_button:
                        logInGoogle();
                        break;
                }
            }
        });

        ////////////////////////////////////////////////////////


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

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                sharedPreferences = getSharedPreferences("token", MODE_PRIVATE);
                String token = sharedPreferences.getString("token", "notoken");

                if(token == "notoken")
                    login(usernameEditText.getText().toString(), passwordEditText.getText().toString());
                else {
                    //loginActivity();
                    getUser(usernameEditText.getText().toString());
                    Log.d("aa", token);
                }

            }
        });
        sharedPreferences = getSharedPreferences("token", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "notoken");

        sharedPreferences = getSharedPreferences("email", MODE_PRIVATE);
        String mail = sharedPreferences.getString("email", "nomail");

        if(token != "notoken") {
            getUser(mail);
            System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" + mail);
        }
    }

    /** Called when the user taps the Send button */
    public void signUpButton(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
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
                else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showErrorMatch();
                        }
                    });
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
    
    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }


    public void logInGoogle() {
        // Do something in response to button
        if(ber.getErrorMsg().equals("connection")) {
            showErrorConnection();
            return;
        }

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            loginActivity();
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Error", "signInResult:failed code=" + e.getStatusCode());
            showErrorConnection();
        }
    }


    //////////////////////////////////////////////
    //////////////Requests///////////////////////
    ////////////////////////////////////////////

    public boolean matchUser(String user, String pwd) throws JSONException {
        JSONArray usersList = new JSONArray();
        usersList = ber.getUsersList();

        System.out.println(ber.getErrorMsg());
        if (ber.getErrorMsg().equals("connection")) {
            return false;
        }

        for(int i = 0; i < usersList.length(); ++i) {
            JSONObject us = usersList.getJSONObject(i);
            String userLogin = us.getString("EMAIL");
            String pwdLogin = us.getString("PASSWORD");

            System.out.println("user: " + userLogin + " " + user);
            System.out.println("password: " + pwdLogin + " " + pwd);


            if(user.equals(userLogin) && pwd.equals(pwdLogin)) {
                System.out.println("It's a match!");
                //Iniciar session
                session.init(us);
                return true;
            }

            System.out.println(" ");
        }

        return false;
    }


    void showErrorMatch () {
        new AlertDialog.Builder(this)
                .setTitle(R.string.not_match)
                .setMessage("😅😅😅😅😅😳")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
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
                        finish();
                        startActivity(getIntent());
                    }
                })
                .show();
    }

    @Override
    public void onBackPressed() {}

}