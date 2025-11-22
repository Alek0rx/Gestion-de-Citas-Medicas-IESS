package gestion.gestion_citas_medicas.ClasesSQL;

import gestion.gestion_citas_medicas.ClasesNormales.Operador;
import gestion.gestion_citas_medicas.ConexionBD.Conexion_BD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OperadorSQL {

    // INSERTAR
    public boolean insertar(Operador op) {
        String sql = "INSERT INTO operador (id_usuario, nombre, apellido, cedula_op, telefono, correo, direccion) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexion_BD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, op.getIdUsuario());
            stmt.setString(2, op.getNombre());
            stmt.setString(3, op.getApellido());
            stmt.setString(4, op.getCedula());
            stmt.setString(5, op.getTelefono());
            stmt.setString(6, op.getCorreo());
            stmt.setString(7, op.getDireccion());

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    op.setIdOperador(rs.getInt(1));
                }
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    // BUSCAR POR ID
    public Operador buscarPorId(int id) {
        String sql = "SELECT * FROM operador WHERE id_operador = ?";

        try (Connection conn = Conexion_BD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Operador(
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("cedula_op"),
                        rs.getString("telefono"),
                        rs.getString("correo"),
                        rs.getString("direccion"),
                        rs.getInt("id_operador"),
                        rs.getInt("id_usuario")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    // LISTAR TODOS
    public List<Operador> listarTodos() {
        List<Operador> lista = new ArrayList<>();
        String sql = "SELECT * FROM operador ORDER BY apellido, nombre";

        try (Connection conn = Conexion_BD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Operador op = new Operador(
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("cedula_op"),
                        rs.getString("telefono"),
                        rs.getString("correo"),
                        rs.getString("direccion"),
                        rs.getInt("id_operador"),
                        rs.getInt("id_usuario")
                );

                lista.add(op);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }


    // ACTUALIZAR
    public boolean actualizar(Operador op) {
        String sql = " UPDATE operador SET nombre = ?, apellido = ?, cedula_op = ?, telefono = ?, correo = ?, direccion = ? WHERE id_operador = ? ";

        try (Connection conn = Conexion_BD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, op.getNombre());
            stmt.setString(2, op.getApellido());
            stmt.setString(3, op.getCedula());
            stmt.setString(4, op.getTelefono());
            stmt.setString(5, op.getCorreo());
            stmt.setString(6, op.getDireccion());
            stmt.setInt(7, op.getIdOperador());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    // ELIMINAR
    public boolean eliminar(int id) {
        String sql = "DELETE FROM operador WHERE id_operador = ?";

        try (Connection conn = Conexion_BD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
