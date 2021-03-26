package com.example.safetourbcn;

import android.app.DownloadManager;
import android.content.Context;

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

    public boolean getUsers(String user, String password, Context context) {
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

                    System.out.println(r);
                }
            }
        });
        return false;
    }

}




