package com.example.safetourbcn;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class BookingsActivity extends AppCompatActivity {
    Session session = Session.getInstance();
    BackEndRequests ber = BackEndRequests.getInstance();
    AppBarConfiguration appBarConfiguration;
    ArrayList<String> bookingList;
    BackEndRequests backEndRequests = BackEndRequests.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookings);

        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.bookingsActivity)
                .build();

        getBookingsList();

    }


    public void print(JSONObject jo) {
        System.out.println(jo.toString());
    }

    public void getBookingsList() {
        String url = ber.getServerAddress() + "/Users/Reservations";
        Request request = new Request.Builder()
                .url(url)
                .addHeader("AUTHORIZATION", session.getApiKey())
                .build();

        ber.getClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                ber.setErrorMsg("connection");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.isSuccessful()) {
                    System.out.println("ddddddddddddddx");
                    String r = response.body().string();

                    try {
                        JSONArray ja = new JSONArray(r);
                        for(int i = 0; i < ja.length(); ++i) {
                            JSONObject jo = ja.getJSONObject(i);

                            print(jo);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        ber.setErrorMsg("connection");
                    }
                }

                else {
                    String r = response.body().string();
                    System.out.println(r);
                }
            }

        });
    }
}