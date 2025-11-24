package gestion.gestion_citas_medicas.ClasesSQL;

import gestion.gestion_citas_medicas.ConexionBD.Conexion_BD;
import gestion.gestion_citas_medicas.ClasesNormales.Receta;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecetaSQL {

    public void insertar(Receta receta) throws SQLException {
        String sql = "INSERT INTO receta (id_tratamiento, id_medicamento, indicaciones) VALUES (?, ?, ?)";
        try (Connection con = Conexion_BD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, receta.getIdTratamiento());
            ps.setInt(2, receta.getIdMedicamento());
            ps.setString(3, receta.getIndicaciones());
            ps.executeUpdate();
        }
    }

    public Receta obtener(int idTratamiento, int idMedicamento) throws SQLException {
        String sql = "SELECT * FROM receta WHERE id_tratamiento = ? AND id_medicamento = ?";
        try (Connection con = Conexion_BD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idTratamiento);
            ps.setInt(2, idMedicamento);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Receta(
                            rs.getInt("id_tratamiento"),
                            rs.getInt("id_medicamento"),
                            rs.getString("indicaciones")
                    );
                }
            }
        }
        return null;
    }

    public List<Receta> obtenerPorTratamiento(int idTratamiento) throws SQLException {
        String sql = "SELECT * FROM receta WHERE id_tratamiento = ?";
        List<Receta> lista = new ArrayList<>();

        try (Connection con = Conexion_BD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idTratamiento);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Receta(
                            rs.getInt("id_tratamiento"),
                            rs.getInt("id_medicamento"),
                            rs.getString("indicaciones")
                    ));
                }
            }
        }
        return lista;
    }

    public void actualizar(Receta receta) throws SQLException {
        String sql = "UPDATE receta SET indicaciones = ? WHERE id_tratamiento = ? AND id_medicamento = ?";
        try (Connection con = Conexion_BD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, receta.getIndicaciones());
            ps.setInt(2, receta.getIdTratamiento());
            ps.setInt(3, receta.getIdMedicamento());
            ps.executeUpdate();
        }
    }

    public void eliminar(int idTratamiento, int idMedicamento) throws SQLException {
        String sql = "DELETE FROM receta WHERE id_tratamiento = ? AND id_medicamento = ?";
        try (Connection con = Conexion_BD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idTratamiento);
            ps.setInt(2, idMedicamento);
            ps.executeUpdate();
        }
    }

    public void insert(Connection con, Receta r) throws SQLException {
        // La tabla receta usa un composite key (id_tratamiento, id_medicamento)
        String sql = "INSERT INTO receta (id_tratamiento, id_medicamento, indicaciones) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, r.getIdTratamiento()); // Debe estar seteado antes de llamar a este método
            stmt.setInt(2, r.getIdMedicamento());
            stmt.setString(3, r.getIndicaciones());

            stmt.executeUpdate();
        }
    }
}

