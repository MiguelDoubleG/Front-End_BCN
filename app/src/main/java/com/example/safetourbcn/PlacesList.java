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
            Double lat = us.getDouble("LOCAL_X");
            Double lng = us.getDouble("LOCAL_Y");
            Integer maxCapacity = us.getInt("MAX_CAPACITY");
            Integer houropen = us.getInt("HOUROPEN");
            Integer hourclose = us.getInt("HOURCLOSE");
            String category =  us.getString("CATEGORY");
            Integer price = Integer.parseInt(us.getString("PRICE"));
            Boolean discount = Integer.parseInt(us.getString("DISCOUNT")) == 1;
            String address = us.getString("ADDRESS");
            String website = us.getString("WEBSITE");
            String instagram = us.getString("INSTAGRAM");

            placesList
                    .places
                    .add(new Establishment(id, name, idCompany, description, lat, lng,
                            maxCapacity, houropen, hourclose, category, price, discount, address, website, instagram));
        }
    }

    public int getLength() { return places.size(); }

    public Establishment getEstablishmentById(int i) {
        Establishment e = places.stream()
                .filter(est -> i == est.getId())
                .findFirst()
                .orElse(null);

        return e;
    }

    public Establishment getEstablishment(int i) { return places.get(i); }
}
