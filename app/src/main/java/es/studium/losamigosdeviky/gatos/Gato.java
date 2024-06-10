package es.studium.losamigosdeviky.gatos;

import java.time.LocalDate;
import java.util.Arrays;

public class Gato {
    private int idGato;
    private String nombreGato;
    private String sexoGato;
    private String descripcionGato;
    private int esEsterilizado;
    private String fotoGato;
    private LocalDate fechaNacimientoGato;
    private String chipGato;
    private int idColoniaFK4;

    public Gato(String nombreGato, String sexoGato, String descripcionGato, int esEsterilizado, String fotoGato, LocalDate fechaNacimientoGato, String chipGato, int idColoniaFK4) {
        this.nombreGato = nombreGato;
        this.sexoGato = sexoGato;
        this.descripcionGato = descripcionGato;
        this.esEsterilizado = esEsterilizado;
        this.fotoGato = fotoGato;
        this.fechaNacimientoGato = fechaNacimientoGato;
        this.chipGato = chipGato;
        this.idColoniaFK4 = idColoniaFK4;
    }

    public Gato(int idGato, String nombreGato, String sexoGato, String descripcionGato, int esEsterilizado, String fotoGato, LocalDate fechaNacimientoGato, String chipGato, int idColoniaFK4) {
        this.idGato = idGato;
        this.nombreGato = nombreGato;
        this.sexoGato = sexoGato;
        this.descripcionGato = descripcionGato;
        this.esEsterilizado = esEsterilizado;
        this.fotoGato = fotoGato;
        this.fechaNacimientoGato = fechaNacimientoGato;
        this.chipGato = chipGato;
        this.idColoniaFK4 = idColoniaFK4;
    }

    public int getIdGato() {
        return idGato;
    }

    public String getNombreGato() {
        return nombreGato;
    }

    public String getSexoGato() {
        return sexoGato;
    }

    public String getDescripcionGato() {
        return descripcionGato;
    }

    public int getEsEsterilizado() {
        return esEsterilizado;
    }

    public String getFotoGato() {
        return fotoGato;
    }

    public LocalDate getFechaNacimientoGato() {
        return fechaNacimientoGato;
    }

    public String getChipGato() {
        return chipGato;
    }

    public int getIdColoniaFK4() {
        return idColoniaFK4;
    }

    public void setNombreGato(String nombreGato) {
        this.nombreGato = nombreGato;
    }

    public void setSexoGato(String sexoGato) {
        this.sexoGato = sexoGato;
    }

    public void setDescripcionGato(String descripcionGato) {
        this.descripcionGato = descripcionGato;
    }

    public void setEsEsterilizado(int esEsterilizado) {
        this.esEsterilizado = esEsterilizado;
    }

    public void setFotoGato(String fotoGato) {
        this.fotoGato = fotoGato;
    }

    public void setFechaNacimientoGato(LocalDate fechaNacimientoGato) {
        this.fechaNacimientoGato = fechaNacimientoGato;
    }

    public void setChipGato(String chipGato) {
        this.chipGato = chipGato;
    }

    public void setIdColoniaFK4(int idColoniaFK4) {
        this.idColoniaFK4 = idColoniaFK4;
    }

    @Override
    public String toString() {
        return "Gato{" +
                "idGato=" + idGato +
                ", nombreGato='" + nombreGato + '\'' +
                ", sexoGato='" + sexoGato + '\'' +
                ", descripcionGato='" + descripcionGato + '\'' +
                ", esEsterilizado=" + esEsterilizado +
                ", fotoGato=" + fotoGato +
                ", fechaNacimientoGato=" + fechaNacimientoGato +
                ", chipGato='" + chipGato + '\'' +
                ", idColoniaFK4=" + idColoniaFK4 +
                '}';
    }
}
