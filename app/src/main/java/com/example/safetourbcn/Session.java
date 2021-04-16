package com.example.safetourbcn;

import org.json.JSONException;
import org.json.JSONObject;

public class Session {
    private static Session currentSession;
    private UsuarioIndividual currentUser;

    Session() {}

    public static Session getInstance() {
        if(currentSession == null) {
            currentSession = new Session();
        }

        return currentSession;
    }

    public void init(JSONObject info) throws JSONException {
        currentUser = new UsuarioIndividual(info.getString("NAME"), info.getString("PASSWORD"),info.getString("EMAIL"));
    }
}
