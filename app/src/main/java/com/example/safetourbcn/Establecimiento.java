package com.example.safetourbcn;

public class Establecimiento {
    private String localizacion;
    private String id;
    private String descripcion;
    private int aforoMax;
    private String horario;
    private String idEmpresa;

    public Establecimiento(){ }
    public Establecimiento(String loc, String id, String desc, int aforo, String horario, String empresa){
        localizacion = loc;
        this.id = id;
        descripcion = desc;
        aforoMax = aforo;
        this.horario = horario;
        idEmpresa = empresa;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getAforoMax() {
        return aforoMax;
    }

    public String getId() {
        return id;
    }

    public String getHorario() {
        return horario;
    }

    public String getLocalizacion() {
        return localizacion;
    }

    public String getIdEmpresa() {
        return idEmpresa;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAforoMax(int aforoMax) {
        this.aforoMax = aforoMax;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public void setLocalizacion(String localizacion) {
        this.localizacion = localizacion;
    }

}