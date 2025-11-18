package gestion.gestion_citas_medicas.clasesNormales;

import java.time.LocalDate;

public class Medicamento {
    private int idMedicamento;
    private String nombre;
    private String despcripcion;
    private LocalDate fechaCreacion;
    private LocalDate fechaExpiracion;

    // -- Constructor --

    public Medicamento() {}

    public Medicamento(int idMedicamento, LocalDate fechaExpiracion, LocalDate fechaCreacion,
                       String despcripcion, String nombre) {
        this.idMedicamento = idMedicamento;
        this.fechaExpiracion = fechaExpiracion;
        this.fechaCreacion = fechaCreacion;
        this.despcripcion = despcripcion;
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

    public String getDespcripcion() {
        return despcripcion;
    }

    public void setDespcripcion(String despcripcion) {
        this.despcripcion = despcripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
