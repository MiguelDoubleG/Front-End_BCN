package com.example.safetourbcn;

import java.util.Date;

public class ReservaEstablecimiento extends Reserva{
    private String hora;
    private Date fecha;
    private String idEstablecimiento;

    public ReservaEstablecimiento(){}

    public ReservaEstablecimiento(String idusuario, int numpersonas, String hora, Date data, String establecimiento) {
        super(idusuario, numpersonas);
        this.hora = hora;
        fecha = data;
        idEstablecimiento = establecimiento;
    }

    @Override
    public int getNumPersonas() {
        return super.getNumPersonas();
    }

    @Override
    public String getIdUsuario() {
        return super.getIdUsuario();
    }

    public Date getFecha() {
        return fecha;
    }

    public String getHora() {
        return hora;
    }

    public String getIdEstablecimiento() {
        return idEstablecimiento;
    }
}
