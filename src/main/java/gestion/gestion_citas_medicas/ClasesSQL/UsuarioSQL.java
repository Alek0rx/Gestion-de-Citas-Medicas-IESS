package gestion.gestion_citas_medicas.ClasesSQL;

import gestion.gestion_citas_medicas.ConexionBD.Conexion_BD;
import gestion.gestion_citas_medicas.ClasesNormales.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UsuarioSQL {
    // Método para obtener todos los usuarios


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

    // Método de prueba para mostrar usuarios en consola
    public void mostrarUsuarios() {
        try {
            List<Usuario> usuarios = findAll();
            for (Usuario u : usuarios) {
                System.out.println("ID: " + u.getIdUsuario() +
                        ", Usuario: " + u.getUsuario() +
                        ", Password: " + u.getPassword() +
                        ", Rol: " + u.getRol() +
                        ", Estado: " + u.getEstado());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método main para probar la clase
    public static void main(String[] args) {
        UsuarioSQL usuarioSQL = new UsuarioSQL();
        usuarioSQL.mostrarUsuarios();
    }
}
