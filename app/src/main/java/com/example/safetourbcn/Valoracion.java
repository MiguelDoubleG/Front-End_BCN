package com.example.safetourbcn;

public class Valoracion {
    private int puntuacion;
    private String comentario;
    private String idValoracion;
    private Boolean ConReserva;
    private Boolean[] medidas = new Boolean[4];
    private String idUsuario;


    public Valoracion(){}
    public Valoracion(int puntuacion, String comentario, String idValoracion, Boolean conReserva, Boolean[] medidas, String idusuario) {
        this.puntuacion = puntuacion;
        this.comentario = comentario;
        this.idValoracion = idValoracion;
        ConReserva = conReserva;
        this.medidas = medidas;
        idUsuario = idusuario;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public String getIdValoracion() {
        return idValoracion;
    }

    public String getComentario() {
        return comentario;
    }

    public Boolean getConReserva() {
        return ConReserva;
    }

    public Boolean[] getMedidas() {
        return medidas;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public void setMedidas(Boolean[] medidas) {
        this.medidas = medidas;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }
}
