package es.studium.losamigosdeviky.gatos;

import java.time.LocalDate;
import java.util.Arrays;

public class Gato {
    private int idGato;
    private String nombreGato;
    private String sexoGato;
    private String descripcionGato;
    private int esEsterilizado;
    private byte[] fotoGato;
    private LocalDate fechaNacimientoGato;
    private String chipGato;
    private int idVeterinarioFK3;
    private int idColoniaFK4;

    public Gato(String nombreGato, String sexoGato, String descripcionGato, int esEsterilizado, byte[] fotoGato, LocalDate fechaNacimientoGato, String chipGato, int idVeterinarioFK3, int idColoniaFK4) {
        this.nombreGato = nombreGato;
        this.sexoGato = sexoGato;
        this.descripcionGato = descripcionGato;
        this.esEsterilizado = esEsterilizado;
        this.fotoGato = fotoGato;
        this.fechaNacimientoGato = fechaNacimientoGato;
        this.chipGato = chipGato;
        this.idVeterinarioFK3 = idVeterinarioFK3;
        this.idColoniaFK4 = idColoniaFK4;
    }

    public Gato(int idGato, String nombreGato, String sexoGato, String descripcionGato, int esEsterilizado, byte[] fotoGato, LocalDate fechaNacimientoGato, String chipGato, int idVeterinarioFK3, int idColoniaFK4) {
        this.idGato = idGato;
        this.nombreGato = nombreGato;
        this.sexoGato = sexoGato;
        this.descripcionGato = descripcionGato;
        this.esEsterilizado = esEsterilizado;
        this.fotoGato = fotoGato;
        this.fechaNacimientoGato = fechaNacimientoGato;
        this.chipGato = chipGato;
        this.idVeterinarioFK3 = idVeterinarioFK3;
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

    public byte[] getFotoGato() {
        return fotoGato;
    }

    public LocalDate getFechaNacimientoGato() {
        return fechaNacimientoGato;
    }

    public String getChipGato() {
        return chipGato;
    }

    public int getIdVeterinarioFK3() {
        return idVeterinarioFK3;
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

    public void setFotoGato(byte[] fotoGato) {
        this.fotoGato = fotoGato;
    }

    public void setFechaNacimientoGato(LocalDate fechaNacimientoGato) {
        this.fechaNacimientoGato = fechaNacimientoGato;
    }

    public void setChipGato(String chipGato) {
        this.chipGato = chipGato;
    }

    public void setIdVeterinarioFK3(int idVeterinarioFK3) {
        this.idVeterinarioFK3 = idVeterinarioFK3;
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
                ", fotoGato=" + Arrays.toString(fotoGato) +
                ", fechaNacimientoGato=" + fechaNacimientoGato +
                ", chipGato='" + chipGato + '\'' +
                ", idVeterinarioFK3=" + idVeterinarioFK3 +
                ", idColoniaFK4=" + idColoniaFK4 +
                '}';
    }
}
