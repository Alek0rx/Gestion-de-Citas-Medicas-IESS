package gestion.gestion_citas_medicas.ClasesNormales;

public class Usuario {
    private int idUsuario;
    private String usuario;
    private String password;
    private String rol;
    private String estado;
    private String especialidad;

    // ----- SOLO POR CUESTIONES DE INTERFAZ ------
    private String nombreUsuario;

    // -- Constructor --
    public Usuario() {}

    public Usuario(int idUsuario, String estado, String rol, String password, String usuario) {
        this.idUsuario = idUsuario;
        this.estado = estado;
        this.rol = rol;
        this.password = password;
        this.usuario = usuario;
    }

    // -- Getters y Setters --
    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getEspecialidad() {
        return especialidad != null ? especialidad : "";
    }
}
