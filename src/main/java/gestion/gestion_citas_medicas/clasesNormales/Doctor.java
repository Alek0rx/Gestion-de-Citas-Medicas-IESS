package gestion.gestion_citas_medicas.clasesNormales;

public class Doctor extends Persona {
    private int idDoctor;
    private String especialidad;
    private String consultorio;

    // -- Constructor --
    public Doctor() {
        super();
    }

    public Doctor(String nombre, String apellido, String cedula, String telefono, String correo, int idDoctor, String consultorio, String especialidad) {
        super(nombre, apellido, cedula, telefono, correo);
        this.idDoctor = idDoctor;
        this.consultorio = consultorio;
        this.especialidad = especialidad;
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

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }
}
