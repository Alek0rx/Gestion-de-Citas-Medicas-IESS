package gestion.gestion_citas_medicas.Logica;

import gestion.gestion_citas_medicas.ClasesNormales.*;
import gestion.gestion_citas_medicas.ClasesSQL.Cita_MedicaSQL;
import gestion.gestion_citas_medicas.ClasesSQL.HorarioSQL;
import gestion.gestion_citas_medicas.ClasesSQL.PacienteSQL;
import gestion.gestion_citas_medicas.Controladores.CVerCitasDoctor;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class VerCitas {
    public String generarMensajeDetalles(Cita_Medica citaSeleccionada) throws Exception {

        // ===== PACIENTE =====
        PacienteSQL pacienteSQL = new PacienteSQL();
        Paciente paciente = pacienteSQL.findById(citaSeleccionada.getIdPaciente());

        String nombrePaciente = (paciente != null)
                ? paciente.getNombre() + " " + paciente.getApellido()
                : "Desconocido";

        // ===== HORARIO =====
        HorarioSQL horarioSQL = new HorarioSQL();
        Horario horario = horarioSQL.findById(citaSeleccionada.getIdHorario());

        String hora = (horario != null)
                ? horario.getHoraInicio() + " - " + horario.getHoraFin()
                : "No disponible";

        // ===== ARMAR MENSAJE =====
        StringBuilder mensaje = new StringBuilder();
        mensaje.append("Paciente: ").append(nombrePaciente).append("\n");
        mensaje.append("Fecha: ").append(citaSeleccionada.getFechaCita()).append("\n");
        mensaje.append("Hora: ").append(hora).append("\n");
        mensaje.append("Estado: ").append(citaSeleccionada.getEstado()).append("\n");

        return mensaje.toString();
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(
                null,
                mensaje,
                "Detalles de la cita",
                JOptionPane.INFORMATION_MESSAGE
        );
    }



}
