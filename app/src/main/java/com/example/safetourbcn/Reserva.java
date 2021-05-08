package com.example.safetourbcn;

public class Reserva {
    private String idUsuario;
    private int numPersonas;

    public Reserva(){}
    public Reserva(String usuario, int numpersonas){
        idUsuario = usuario;
        numPersonas = numpersonas;
    }

    public int getNumPersonas() {
        return numPersonas;
    }

    public String getIdUsuario() {
        return idUsuario;
    }
}
