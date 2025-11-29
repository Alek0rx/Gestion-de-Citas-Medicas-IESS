package gestion.gestion_citas_medicas.Logica;

import gestion.gestion_citas_medicas.ClasesNormales.*;
import gestion.gestion_citas_medicas.ClasesSQL.DoctorSQL;
import gestion.gestion_citas_medicas.ClasesSQL.OperadorSQL;
import gestion.gestion_citas_medicas.ClasesSQL.PacienteSQL;
import gestion.gestion_citas_medicas.ClasesSQL.UsuarioSQL;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdminService {
    private UsuarioSQL userDAO;
    private PacienteSQL pacienteDAO;
    private DoctorSQL doctorDAO;
    private OperadorSQL operadorDAO;


    public AdminService() {
        this.userDAO = new UsuarioSQL();
        this.pacienteDAO = new PacienteSQL();
        this.doctorDAO = new DoctorSQL();
        this.operadorDAO = new OperadorSQL();
    }

    // En AdminService.java
    public void actualizarEstadoUsuario(int idUsuario, String nuevoEstado) throws Exception {
        userDAO.actualizarEstado(idUsuario, nuevoEstado);
    }

    public List<Usuario> obtenerUsuariosParaTabla() throws Exception {
        // 1. Obtener los usuarios básicos (login, pass, rol)
        List<Usuario> usuarios = userDAO.obtenerUsuariosSinAdmin();

        // 2. Rellenar el campo 'nombreUsuario' dependiendo del rol
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
            // Guardamos el nombre en el objeto Usuario (en el campo auxiliar que creaste)
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

    public void crearNuevoUsuario(Usuario nuevoUser, Object perfil) throws Exception {

        int idGenerado;

        try {
            idGenerado = userDAO.insertarUsuario(nuevoUser);
            nuevoUser.setIdUsuario(idGenerado);

            switch (nuevoUser.getRol().toLowerCase()) {
                case "paciente":
                    Paciente paciente = (Paciente) perfil;
                    paciente.setIdUsuario(idGenerado);
                    pacienteDAO.insertarPaciente(paciente);
                    break;
                case "doctor":
                    Doctor doctor = (Doctor) perfil;
                    doctor.setIdUsuario(idGenerado);
                    doctorDAO.insertarDoctor(doctor);
                    break;
                case "operador":
                    Operador operador = (Operador) perfil;
                    operador.setIdUsuario(idGenerado);
                    operadorDAO.insertarOperador(operador);
                    break;
                default:
                    throw new Exception("Rol desconocido: " + nuevoUser.getRol());
            }
        }
        catch (
                SQLException e) {
            throw new Exception("Fallo en la creación del usuario. Revise la inserción en el DAO: " + e.getMessage());
        }
    }

    public void eliminarUsuario(int idUsuario) throws Exception {
        try {
            userDAO.eliminarUsuario(idUsuario);
        } catch (
                SQLException e) {
            // En un entorno productivo, aquí podrías loguear el error y dar un mensaje más amigable
            throw new Exception("Fallo en la eliminación del usuario. Error SQL: " + e.getMessage());
        }
    }
}
