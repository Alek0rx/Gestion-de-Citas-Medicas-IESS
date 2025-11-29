package gestion.gestion_citas_medicas.ClasesNormales;

import gestion.gestion_citas_medicas.ClasesSQL.HorarioSQL;

import java.time.LocalDate;

public class Cita_Medica {
    private int idCita;
    private int idDoctor;
    private int idPaciente;
    private int idHorario;
    private int idTipo;
    private String estado;
    private LocalDate fechaCita;

    // solo para efectos de interfaz, no se relaciona con la base de datos:
    // ------------------------------------------
    private String nombreDoctor;
    private String apellidoDoctor;
    private String especialidadNombre;
    private String  horaRango;
    private String nombrePaciente;
    private String apellidoPaciente;
    // --------------------------------------------


    // -- Constructor
    public Cita_Medica() {
    }

    public Cita_Medica(LocalDate fechaCita, String estado, int idTipo, int idDoctor,
                       int idPaciente, int idHorario) {
        this.fechaCita = fechaCita;
        this.estado = estado;
        this.idTipo = idTipo;
        this.idDoctor = idDoctor;
        this.idPaciente = idPaciente;
        this.idHorario = idHorario;
    }


    public Cita_Medica(LocalDate fechaCita, String estado, int idTipo, int idPaciente, int idHorario) {
        this.fechaCita = fechaCita;
        this.estado = estado;
        this.idTipo = idTipo;
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



    public String getNombreDoctor() {
        return nombreDoctor;
    }

    public String getEspecialidadNombre() {
        return especialidadNombre;
    }

    public void setEspecialidadNombre(String especialidadNombre) {
        this.especialidadNombre = especialidadNombre;
    }

    public void setNombreDoctor(String nombreDoctor) {
        this.nombreDoctor = nombreDoctor;
    }

    public String getHoraRango() {
        return horaRango;
    }

    public void setHoraRango(String horaRango) {
        this.horaRango = horaRango;
    }

    public String getNombrePaciente() {
        return nombrePaciente;
    }

    public void setNombrePaciente(String nombrePaciente) {
        this.nombrePaciente = nombrePaciente;
    }

    public String getApellidoPaciente() {
        return apellidoPaciente;
    }

    public void setApellidoPaciente(String apellidoPaciente) {
        this.apellidoPaciente = apellidoPaciente;
    }

    public String getApellidoDoctor() {
        return apellidoDoctor;
    }

    public void setApellidoDoctor(String apellidoDoctor) {
        this.apellidoDoctor = apellidoDoctor;
    }

    @Override
    public String toString() {
        return String.format(
                "idCita: %s\nidPaciente: %s\nFecha cita: %s\nHora Cita: %s\nEspecialidad: %s\nEstado: %s\nDoctor: %s\n",
                idCita, idPaciente, fechaCita.toString(), horaRango,especialidadNombre,estado, nombreDoctor
        );
    }

    public void setDoctorNombre(String nombreDoctor) {
        this.nombreDoctor = nombreDoctor;
    }
}
