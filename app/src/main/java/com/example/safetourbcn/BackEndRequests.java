package com.example.safetourbcn;

import android.app.DownloadManager;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BackEndRequests {
    RequestQueue rq;
    String url = "";

    //devuelve true si hacen match
    //context es "this" cuando se accede a la funcion desde una activity

    public BackEndRequests() {
    }

    public boolean getUsers(String user, String password, Context context) {
        final boolean[] match = {false};
        rq = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray listUsers = response.getJSONArray("");

                    for(int i =  0; i < listUsers.length(); ++i) {
                        JSONObject user = listUsers.getJSONObject(i);
                        String id = user.getString("EMAIL");
                        String pwd = user.getString("PASSWORD");

                        if (id.equals(user) && pwd.equals(password)) match[0] = true;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        rq.add(request);
        return match[0];
    }

}




