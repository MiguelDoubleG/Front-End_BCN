package com.example.safetourbcn;

public class Usuario {
    private String nombre;
    private String password;
    private String id;

    Usuario(){
    }

    Usuario(String nom, String pass, String identification){
        nombre = nom;
        password = pass;
        id = identification;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getPassword() {
        return password;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
