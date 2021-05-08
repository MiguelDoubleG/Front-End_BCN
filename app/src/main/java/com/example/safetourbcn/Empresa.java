package com.example.safetourbcn;

public class Empresa extends Usuario {
    private String descripcion;

    public Empresa(){
        super();
    }
    public Empresa(String nom, String pass, String id, String desc){
        super(nom, pass, id);
        descripcion = desc;
    }

    @Override
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    public String getNombre() {
        return super.getNombre();
    }

    @Override
    public String getEmail() {
        return super.getEmail();
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public void setPassword(String password) {
        super.setPassword(password);
    }

    @Override
    public void setNombre(String nombre) {
        super.setNombre(nombre);
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}