package gestion.gestion_citas_medicas.Logica;

import gestion.gestion_citas_medicas.ClasesNormales.SessionManager;
import gestion.gestion_citas_medicas.ClasesNormales.Usuario;
import gestion.gestion_citas_medicas.ClasesSQL.DoctorSQL;
import gestion.gestion_citas_medicas.ClasesSQL.OperadorSQL;
import gestion.gestion_citas_medicas.ClasesSQL.PacienteSQL;
import gestion.gestion_citas_medicas.ClasesSQL.UsuarioSQL;

import javax.swing.*;
import java.sql.SQLException;
import java.util.Locale;

public class UsuarioService {
    private UsuarioSQL usuarioDAO;
    private PacienteSQL pacienteDAO;
    private DoctorSQL doctorDAO;
    private OperadorSQL operadorDAO;

    public UsuarioService() {
        this.usuarioDAO = new UsuarioSQL();
        this.pacienteDAO = new PacienteSQL();
        this.doctorDAO = new DoctorSQL();
        this.operadorDAO = new OperadorSQL();
    }

    public Usuario iniciarSesion(String usuario, String password, String rol) {
        if (usuario == null || password == null || usuario.isBlank() || password.isBlank()) {
            throw new IllegalArgumentException("Las credenciales no pueden estar vacías");
        }
        Usuario user = usuarioDAO.obtenerUsuario(usuario,password,rol);
        if (user == null) {
            throw new RuntimeException("Usuario o contraseña incorrectos");
        }
        System.out.println("Usario encontrado");
        return user;
    }

    public void establecerUsuario(Usuario user) throws Exception {
        if (user == null) {
            throw new IllegalArgumentException("Usuario nulo recibido en establecerUsuario");
        }

        String rolRaw = user.getRol();
        if (rolRaw == null || rolRaw.isBlank()) {
            throw new IllegalArgumentException("El rol del usuario es nulo o vacío");
        }

        String rol = rolRaw.trim().toLowerCase(Locale.ROOT);
        Object perfil = null;

        try {
            switch (rol) {
                case "paciente" -> perfil = pacienteDAO.obtenerPacientePorIdUsuario(user.getIdUsuario());
                case "doctor"   -> perfil = doctorDAO.obtenerDoctorPorIdUsuario(user.getIdUsuario());
                case "operador" -> perfil = operadorDAO.obtenerOperadorPorIdUsuario(user.getIdUsuario());
                case "administrador" -> {
                    perfil = user;
                }
                default -> throw new RuntimeException("Rol no reconocido: " + user.getRol());
            }
        } catch (
                SQLException sqle) {
            // si tus DAOs lanzan SQLException, conviértelo a RuntimeException o lánzalo arriba
            throw new RuntimeException("Error al obtener perfil desde la BD: " + sqle.getMessage(), sqle);
        } catch (Exception e) {
            // para atrapar otras excepciones del DAO y ver el mensaje real
            throw new RuntimeException("Error al cargar perfil: " + e.getMessage(), e);
        }

        if (perfil == null) {
            throw new RuntimeException("Perfil no encontrado para el usuario con rol '" + user.getRol() + "'");
        }

        // ok: guardamos sesión
        SessionManager.setSesion(user, perfil);
    }

}
