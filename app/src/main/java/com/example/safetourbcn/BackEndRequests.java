package com.example.safetourbcn;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

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

public class BackEndRequests {
    private static BackEndRequests ber;
    private OkHttpClient client;
    private JSONArray usersList;
    private JSONArray placesList;
    private String errorMsg;


    private BackEndRequests() {
        client = new OkHttpClient();
        errorMsg = "connection";

        updateUsersList();
    }

    public static BackEndRequests getInstance() {
        if(ber == null) {
            ber = new BackEndRequests();
        }

        return ber;
    }




    /////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////USERS//////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////

    public void updateUsersList() {
        String url = "http://10.4.41.144:3000/users";

        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                errorMsg = "connection";
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.isSuccessful()) {
                    String r = response.body().string();

                    try {
                        usersList = new JSONArray(r);
                        errorMsg = "";
                    } catch (JSONException e) {
                        e.printStackTrace();
                        errorMsg = "connection";
                    }

                }
            }

        });
    }


    public JSONArray getUsersList() { return usersList;}



    public void addUser(String user, String pwd, String name) {
        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
        JSONObject newUser = new JSONObject();
        String url = "http://10.4.41.144:3000/registerIndividualUser";

        try {
            newUser.put("email", user);
            newUser.put("username", name);
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
            updateUsersList();
        } catch (IOException e) {
            System.out.println("ERROR//////////////////////////////////////////7");
            e.printStackTrace();
        }
    }

    public JSONObject getUserInfo(String email) throws JSONException {
        for(int i = 0; i < usersList.length(); ++i) {
            JSONObject us = usersList.getJSONObject(i);

            String userLogin = us.getString("EMAIL");

            if(email.equals(userLogin)) {
                return us;
            }
        }

        return null;
    }



    /////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////PLACES/////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////

    public void updatePlacesList() {
        String url = "http://10.4.41.144:3000/establishments";

        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                errorMsg = "connection";
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.isSuccessful()) {
                    String r = response.body().string();

                    try {
                        placesList = new JSONArray(r);
                        errorMsg = "";

                        PlacesList pl = PlacesList.getInstance();
                        pl.updateList(placesList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        errorMsg = "connection";
                    }

                }
            }
        });
    }


    public JSONArray getPlacesListList() { return placesList;}










    public String getErrorMsg() {
        return errorMsg;
    }
}




