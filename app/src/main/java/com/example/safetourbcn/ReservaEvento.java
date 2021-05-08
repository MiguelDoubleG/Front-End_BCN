package com.example.safetourbcn;

public class ReservaEvento extends Reserva{
    private String idEvento;

    public ReservaEvento(){}
    public ReservaEvento(String idusuario, int numpersonas, String idevento){
        super(idusuario, numpersonas);
        idEvento = idevento;
    }
    @Override
    public String getIdUsuario() {
        return super.getIdUsuario();
    }

    @Override
    public int getNumPersonas() {
        return super.getNumPersonas();
    }

    public String getIdEvento() {
        return idEvento;
    }

}
