package gestion.gestion_citas_medicas.clasesNormales;

public class Especialidad {
    private int idTipo;
    private String nombre;
    private String descripcion;

    // -- Constructor --

    public Especialidad() {}

    public Especialidad(int idTipo, String descripcion, String nombre) {
        this.idTipo = idTipo;
        this.descripcion = descripcion;
        this.nombre = nombre;
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
}
