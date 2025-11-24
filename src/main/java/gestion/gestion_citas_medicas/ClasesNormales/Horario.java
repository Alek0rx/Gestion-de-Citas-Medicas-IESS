package gestion.gestion_citas_medicas.ClasesNormales;

import java.time.LocalTime;

public class Horario {
    private int idHorario;
    private LocalTime horaInicio;
    private LocalTime horaFin;

    // -- Constructor --
    public Horario() {}

    public Horario(int idHorario, LocalTime horaFin, LocalTime horaInicio) {
        this.idHorario = idHorario;
        this.horaFin = horaFin;
        this.horaInicio = horaInicio;
    }

    // -- Getters y Setters --

    public int getIdHorario() {
        return idHorario;
    }

    public void setIdHorario(int idHorario) {
        this.idHorario = idHorario;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }
}
