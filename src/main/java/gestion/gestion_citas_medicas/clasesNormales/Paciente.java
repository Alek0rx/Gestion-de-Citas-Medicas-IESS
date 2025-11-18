package gestion.gestion_citas_medicas.clasesNormales;

import java.time.LocalDate;

public class Paciente extends Persona{
    private int idPaciente;
    private LocalDate fechaNacimiento;

    // -- Constructor --
    public Paciente() {
        super();
    }

    public Paciente(String nombre, String apellido, String cedula, String telefono, String correo,
                    int idPaciente, LocalDate fechaNacimiento) {
        super(nombre, apellido, cedula, telefono, correo);
        this.idPaciente = idPaciente;
        this.fechaNacimiento = fechaNacimiento;
    }

    // -- Getters y Setters
    public Paciente(int idPaciente) {
        this.idPaciente = idPaciente;
    }

    public int getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(int idPaciente) {
        this.idPaciente = idPaciente;
    }

    public LocalDate getFechaNacimientoPaciente() {
        return fechaNacimiento;
    }

    public void setFechaNacimientoPaciente(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }
}
