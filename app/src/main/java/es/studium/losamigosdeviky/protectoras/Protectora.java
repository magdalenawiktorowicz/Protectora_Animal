package es.studium.losamigosdeviky.protectoras;

public class Protectora {
    private int idProtectora;
    private String nombreProtectora;
    private String direccionProtectora;
    private String localidadProtectora;
    private int telefonoProtectora;
    private String correoProtectora;

    public Protectora(String nombreProtectora, String direccionProtectora, String localidadProtectora, int telefonoProtectora, String correoProtectora) {
        this.nombreProtectora = nombreProtectora;
        this.direccionProtectora = direccionProtectora;
        this.localidadProtectora = localidadProtectora;
        this.telefonoProtectora = telefonoProtectora;
        this.correoProtectora = correoProtectora;
    }

    public Protectora(int idProtectora, String nombreProtectora, String direccionProtectora, String localidadProtectora, int telefonoProtectora, String correoProtectora) {
        this.idProtectora = idProtectora;
        this.nombreProtectora = nombreProtectora;
        this.direccionProtectora = direccionProtectora;
        this.localidadProtectora = localidadProtectora;
        this.telefonoProtectora = telefonoProtectora;
        this.correoProtectora = correoProtectora;
    }

    public int getIdProtectora() {
        return idProtectora;
    }

    public String getNombreProtectora() {
        return nombreProtectora;
    }

    public String getDireccionProtectora() {
        return direccionProtectora;
    }

    public String getLocalidadProtectora() {
        return localidadProtectora;
    }

    public int getTelefonoProtectora() {
        return telefonoProtectora;
    }

    public String getCorreoProtectora() {
        return correoProtectora;
    }

    public void setNombreProtectora(String nombreProtectora) {
        this.nombreProtectora = nombreProtectora;
    }

    public void setDireccionProtectora(String direccionProtectora) {
        this.direccionProtectora = direccionProtectora;
    }

    public void setLocalidadProtectora(String localidadProtectora) {
        this.localidadProtectora = localidadProtectora;
    }

    public void setTelefonoProtectora(int telefonoProtectora) {
        this.telefonoProtectora = telefonoProtectora;
    }

    public void setCorreoProtectora(String correoProtectora) {
        this.correoProtectora = correoProtectora;
    }

    @Override
    public String toString() {
        return "Protectora{" +
                "idProtectora=" + idProtectora +
                ", nombreProtectora='" + nombreProtectora + '\'' +
                ", direccionProtectora='" + direccionProtectora + '\'' +
                ", localidadProtectora='" + localidadProtectora + '\'' +
                ", telefonoProtectora=" + telefonoProtectora +
                ", correoProtectora='" + correoProtectora + '\'' +
                '}';
    }
}
