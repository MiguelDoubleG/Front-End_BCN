package com.example.safetourbcn;

import android.app.DownloadManager;
import android.content.Context;
import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BackEndRequests {
    OkHttpClient client = new OkHttpClient();

    //devuelve true si hacen match
    //context es "this" cuando se accede a la funcion desde una activity

    public BackEndRequests() {
    }

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


                            if(user.equals(userLogin) && pwd.equals(pwdLogin)) System.out.println("It's a match!");

                            System.out.println(" ");
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }
        });
    }

}




