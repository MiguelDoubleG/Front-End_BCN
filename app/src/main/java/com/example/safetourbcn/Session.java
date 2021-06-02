package com.example.safetourbcn;

import org.json.JSONException;
import org.json.JSONObject;

public class Session {
    private static Session currentSession;
    private UsuarioIndividual currentUser;
    private String apiKey;

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

    public void init(String name, String password, String email) throws JSONException {
        currentUser = new UsuarioIndividual(name, password, email);
    }


    public void initGoogle(String name, String password, String email) {
        currentUser = new UsuarioIndividual(name, password, email);
    }

    public void editUserName(String name) {
        currentUser.setNombre(name);
    }

    public String getName() { return currentUser.getNombre();}

    public String getEmail() { return currentUser.getEmail();}

    public String getPassword() { return currentUser.getPassword();}

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = "Bearer " + apiKey;
    }
}
