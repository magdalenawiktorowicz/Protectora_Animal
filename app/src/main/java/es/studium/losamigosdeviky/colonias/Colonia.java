package es.studium.losamigosdeviky.colonias;

public class Colonia {
    private int idColonia;
    private String nombreColonia;
    private int cpColonia;
    private String latitudColonia;
    private String longitudColonia;
    private String direccionColonia;
    private int idAyuntamientoFK1;
    private int idProtectoraFK2;

    public Colonia(String nombreColonia, int cpColonia, String latitudColonia, String longitudColonia, String direccionColonia, int idAyuntamientoFK1, int idProtectoraFK2) {
        this.nombreColonia = nombreColonia;
        this.cpColonia = cpColonia;
        this.latitudColonia = latitudColonia;
        this.longitudColonia = longitudColonia;
        this.direccionColonia = direccionColonia;
        this.idAyuntamientoFK1 = idAyuntamientoFK1;
        this.idProtectoraFK2 = idProtectoraFK2;
    }

    public Colonia(int idColonia, String nombreColonia, int cpColonia, String latitudColonia, String longitudColonia, String direccionColonia, int idAyuntamientoFK1, int idProtectoraFK2) {
        this.idColonia = idColonia;
        this.nombreColonia = nombreColonia;
        this.cpColonia = cpColonia;
        this.latitudColonia = latitudColonia;
        this.longitudColonia = longitudColonia;
        this.direccionColonia = direccionColonia;
        this.idAyuntamientoFK1 = idAyuntamientoFK1;
        this.idProtectoraFK2 = idProtectoraFK2;
    }

    public int getIdColonia() {
        return idColonia;
    }

    public String getNombreColonia() {
        return nombreColonia;
    }

    public int getCpColonia() {
        return cpColonia;
    }

    public String getLatitudColonia() {
        return latitudColonia;
    }

    public String getLongitudColonia() {
        return longitudColonia;
    }

    public String getDireccionColonia() {
        return direccionColonia;
    }

    public int getIdAyuntamientoFK1() {
        return idAyuntamientoFK1;
    }

    public int getIdProtectoraFK2() {
        return idProtectoraFK2;
    }

    public void setNombreColonia(String nombreColonia) {
        this.nombreColonia = nombreColonia;
    }

    public void setCpColonia(int cpColonia) {
        this.cpColonia = cpColonia;
    }

    public void setLatitudColonia(String latitudColonia) {
        this.latitudColonia = latitudColonia;
    }

    public void setLongitudColonia(String longitudColonia) {
        this.longitudColonia = longitudColonia;
    }

    public void setDireccionColonia(String direccionColonia) {
        this.direccionColonia = direccionColonia;
    }

    public void setIdAyuntamientoFK1(int idAyuntamientoFK1) {
        this.idAyuntamientoFK1 = idAyuntamientoFK1;
    }

    public void setIdProtectoraFK2(int idProtectoraFK2) {
        this.idProtectoraFK2 = idProtectoraFK2;
    }

    @Override
    public String toString() {
        return "Colonia{" +
                "idColonia=" + idColonia +
                ", nombreColonia='" + nombreColonia + '\'' +
                ", cpColonia=" + cpColonia +
                ", latitudColonia='" + latitudColonia + '\'' +
                ", longitudColonia='" + longitudColonia + '\'' +
                ", direccionColonia='" + direccionColonia + '\'' +
                ", idAyuntamientoFK1=" + idAyuntamientoFK1 +
                ", idProtectoraFK2=" + idProtectoraFK2 +
                '}';
    }
}
