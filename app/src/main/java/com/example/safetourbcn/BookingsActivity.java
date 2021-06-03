package com.example.safetourbcn;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    ArrayList<JSONObject> bookingList;
    BackEndRequests backEndRequests = BackEndRequests.getInstance();
    RecyclerView mRecentRecyclerView;
    LinearLayoutManager mRecentLayoutManager;
    RecyclerView.Adapter<CustomViewHolder> mAdapter;
    PlacesList placesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookings);
        placesList = PlacesList.getInstance();
        bookingList = new ArrayList<>();
        getBookingsList();
        setInfoToList();
        mRecentRecyclerView = (RecyclerView) findViewById(R.id.bookingsList);
        //mRecentRecyclerView.setHasFixedSize(true);
        mRecentLayoutManager = new LinearLayoutManager(this);
        mRecentRecyclerView.setLayoutManager(mRecentLayoutManager);
        mRecentRecyclerView.setAdapter(mAdapter);

        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.bookingsActivity)
                .build();
    }


    public void print(JSONObject jo) {
        System.out.println(jo.toString());
    }

    public void getBookingsList() {
        String url = ber.getServerAddress() + "/User/Reservations";
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
                    System.out.println("response: " + r);

                    try {
                        JSONArray ja = new JSONArray(r);
                        if(ja.length() == 0) {

                        }
                        for(int i = 0; i < ja.length(); ++i) {
                            JSONObject jo = ja.getJSONObject(i);
                            bookingList.add(jo);
                            print(jo);
                        }

                        setInfoToList();

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



    void setInfoToList() {
        mAdapter = new RecyclerView.Adapter<CustomViewHolder>() {
            @Override
            public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                View view = LayoutInflater
                        .from(viewGroup.getContext())
                        .inflate(R.layout.booking_view, viewGroup, false);
                return new CustomViewHolder(view);
            }

            @Override
            public void onBindViewHolder(CustomViewHolder viewHolder, int i) {

                try {
                    Establishment e = placesList
                            .getEstablishmentById(bookingList.get(i).getInt("ID_RESERVATION"));

                    viewHolder.nom.setText(e.getName());
                    viewHolder.desc.setText(bookingList.get(i).getString("RESERVATION_DATE"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public int getItemCount() {
                return bookingList.size();
            }

        };
        mAdapter.notifyItemInserted(bookingList.size() - 1);
    }

    private class CustomViewHolder extends RecyclerView.ViewHolder {

        private TextView nom;
        private TextView desc;

        public CustomViewHolder(View itemView) {
            super(itemView);
            nom = (TextView) itemView.findViewById(R.id.nomEstablishmentBooking);
            desc = (TextView) itemView.findViewById(R.id.infoEstablishmentBooking);
        }
    }
}