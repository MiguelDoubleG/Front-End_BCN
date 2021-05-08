package com.example.safetourbcn;

public class UsuarioIndividual extends Usuario {
    private String LocActual;

    public UsuarioIndividual(){
    }

    public UsuarioIndividual(String nom, String pass, String id){
        super(nom, pass, id);
    }

    @Override
    public String getEmail() {
        return super.getEmail();
    }

    @Override
    public String getNombre() {
        return super.getNombre();
    }

    @Override
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    public void setNombre(String nombre) {
        super.setNombre(nombre);
    }

    @Override
    public void setPassword(String password) {
        super.setPassword(password);
    }

    public String getLocActual() {
        if (!LocActual.isEmpty()) return LocActual;
        else return "";
    }

    public void setLocActual(String locActual) {
        LocActual = locActual;
    }
}