package com.example.safetourbcn;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PlacesList {
    private static PlacesList placesList;
    private List<Establishment> places;

    private PlacesList(){}

    public static PlacesList getInstance() {
        if(placesList == null) {
            placesList = new PlacesList();
        }

        return placesList;
    }


    public void updateList (JSONArray ja) throws JSONException {
        placesList.places = new ArrayList<Establishment>();

        for(int i = 0; i < ja.length(); ++i) {
            JSONObject us = ja.getJSONObject(i);

            int id = us.getInt("ID_ESTABLISHMENT");
            String name = us.getString("NAME");
            String idCompany = us.getString("OWNER");
            String description = us.getString("DESCRIPTION");
            double lat = us.getDouble("LOCAL_X");
            double lng = us.getDouble("LOCAL_Y");
            int maxCapacity = us.getInt("MAX_CAPACITY");
            String schedule = us.getString("SCHEDULE");

            placesList
                    .places
                    .add(new Establishment(id, name, idCompany,
                            description, lat, lng, maxCapacity, schedule));
        }
    }

    public int getLength() { return places.size(); }

    public Establishment getEstablishment(int i) { return places.get(i); }
}
