package com.example.safetourbcn;

public class Usuario {
    private String nombre;
    private String password;
    private String email;

    public Usuario(){
    }

    public Usuario(String nom, String pass, String mail){
        nombre = nom;
        password = pass;
        email = mail;
    }

    public String getEmail(){ return email; }

    public String getNombre() {
        return nombre;
    }

    public String getPassword() {
        return password;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
