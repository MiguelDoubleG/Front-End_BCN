package com.example.safetourbcn;

public class ValoracionEvento extends Valoracion {
    private String idEvento;

    public ValoracionEvento(int puntuacion, String comentario, String idValoracion, Boolean conReserva, Boolean[] medidas, String idusuario, String idevento) {
        super(puntuacion, comentario, idValoracion, conReserva, medidas, idusuario);
        idEvento = idevento;
    }

    @Override
    public String getIdUsuario() {
        return super.getIdUsuario();
    }

    @Override
    public Boolean getConReserva() {
        return super.getConReserva();
    }

    @Override
    public Boolean[] getMedidas() {
        return super.getMedidas();
    }

    @Override
    public int getPuntuacion() {
        return super.getPuntuacion();
    }

    @Override
    public String getComentario() {
        return super.getComentario();
    }

    @Override
    public String getIdValoracion() {
        return super.getIdValoracion();
    }

    public String getIdEvento() {
        return idEvento;
    }

    @Override
    public void setComentario(String comentario) {
        super.setComentario(comentario);
    }

    @Override
    public void setMedidas(Boolean[] medidas) {
        super.setMedidas(medidas);
    }

    @Override
    public void setPuntuacion(int puntuacion) {
        super.setPuntuacion(puntuacion);
    }

}
