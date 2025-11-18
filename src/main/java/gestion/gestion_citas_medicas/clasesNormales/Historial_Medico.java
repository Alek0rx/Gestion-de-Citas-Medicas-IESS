package gestion.gestion_citas_medicas.clasesNormales;

import java.time.LocalDate;

public class Historial_Medico {
    private int idHistorial;
    private int idCita;
    private String diagnostico;
    private String signosVitales;
    private LocalDate fechaRegistro;

    // -- Constructor --
    public Historial_Medico() {}

    public Historial_Medico(int idHistorial, LocalDate fechaRegistro, String signosVitales, int idCita, String diagnostico) {
        this.idHistorial = idHistorial;
        this.fechaRegistro = fechaRegistro;
        this.signosVitales = signosVitales;
        this.idCita = idCita;
        this.diagnostico = diagnostico;
    }

    // -- Getters y Setters --
    public int getIdHistorial() {
        return idHistorial;
    }

    public void setIdHistorial(int idHistorial) {
        this.idHistorial = idHistorial;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getSignosVitales() {
        return signosVitales;
    }

    public void setSignosVitales(String signosVitales) {
        this.signosVitales = signosVitales;
    }

    public int getIdCita() {
        return idCita;
    }

    public void setIdCita(int idCita) {
        this.idCita = idCita;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }
}

