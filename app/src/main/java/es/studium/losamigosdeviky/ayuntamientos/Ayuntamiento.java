package es.studium.losamigosdeviky.ayuntamientos;

public class Ayuntamiento {
    private int idAyuntamiento;
    private String nombreAyuntamiento;
    private int telefonoAyuntamiento;
    private String responsableAyuntamiento;
    private String direccionAyuntamiento;
    private int cpAyuntamiento;

    public Ayuntamiento(String nombreAyuntamiento, int telefonoAyuntamiento, String responsableAyuntamiento, String direccionAyuntamiento, int cpAyuntamiento) {
        this.nombreAyuntamiento = nombreAyuntamiento;
        this.telefonoAyuntamiento = telefonoAyuntamiento;
        this.responsableAyuntamiento = responsableAyuntamiento;
        this.direccionAyuntamiento = direccionAyuntamiento;
        this.cpAyuntamiento = cpAyuntamiento;
    }

    public int getIdAyuntamiento() {
        return idAyuntamiento;
    }

    public String getNombreAyuntamiento() {
        return nombreAyuntamiento;
    }

    public int getTelefonoAyuntamiento() {
        return telefonoAyuntamiento;
    }

    public String getResponsableAyuntamiento() {
        return responsableAyuntamiento;
    }

    public String getDireccionAyuntamiento() {
        return direccionAyuntamiento;
    }

    public int getCpAyuntamiento() {
        return cpAyuntamiento;
    }

    @Override
    public String toString() {
        return nombreAyuntamiento;
    }
}
