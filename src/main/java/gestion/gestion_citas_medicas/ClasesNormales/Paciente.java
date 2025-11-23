package gestion.gestion_citas_medicas.ClasesNormales;

import java.time.LocalDate;

public class Paciente extends Persona{
    private int idPaciente;
    private int idUsuario;
    private char genero;
    private LocalDate fechaNacimiento;

    // -- Constructor --
    public Paciente() {
        super();
    }

    public Paciente(String nombre, String apellido, String cedula, String telefono, String correo,
                    String direccion, int idPaciente, int idUsuario, char genero, LocalDate fechaNacimiento) {
        super(nombre, apellido, cedula, telefono, correo, direccion);
        this.idPaciente = idPaciente;
        this.idUsuario = idUsuario;
        this.genero = genero;
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

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public char getGenero() {
        return genero;
    }

    public void setGenero(char genero) {
        this.genero = genero;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
