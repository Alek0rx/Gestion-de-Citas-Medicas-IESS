package gestion.gestion_citas_medicas.ClasesNormales;

import java.time.LocalDate;

public class Medicamento {
    private int idMedicamento;
    private String nombre;
    private String descripcion;
    private LocalDate fechaCreacion;
    private LocalDate fechaExpiracion;

    // -- Constructor --

    public Medicamento() {}

    public Medicamento(int idMedicamento, LocalDate fechaExpiracion, LocalDate fechaCreacion,
                       String descripcion, String nombre) {
        this.idMedicamento = idMedicamento;
        this.fechaExpiracion = fechaExpiracion;
        this.fechaCreacion = fechaCreacion;
        this.descripcion = descripcion;
        this.nombre = nombre;
    }
    public Medicamento(int idMedicamento, String nombre){
        this.idMedicamento = idMedicamento;
        this.nombre = nombre;
    }

    // -- Getters y Setters

    public int getIdMedicamento() {
        return idMedicamento;
    }

    public void setIdMedicamento(int idMedicamento) {
        this.idMedicamento = idMedicamento;
    }

    public LocalDate getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(LocalDate fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

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
    @Override
    public String toString() {
        return this.nombre;
    }
}
