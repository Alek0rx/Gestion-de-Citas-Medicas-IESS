package gestion.gestion_citas_medicas.ClasesNormales;

import java.time.LocalDate;

public class DetalleTratamiento {
    private String descripcionTratamiento;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String indicacionesReceta;
    private String nombreMedicamento; // Nombre del medicamento (si existe)

    public DetalleTratamiento() {
    }

    public DetalleTratamiento(String diagnosticoHistorial, String descripcionTratamiento, LocalDate fechaInicio, LocalDate fechaFin, String indicacionesReceta, String nombreMedicamento) {
        this.descripcionTratamiento = descripcionTratamiento;
        this.nombreMedicamento = nombreMedicamento;
        this.indicacionesReceta = indicacionesReceta;
        this.fechaFin = fechaFin;
        this.fechaInicio = fechaInicio;
    }

    public String getDescripcionTratamiento() {
        return descripcionTratamiento;
    }

    public void setDescripcionTratamiento(String descripcionTratamiento) {
        this.descripcionTratamiento = descripcionTratamiento;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getIndicacionesReceta() {
        return indicacionesReceta;
    }

    public void setIndicacionesReceta(String indicacionesReceta) {
        this.indicacionesReceta = indicacionesReceta;
    }

    public String getNombreMedicamento() {
        return nombreMedicamento;
    }

    public void setNombreMedicamento(String nombreMedicamento) {
        this.nombreMedicamento = nombreMedicamento;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Tratamiento:\n");
        sb.append("   • Descripción: ").append(descripcionTratamiento != null ? descripcionTratamiento : "No especificada").append("\n");
        sb.append("   • Medicamento: ").append(nombreMedicamento != null ? nombreMedicamento : "Ninguno").append("\n");
        sb.append("   • Indicaciones: ").append(indicacionesReceta != null ? indicacionesReceta : "Sin indicaciones").append("\n");
        sb.append("   • Desde: ").append(fechaInicio != null ? fechaInicio.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "No definida").append("\n");
        sb.append("   • Hasta: ").append(fechaFin != null ? fechaFin.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "Indefinida");

        return sb.toString();
    }
}