package gestion.gestion_citas_medicas.ClasesSQL;

import gestion.gestion_citas_medicas.ConexionBD.Conexion_BD;
import gestion.gestion_citas_medicas.ClasesNormales.Horario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HorarioSQL {

    public Horario buscarPorId(int id) {
        String sql = "SELECT * FROM horario WHERE id_horario = ?";
        try (Connection conn = Conexion_BD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Horario(
                        rs.getInt("id_horario"),
                        rs.getTime("hora_inicio").toLocalTime(),
                        rs.getTime("hora_fin").toLocalTime()
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public List<Horario> listarTodos() {
        List<Horario> lista = new ArrayList<>();
        String sql = "SELECT * FROM horario ORDER BY hora_inicio";

        try (Connection conn = Conexion_BD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Horario h = new Horario(
                        rs.getInt("id_horario"),
                        rs.getTime("hora_inicio").toLocalTime(),
                        rs.getTime("hora_fin").toLocalTime()
                );
                lista.add(h);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
}

