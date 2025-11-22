package gestion.gestion_citas_medicas.ClasesNormales;

import java.time.LocalDate;

public class Cita_Medica {
    private int idCita;
    private int idDoctor;
    private int idPaciente;
    private int idHorario;
    private int idTipo;
    private String estado;
    private LocalDate fechaCita;

    // -- Constructor
    public Cita_Medica() {}

    public Cita_Medica(int idCita, LocalDate fechaCita, String estado, int idTipo, int idDoctor,
                       int idPaciente, int idHorario) {
        this.idCita = idCita;
        this.fechaCita = fechaCita;
        this.estado = estado;
        this.idTipo = idTipo;
        this.idDoctor = idDoctor;
        this.idPaciente = idPaciente;
        this.idHorario = idHorario;
    }

    // -- Getters y Setters

    public int getIdCita() {
        return idCita;
    }

    public void setIdCita(int idCita) {
        this.idCita = idCita;
    }

    public LocalDate getFechaCita() {
        return fechaCita;
    }

    public void setFechaCita(LocalDate fechaCita) {
        this.fechaCita = fechaCita;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(int idTipo) {
        this.idTipo = idTipo;
    }

    public int getIdHorario() {
        return idHorario;
    }

    public void setIdHorario(int idHorario) {
        this.idHorario = idHorario;
    }

    public int getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(int idPaciente) {
        this.idPaciente = idPaciente;
    }

    public int getIdDoctor() {
        return idDoctor;
    }

    public void setIdDoctor(int idDoctor) {
        this.idDoctor = idDoctor;
    }
}
