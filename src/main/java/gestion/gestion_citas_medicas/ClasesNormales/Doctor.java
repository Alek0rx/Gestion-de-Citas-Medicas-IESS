package gestion.gestion_citas_medicas.ClasesNormales;

public class Doctor extends Persona {
    private int idDoctor;
    private int idUsuario;
    private int idEspecialidad;
    private String estado;
    private String consultorio;

    // -- Constructor --
    public Doctor() {
        super();
    }

    public Doctor(String nombre, String apellido, String cedula, String telefono, String correo, String direccion, int idDoctor, int idUsuario,
                  int idEspecialidad, String estado,String consultorio) {
        super(nombre, apellido, cedula, telefono, correo, direccion);
        this.idDoctor = idDoctor;
        this.idUsuario = idUsuario;
        this.estado = estado;
        this.consultorio = consultorio;
        this.idEspecialidad = idEspecialidad;
    }

    // -- Getters y Setters
    public String getConsultorio() {
        return consultorio;
    }

    public void setConsultorio(String consultorio) {
        this.consultorio = consultorio;
    }

    public int getIdDoctor() {
        return idDoctor;
    }

    public void setIdDoctor(int idDoctor) {
        this.idDoctor = idDoctor;
    }

    public int getIdEspecialidad() {
        return idEspecialidad;
    }

    public void setIdEspecialidad(int idEspecialidad) {
        this.idEspecialidad = idEspecialidad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Override
    public String toString() {
        return super.toString() + estado + consultorio;
    }
}