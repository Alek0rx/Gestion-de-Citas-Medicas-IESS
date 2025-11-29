package gestion.gestion_citas_medicas.Logica;

import gestion.gestion_citas_medicas.ClasesNormales.*;
import gestion.gestion_citas_medicas.ClasesSQL.Cita_MedicaSQL;
import gestion.gestion_citas_medicas.ClasesSQL.HorarioSQL;
import gestion.gestion_citas_medicas.ClasesSQL.PacienteSQL;
// No necesitamos CVerCitasDoctor aquí

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class VerCitas {


    // Instancias de las clases SQL (acceso a datos)
    private final Cita_MedicaSQL citaSQL = new Cita_MedicaSQL();
    private final PacienteSQL pacienteSQL = new PacienteSQL();
    private final HorarioSQL horarioSQL = new HorarioSQL(); // Aunque se sugiere usar ElementosEstaticos

    /**
     * @return Una lista de todas las citas del doctor actualmente logueado.
     */
    public List<Cita_Medica> obtenerCitasDoctor() throws Exception {
        Doctor doctorLogueado = SessionManager.getPerfil(Doctor.class);
        if (doctorLogueado == null) {
            return new ArrayList<>();
        }
        int idDoctor = doctorLogueado.getIdDoctor();
        return citaSQL.findByDoctor(idDoctor);
    }

    /**
     * Lógica para obtener el nombre del paciente a partir de la cita.
     * @return Nombre completo del paciente o "N/A".
     */
    public String obtenerNombrePaciente(int idPaciente) {
        try {
            Paciente p = pacienteSQL.findById(idPaciente);
            return (p != null) ? p.getNombre() + " " + p.getApellido() : "N/A";
        } catch (Exception e) {
            e.printStackTrace();
            return "N/A (Error DB)";
        }
    }

    /**
     * Actualiza el estado de la cita en la base de datos.
     */
    public void actualizarEstadoCita(int idCita, String nuevoEstado) throws Exception {
        citaSQL.actualizarEstado(idCita, nuevoEstado);
    }

    // El resto de tus métodos de detalles (generarMensajeDetalles, mostrarMensaje) se mantienen aquí.

    public String generarMensajeDetalles(Cita_Medica citaSeleccionada) throws Exception {
        // Ahora usamos el método obtenerNombrePaciente de esta clase:
        String nombrePaciente = obtenerNombrePaciente(citaSeleccionada.getIdPaciente());

        // Para el horario, es mejor seguir usando ElementosEstaticos si el controlador lo hace:
        String hora = ElementosEstaticos.getHorarioPorId(citaSeleccionada.getIdHorario());
        hora = (hora != null) ? hora : "No disponible";

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