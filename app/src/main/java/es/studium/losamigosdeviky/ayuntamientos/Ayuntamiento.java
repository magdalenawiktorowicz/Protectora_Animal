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

    public Ayuntamiento(int idAyuntamiento, String nombreAyuntamiento, int telefonoAyuntamiento, String responsableAyuntamiento, String direccionAyuntamiento, int cpAyuntamiento) {
        this.idAyuntamiento = idAyuntamiento;
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

    void setNombreAyuntamiento(String nombreAyuntamiento) {
        this.nombreAyuntamiento = nombreAyuntamiento;
    }

    void setTelefonoAyuntamiento(int telefonoAyuntamiento) {
        this.telefonoAyuntamiento = telefonoAyuntamiento;
    }

    void setResponsableAyuntamiento(String responsableAyuntamiento) {
        this.responsableAyuntamiento = responsableAyuntamiento;
    }

    void setDireccionAyuntamiento(String direccionAyuntamiento) {
        this.direccionAyuntamiento = direccionAyuntamiento;
    }

    void setCpAyuntamiento(int cpAyuntamiento) {
        this.cpAyuntamiento = cpAyuntamiento;
    }

    @Override
    public String toString() {
        return "Ayuntamiento{" +
                "idAyuntamiento=" + idAyuntamiento +
                ", nombreAyuntamiento='" + nombreAyuntamiento + '\'' +
                ", telefonoAyuntamiento=" + telefonoAyuntamiento +
                ", responsableAyuntamiento='" + responsableAyuntamiento + '\'' +
                ", direccionAyuntamiento='" + direccionAyuntamiento + '\'' +
                ", cpAyuntamiento=" + cpAyuntamiento +
                '}';
    }
}
