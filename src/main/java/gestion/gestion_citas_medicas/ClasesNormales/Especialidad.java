package gestion.gestion_citas_medicas.ClasesNormales;

public class Especialidad {
    private int idTipo;
    private String nombre;
    private String descripcion;
    private int idEspecialidad;
    private String nombreEspecialidad;

    // -- Constructor --

    public Especialidad() {}

    public Especialidad(int idTipo, String descripcion, String nombre) {
        this.idTipo = idTipo;
        this.descripcion = descripcion;
        this.nombre = nombre;
    }

    public Especialidad(int idEspecialidad, String nombreEspecialidad) {
        this.idEspecialidad = idEspecialidad;
        this.nombreEspecialidad = nombreEspecialidad;
    }

    // -- Setters y Getters

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(int idTipo) {
        this.idTipo = idTipo;
    }

    public String getNombreEspecialidad() {
        return nombreEspecialidad;
    }

    public void setNombreEspecialidad(String nombreEspecialidad) {
        this.nombreEspecialidad = nombreEspecialidad;
    }
}
