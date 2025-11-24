package gestion.gestion_citas_medicas.Logica;

import gestion.gestion_citas_medicas.ClasesNormales.Cita_Medica;
import gestion.gestion_citas_medicas.ClasesNormales.ElementosEstaticos;
import gestion.gestion_citas_medicas.ClasesNormales.Paciente;
import gestion.gestion_citas_medicas.ClasesNormales.SessionManager;
import gestion.gestion_citas_medicas.ClasesSQL.Cita_MedicaSQL;
import gestion.gestion_citas_medicas.ClasesSQL.DoctorSQL;
import gestion.gestion_citas_medicas.ClasesSQL.HorarioSQL;

import java.time.LocalDate;
import java.util.*;

public class CitaService {
    private Cita_MedicaSQL citaMedicaDAO;
    private DoctorSQL doctorDAO;
    private HorarioSQL horarioDAO;

    public CitaService() {
        this.citaMedicaDAO = new Cita_MedicaSQL();
        this.horarioDAO = new HorarioSQL();
        this.doctorDAO = new DoctorSQL();
    }


    public void generarCita(String especialidad, LocalDate fecha, String horario) throws Exception {
        if (especialidad.isEmpty() || horario.isEmpty() || fecha == null) {
            throw new IllegalArgumentException("Los campos no pueden estar vacíos");
        }
        Paciente p = SessionManager.getPerfil(Paciente.class);

        try {
            int idEspecialidad = ElementosEstaticos.getIdEspecialidad(especialidad);
            int idHorario = ElementosEstaticos.getIdHorario(horario);

            Cita_Medica nuevaCita = new Cita_Medica(
                    fecha, "pendiente", idEspecialidad, p.getIdPaciente(),
                    idHorario
            );
            citaMedicaDAO.insert(nuevaCita);
            System.out.println("Se generó la cita exitosamente");
        } catch (
                RuntimeException e) {
            throw new Exception("Error al generar el Cita: " + e.getMessage());
        }

    }

    // ------------- SOBRECARGA DE FUNCIONES ------------------------

    // PARA LA REPROGRAMACION
    public List<String> obtenerHorariosDisponibles(LocalDate fecha, String especialidad, int idCitaExcluir) {
        try {
            Paciente p = SessionManager.getPerfil(Paciente.class);
            if (p == null) {
                throw new Exception("Sesión de paciente no encontrada.");
            }
            int idPaciente = p.getIdPaciente();
            int idEspecialidad = ElementosEstaticos.getIdEspecialidad(especialidad);
            List<Integer> horariosOcupados = citaMedicaDAO.encontrarCitas(idEspecialidad, fecha);
            List<Integer> horariosOcupadosPaciente = citaMedicaDAO.encontrarHorariosOcupadosPorPaciente(idPaciente, fecha, idCitaExcluir);

            List<String> horariosDisponible = new ArrayList<>();

            Set<Integer> horariosTotalesOcupados = new HashSet<>();
            horariosTotalesOcupados.addAll(horariosOcupados);
            horariosTotalesOcupados.addAll(horariosOcupadosPaciente);

            for (Map.Entry<Integer, String> horarioEntry : ElementosEstaticos.horarios.entrySet()) {
                Integer idHorario = horarioEntry.getKey();

                // SOLO añadir si el horario NO está en el conjunto de TOTALES ocupados
                if (!horariosTotalesOcupados.contains(idHorario)) {
                    horariosDisponible.add(horarioEntry.getValue());
                }
            }
            return horariosDisponible;
        }

        catch (Exception e) {
            throw new RuntimeException("Error al cargar el Horario: " + e.getMessage(), e);
        }
    }
    // PARA EL AGENDAMIENTO
    public List<String> obtenerHorariosDisponibles(LocalDate fecha, String especialidad) {
        try {
            Paciente p = SessionManager.getPerfil(Paciente.class);
            if (p == null) {
                throw new Exception("Sesión de paciente no encontrada.");
            }
            int idPaciente = p.getIdPaciente();
            int idEspecialidad = ElementosEstaticos.getIdEspecialidad(especialidad);
            List<Integer> horariosOcupados = citaMedicaDAO.encontrarCitas(idEspecialidad, fecha);
            List<Integer> horariosOcupadosPaciente = citaMedicaDAO.encontrarHorariosOcupadosPorPaciente(idPaciente, fecha);

            List<String> horariosDisponible = new ArrayList<>();

            Set<Integer> horariosTotalesOcupados = new HashSet<>();
            horariosTotalesOcupados.addAll(horariosOcupados);
            horariosTotalesOcupados.addAll(horariosOcupadosPaciente);

            for (Map.Entry<Integer, String> horarioEntry : ElementosEstaticos.horarios.entrySet()) {
                Integer idHorario = horarioEntry.getKey();

                // SOLO añadir si el horario NO está en el conjunto de TOTALES ocupados
                if (!horariosTotalesOcupados.contains(idHorario)) {
                    horariosDisponible.add(horarioEntry.getValue());
                }
            }
            return horariosDisponible;
        }

        catch (Exception e) {
            throw new RuntimeException("Error al cargar el Horario: " + e.getMessage(), e);
        }
    }




    public List<Cita_Medica> obtenerCitasParaTabla(int idPaciente) throws Exception {
        try {
            List<Cita_Medica> citasCrudas = citaMedicaDAO.encontrarCitasPorIdPaciente(idPaciente);

            Map<Integer, String> mapaNombresDoctor = doctorDAO.obtenerMapaNombresDoctores();

            for (Cita_Medica cita : citasCrudas) {

                int idEspecialidad = cita.getIdTipo();
                String nombreEspecialidad = ElementosEstaticos.getEspecialidadPorId(idEspecialidad);
                cita.setEspecialidadNombre(nombreEspecialidad);

                int idHorario = cita.getIdHorario();
                String rangoHorario = ElementosEstaticos.getHorarioPorId(idHorario);
                cita.setHoraRango(rangoHorario);


                Integer idDoctor = cita.getIdDoctor();
                String nombreDoctor = "No Asignado";

                if (idDoctor != null && idDoctor > 0 && mapaNombresDoctor.containsKey(idDoctor)) {
                    nombreDoctor = mapaNombresDoctor.get(idDoctor);
                }

                cita.setDoctorNombre(nombreDoctor);
            }
            return citasCrudas;
        } catch (
                Exception e) {
            throw new Exception("Error al cargar la Cita: " + e.getMessage(), e);
        }
    }

    public void cancelarCita(int idCita) {
        try {
            citaMedicaDAO.cancelarCitaMedica(idCita);
        } catch (
                Exception e) {
            throw new RuntimeException("Error al cancelar la cita: " + e.getMessage(), e);
        }
    }

    public void reprogramarCita(int idCita, LocalDate nuevaFecha, String nuevoHorarioRango) throws Exception {
        if (nuevaFecha == null || nuevoHorarioRango == null || nuevoHorarioRango.isEmpty()) {
            throw new IllegalArgumentException("Debe seleccionar una fecha y un horario válidos.");
        }

        try {
            int nuevoIdHorario = ElementosEstaticos.getIdHorario(nuevoHorarioRango);
            System.out.println("nuevoIdHorario: " + nuevoIdHorario);
            System.out.println("nuevaFecha: " + nuevaFecha);
            System.out.println("idCita: " + idCita);

            citaMedicaDAO.actualizarCita(idCita, nuevaFecha, nuevoIdHorario);

        } catch (
                Exception e) {
            throw new Exception("Error en el servicio al reprogramar la cita: " + e.getMessage(), e);
        }

    }
}