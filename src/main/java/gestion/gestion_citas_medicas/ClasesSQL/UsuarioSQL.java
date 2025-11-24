package gestion.gestion_citas_medicas.ClasesSQL;

import gestion.gestion_citas_medicas.ConexionBD.Conexion_BD;
import gestion.gestion_citas_medicas.ClasesNormales.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioSQL {
    // Método para obtener todos los usuarios

    public Usuario obtenerUsuario(String usuario, String password, String rol) {
        String sql = "SELECT * FROM usuario WHERE usuario = ? AND password = ? AND rol = ?";

        try (Connection con = Conexion_BD.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, usuario);
            stmt.setString(2, password);
            stmt.setString(3, rol);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("estado"),
                        rs.getString("rol"),
                        rs.getString("usuario"),
                        rs.getString("password")
                );
            }
            else {
                return null;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener usuario: " + e.getMessage(), e);
        }
    }

    public List<Usuario> findAll() throws Exception {
        String sql = "SELECT * FROM usuario";
        List<Usuario> lista = new ArrayList<>();

        try (Connection con = Conexion_BD.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Usuario u = new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("estado"),
                        rs.getString("rol"),
                        rs.getString("password"),
                        rs.getString("usuario")
                );
                lista.add(u);
            }
        }

        return lista;
    }
// Suponiendo que este método pertenece a UsuarioSQL o UsuarioDAO

    public List<Usuario> obtenerUsuariosSinAdmin() throws Exception {
        String sql = "SELECT id_usuario, estado, rol, password, usuario FROM usuario WHERE rol != 'administrador'";

        List<Usuario> lista = new ArrayList<>();

        try (Connection con = Conexion_BD.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Usuario u = new Usuario(
                        // Es importante que el orden de los campos aquí coincida con los campos de tu constructor
                        rs.getInt("id_usuario"),
                        rs.getString("estado"),
                        rs.getString("rol"),
                        rs.getString("password"),
                        rs.getString("usuario")
                );
                lista.add(u);
            }
        }

        return lista;
    }


    public void actualizarEstado(int idUsuario, String nuevoEstado) throws Exception {
        String sql = "UPDATE usuario SET estado = ? WHERE id_usuario = ?";

        try (Connection con = Conexion_BD.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, nuevoEstado); // "activo" o "inactivo"
            stmt.setInt(2, idUsuario);

            stmt.executeUpdate();
        }
    }

    public int insertarUsuario(Usuario u) throws SQLException {
        String sql = "INSERT INTO usuario (usuario, password, estado, rol) VALUES (?, ?, ?, ?)";

        try (Connection con = Conexion_BD.getConnection();
             // Pedimos a la BD que nos devuelva las claves generadas
             PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, u.getUsuario());
            stmt.setString(2, u.getPassword());
            stmt.setString(3, u.getEstado());
            stmt.setString(4, u.getRol());

            stmt.executeUpdate();

            // Obtener el ID generado (AUTO_INCREMENT)
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1); // Devuelve el ID
                } else {
                    throw new SQLException("La inserción falló, no se obtuvo el ID generado.");
                }
            }
        }
    }

    public void eliminarUsuario(int idUsuario) throws SQLException {
        String sql = "DELETE FROM usuario WHERE id_usuario = ?";

        try (Connection con = Conexion_BD.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);

            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas == 0) {
                // Esto solo ocurre si el ID no existe
                throw new SQLException("No se encontró el usuario con ID: " + idUsuario + " para eliminar.");
            }
        }
    }
}
