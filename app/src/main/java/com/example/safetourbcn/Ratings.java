package com.example.safetourbcn;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

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


public class Ratings extends AppCompatActivity {
    Session session = Session.getInstance();
    BackEndRequests ber = BackEndRequests.getInstance();
    JSONArray ja = ber.getUsersList();
    int N = 0;
    String[] authors;
    String[] descriptions;
    int[] values;
    ListView simpleList;
    CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratings);
        simpleList = (ListView) findViewById(R.id.simpleListView);
        getRatings();

        int stars[] = {R.drawable.onestar, R.drawable.twostar, R.drawable.threestar, R.drawable.fourstar, R.drawable.fivestar};

        //take the value of the ratings and translate it into images
        for(int i=0 ;i<N;++i)
        {
            if(values[i] == 1) values[i] = stars[1-1];
            else if(values[i] == 2) values[i] = stars[2-1];
            else if(values[i] == 3) values[i] = stars[3-1];
            else if(values[i] == 4) values[i] = stars[4-1];
            else if(values[i] == 5) values[i] = stars[5-1];
        }
        simpleList.setAdapter(customAdapter);
    }

    public void getRatings() {
        String id = getIntent().getStringExtra("ESTABLISHMENT_ID");
        String url = ber.getServerAddress() + "/Establishment/"+id+"/Ratings";
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
                    System.out.println("DX");
                    String r = response.body().string();
                    System.out.println("response: " + r);
                    try {
                        JSONArray ja = new JSONArray(r);
                        N = ja.length();
                        authors = new String[N];
                        descriptions = new String[N];
                        values = new int[N];

                        for(int i = 0; i < N; ++i) {
                            JSONObject jo = ja.getJSONObject(i);
                            addItem(jo, i);
                        }
                        System.out.println(descriptions[0]);
                        System.out.println(descriptions[1]);
                        try {
                            Thread.sleep(5000);
                            customAdapter = new CustomAdapter(getApplicationContext(), descriptions, values,authors);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        ber.setErrorMsg("connection");
                    }
                }

                else {
                    String r = response.body().string();
                    System.out.println("response: " + r);
                    System.out.println("token " + "Bearer " + session.getApiKey());
                }
            }

        });
    }

    public void addItem(JSONObject obj, int i){
        try {
            String id = obj.getString("ID_AUTHOR");
            double value = obj.getDouble("VALUE");
            String description = obj.getString("DESCRIPTION");
            JSONObject jo = ber.getUserInfo(id);
            String author = jo.getString("NAME");
            authors[i] = author;
            descriptions[i] = description;
            values[i] = (int)value;
        }catch(Exception e){}

    }
}
