package gestion.gestion_citas_medicas.ClasesSQL;

import gestion.gestion_citas_medicas.ClasesNormales.Medicamento;
import gestion.gestion_citas_medicas.ConexionBD.Conexion_BD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicamentoSQL {

    public boolean insertar(Medicamento m) {
        String sql = "INSERT INTO medicamento (nombre, descripcion, fecha_creacion, fecha_expiracion) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection conn = Conexion_BD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, m.getNombre());
            stmt.setString(2, m.getDescripcion());
            stmt.setDate(3, Date.valueOf(m.getFechaCreacion()));
            stmt.setDate(4, Date.valueOf(m.getFechaExpiracion()));

            int filas = stmt.executeUpdate();
            if (filas > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    m.setIdMedicamento(rs.getInt(1));
                }
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    public Medicamento buscarPorId(int id) {
        String sql = "SELECT * FROM medicamento WHERE id_medicamento = ?";

        try (Connection conn = Conexion_BD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Medicamento(
                        rs.getInt("id_medicamento"),
                        rs.getDate("fecha_expiracion").toLocalDate(),
                        rs.getDate("fecha_creacion").toLocalDate(),
                        rs.getString("descripcion"),
                        rs.getString("nombre")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    public List<Medicamento> listarTodos() {
        List<Medicamento> lista = new ArrayList<>();

        String sql = "SELECT * FROM medicamento ORDER BY nombre";

        try (Connection conn = Conexion_BD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new Medicamento(
                        rs.getInt("id_medicamento"),
                        rs.getDate("fecha_expiracion").toLocalDate(),
                        rs.getDate("fecha_creacion").toLocalDate(),
                        rs.getString("descripcion"),
                        rs.getString("nombre")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }


    public boolean actualizar(Medicamento m) {
        String sql = "UPDATE medicamento SET nombre = ?, descripcion = ?, fecha_creacion = ?, fecha_expiracion = ? WHERE id_medicamento = ?";

        try (Connection conn = Conexion_BD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, m.getNombre());
            stmt.setString(2, m.getDescripcion());
            stmt.setDate(3, Date.valueOf(m.getFechaCreacion()));
            stmt.setDate(4, Date.valueOf(m.getFechaExpiracion()));
            stmt.setInt(5, m.getIdMedicamento());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    public boolean eliminar(int id) {
        String sql = "DELETE FROM medicamento WHERE id_medicamento = ?";

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

