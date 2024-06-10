package es.studium.losamigosdeviky.cuidados;

import java.time.LocalDate;

public class Cuidado {
    private int idCuidado;
    private LocalDate fechaInicioCuidado;
    private LocalDate fechaFinCuidado;
    private String descripcionCuidado;
    private String posologiaCuidado;
    private int idGatoFK;
    private int idVeterinarioFK;

    public Cuidado(LocalDate fechaInicioCuidado, LocalDate fechaFinCuidado, String descripcionCuidado, String posologiaCuidado, int idGatoFK, int idVeterinarioFK) {
        this.fechaInicioCuidado = fechaInicioCuidado;
        this.fechaFinCuidado = fechaFinCuidado;
        this.descripcionCuidado = descripcionCuidado;
        this.posologiaCuidado = posologiaCuidado;
        this.idGatoFK = idGatoFK;
        this.idVeterinarioFK = idVeterinarioFK;
    }

    public Cuidado(int idCuidado, LocalDate fechaInicioCuidado, LocalDate fechaFinCuidado, String descripcionCuidado, String posologiaCuidado, int idGatoFK, int idVeterinarioFK) {
        this.idCuidado = idCuidado;
        this.fechaInicioCuidado = fechaInicioCuidado;
        this.fechaFinCuidado = fechaFinCuidado;
        this.descripcionCuidado = descripcionCuidado;
        this.posologiaCuidado = posologiaCuidado;
        this.idGatoFK = idGatoFK;
        this.idVeterinarioFK = idVeterinarioFK;
    }

    public int getIdCuidado() {
        return idCuidado;
    }

    public LocalDate getFechaInicioCuidado() {
        return fechaInicioCuidado;
    }

    public LocalDate getFechaFinCuidado() {
        return fechaFinCuidado;
    }

    public String getDescripcionCuidado() {
        return descripcionCuidado;
    }

    public String getPosologiaCuidado() {
        return posologiaCuidado;
    }

    public int getIdGatoFK() {
        return idGatoFK;
    }

    public int getIdVeterinarioFK() {
        return idVeterinarioFK;
    }

    public void setFechaInicioCuidado(LocalDate fechaInicioCuidado) {
        this.fechaInicioCuidado = fechaInicioCuidado;
    }

    public void setFechaFinCuidado(LocalDate fechaFinCuidado) {
        this.fechaFinCuidado = fechaFinCuidado;
    }

    public void setDescripcionCuidado(String descripcionCuidado) {
        this.descripcionCuidado = descripcionCuidado;
    }

    public void setPosologiaCuidado(String posologiaCuidado) {
        this.posologiaCuidado = posologiaCuidado;
    }

    public void setIdGatoFK(int idGatoFK) {
        this.idGatoFK = idGatoFK;
    }

    public void setIdVeterinarioFK(int idVeterinarioFK) {
        this.idVeterinarioFK = idVeterinarioFK;
    }

    @Override
    public String toString() {
        return "Cuidado{" +
                "idCuidado=" + idCuidado +
                ", fechaInicioCuidado=" + fechaInicioCuidado +
                ", fechaFinCuidado=" + fechaFinCuidado +
                ", descripcionCuidado='" + descripcionCuidado + '\'' +
                ", posologiaCuidado='" + posologiaCuidado + '\'' +
                ", idGatoFK=" + idGatoFK +
                ", idVeterinarioFK=" + idVeterinarioFK +
                '}';
    }
}
