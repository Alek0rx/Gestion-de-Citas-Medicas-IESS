package gestion.gestion_citas_medicas.ClasesSQL;

import gestion.gestion_citas_medicas.ConexionBD.Conexion_BD;
import gestion.gestion_citas_medicas.ClasesNormales.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
}
