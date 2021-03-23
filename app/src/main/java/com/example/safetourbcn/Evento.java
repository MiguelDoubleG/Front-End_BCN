package com.example.safetourbcn;

import java.util.Date;

public class Evento {
    private String localizacion;
    private String descripcion;
    private String idEvento;
    private int PlazasDisponibles;
    private Date date;
    private String horaInici;
    private String horaFinal;
    private String idEmpresa;

    public Evento(){
    }
    public Evento(String loc, String desc, String id, int plazas, Date fecha, String inicio, String horafinal, String empresa){
        localizacion = loc;
        descripcion = desc;
        idEvento = id;
        PlazasDisponibles = plazas;
        horaInici = inicio;
        horaFinal = horafinal;
        idEmpresa = empresa;
    }

    public String getLocalizacion() {
        return localizacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Date getDate() {
        return date;
    }

    public String getHoraFinal() {
        return horaFinal;
    }

    public String getHoraInici() {
        return horaInici;
    }

    public int getPlazasDisponibles() {
        return PlazasDisponibles;
    }

    public String getIdEvento() {
        return idEvento;
    }

    public String getIdEmpresa() {
        return idEmpresa;
    }

    public void setLocalizacion(String localizacion) {
        this.localizacion = localizacion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setHoraFinal(String horaFinal) {
        this.horaFinal = horaFinal;
    }

    public void setHoraInici(String horaInici) {
        this.horaInici = horaInici;
    }

    public void setIdEvento(String idEvento) {
        this.idEvento = idEvento;
    }

    public void setPlazasDisponibles(int plazasDisponibles) {
        PlazasDisponibles = plazasDisponibles;
    }

    /*
    public void setIdEmpresa(String idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

     */
}