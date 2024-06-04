package es.studium.losamigosdeviky.veterinarios;

public class Veterinario {
    private int idVeterinario;
    private String nombreVeterinario;
    private String apellidosVeterinario;
    private int telefonoVeterinario;
    private String especialidadVeterinario;

    public Veterinario(String nombreVeterinario, String apellidosVeterinario, int telefonoVeterinario, String especialidadVeterinario) {
        this.nombreVeterinario = nombreVeterinario;
        this.apellidosVeterinario = apellidosVeterinario;
        this.telefonoVeterinario = telefonoVeterinario;
        this.especialidadVeterinario = especialidadVeterinario;
    }

    public Veterinario(int idVeterinario, String nombreVeterinario, String apellidosVeterinario, int telefonoVeterinario, String especialidadVeterinario) {
        this.idVeterinario = idVeterinario;
        this.nombreVeterinario = nombreVeterinario;
        this.apellidosVeterinario = apellidosVeterinario;
        this.telefonoVeterinario = telefonoVeterinario;
        this.especialidadVeterinario = especialidadVeterinario;
    }

    public int getIdVeterinario() {
        return idVeterinario;
    }

    public String getNombreVeterinario() {
        return nombreVeterinario;
    }

    public String getApellidosVeterinario() {
        return apellidosVeterinario;
    }

    public int getTelefonoVeterinario() {
        return telefonoVeterinario;
    }

    public String getEspecialidadVeterinario() {
        return especialidadVeterinario;
    }

    public void setNombreVeterinario(String nombreVeterinario) {
        this.nombreVeterinario = nombreVeterinario;
    }

    public void setApellidosVeterinario(String apellidosVeterinario) {
        this.apellidosVeterinario = apellidosVeterinario;
    }

    public void setTelefonoVeterinario(int telefonoVeterinario) {
        this.telefonoVeterinario = telefonoVeterinario;
    }

    public void setEspecialidadVeterinario(String especialidadVeterinario) {
        this.especialidadVeterinario = especialidadVeterinario;
    }

    @Override
    public String toString() {
        return "Veterinario{" +
                "idVeterinario=" + idVeterinario +
                ", nombreVeterinario='" + nombreVeterinario + '\'' +
                ", apellidosVeterinario='" + apellidosVeterinario + '\'' +
                ", telefonoVeterinario=" + telefonoVeterinario +
                ", especialidadVeterinario='" + especialidadVeterinario + '\'' +
                '}';
    }
}
