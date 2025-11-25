package gestion.gestion_citas_medicas.Logica;

import gestion.gestion_citas_medicas.ClasesNormales.*;
import gestion.gestion_citas_medicas.ClasesSQL.DoctorSQL;
import gestion.gestion_citas_medicas.ClasesSQL.HorarioSQL; // NECESARIO
import gestion.gestion_citas_medicas.ClasesSQL.Cita_MedicaSQL; // NECESARIO
import gestion.gestion_citas_medicas.ClasesSQL.OperadorSQL;
import gestion.gestion_citas_medicas.ClasesSQL.PacienteSQL;
import gestion.gestion_citas_medicas.ClasesSQL.UsuarioSQL;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class OperadorService {
    private UsuarioSQL userDAO;
    private PacienteSQL pacienteDAO;
    private DoctorSQL doctorDAO;
    private OperadorSQL operadorDAO;

    // DAOs para Citas
    private Cita_MedicaSQL citaMedicaDAO;
    private HorarioSQL horarioDAO;

    // Mapa de Horarios centralizado en el servicio
    private Map<Integer, String> mapaHorarios = new HashMap<>();


    public OperadorService() {
        this.userDAO = new UsuarioSQL();
        this.pacienteDAO = new PacienteSQL();
        this.doctorDAO = new DoctorSQL();
        this.operadorDAO = new OperadorSQL();


        this.citaMedicaDAO = new Cita_MedicaSQL();
        this.horarioDAO = new HorarioSQL();
    }


    public void cargarMapaHorarios() throws Exception {
        mapaHorarios = ElementosEstaticos.horarios;
    }


    public Map<Integer, String> getMapaHorarios() {
        return mapaHorarios;
    }

    public List<Cita_Medica> obtenerCitasPendientes(String especialidad) throws Exception {
        if (especialidad == null || especialidad.isEmpty() || especialidad.equals("Todas")) {
            // Llama al método DAO sin filtro
            return citaMedicaDAO.findAllPendientes();
        } else {
            // Llama al método DAO con filtro
            return citaMedicaDAO.findPendientesByEspecialidad(especialidad);
        }
    }

    public void actualizarEstadoUsuario(int idUsuario, String nuevoEstado) throws Exception {
        userDAO.actualizarEstado(idUsuario, nuevoEstado);
    }

    public List<Usuario> obtenerUsuariosParaTabla() throws Exception {
        List<Usuario> usuarios = userDAO.obtenerUsuariosSinAdmin();

        for (Usuario u : usuarios) {
            String nombreReal = "N/A";

            switch (u.getRol().toLowerCase()) {
                case "paciente":
                    nombreReal = pacienteDAO.obtenerNombreCompleto(u.getIdUsuario());
                    break;
                case "doctor":
                    nombreReal = doctorDAO.obtenerNombreCompleto(u.getIdUsuario());
                    String nombreEspecialidad = doctorDAO.obtenerEspecialidadPorIdUsuario(u.getIdUsuario());
                    u.setEspecialidad(nombreEspecialidad);
                    break;
                case "operador":
                    nombreReal = operadorDAO.obtenerNombreCompleto(u.getIdUsuario());
                    break;
            }
            u.setNombreUsuario(nombreReal);
        }
        return usuarios;
    }

    public ObservableList<String> obtenerEspecialidades() {

        List<String> nombresEspecialidades = new ArrayList<>(
                ElementosEstaticos.especialidad.values()
        );

        nombresEspecialidades.add(0, "Todos");
        return FXCollections.observableArrayList(nombresEspecialidades);
    }
    public List<Doctor> obtenerDoctoresPorEspecialidad(String especialidad) throws Exception {
        return doctorDAO.findByEspecialidad(especialidad);
    }


    public void asignarDoctorACita(int idCita, int idDoctor) throws Exception {

        citaMedicaDAO.asignarDoctor(idCita, idDoctor);
    }
}