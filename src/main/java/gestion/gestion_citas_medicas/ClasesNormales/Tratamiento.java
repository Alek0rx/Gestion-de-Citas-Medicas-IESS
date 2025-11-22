package gestion.gestion_citas_medicas.ClasesNormales;

import java.time.LocalDate;

public class Tratamiento {
    private int idTratamiento;
    private int idCita;
    private String descripcion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    // -- Constructor --
    public Tratamiento() {}

    public Tratamiento(int idTratamiento, LocalDate fechaFin,
                       LocalDate fechaInicio, String descripcion, int idCita) {
        this.idTratamiento = idTratamiento;
        this.fechaFin = fechaFin;
        this.fechaInicio = fechaInicio;
        this.descripcion = descripcion;
        this.idCita = idCita;
    }
    // -- Getters y Setters --

    public int getIdTratamiento() {
        return idTratamiento;
    }

    public void setIdTratamiento(int idTratamiento) {
        this.idTratamiento = idTratamiento;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getIdCita() {
        return idCita;
    }

    public void setIdCita(int idCita) {
        this.idCita = idCita;
    }
}
