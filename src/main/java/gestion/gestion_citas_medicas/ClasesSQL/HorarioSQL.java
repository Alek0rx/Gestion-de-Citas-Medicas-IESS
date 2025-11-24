package gestion.gestion_citas_medicas.ClasesSQL;

import gestion.gestion_citas_medicas.ClasesNormales.Paciente;
import gestion.gestion_citas_medicas.ConexionBD.Conexion_BD;
import gestion.gestion_citas_medicas.ClasesNormales.Horario;

import java.sql.*;
import java.time.LocalTime;
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

    public Horario findById(int idHorario) throws Exception {
        String sql = "SELECT * FROM horario WHERE id_horario = ?";
        Horario h = null;

        try (Connection con = Conexion_BD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idHorario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Horario(
                            rs.getInt("id_horario"),
                            rs.getTime("hora_inicio").toLocalTime(),
                            rs.getTime("hora_fin").toLocalTime()
                    );
                }
            }
        }

        return h;
    }

    public Horario findById2(int idHorario) throws Exception {

        String sql = "SELECT * FROM horario WHERE id_horario = ?";

        Connection con = Conexion_BD.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, idHorario);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            Horario h = new Horario();
            h.setIdHorario(rs.getInt("idHorario"));
            h.setIdHorario(Integer.parseInt(rs.getString("hora")));
            return h;
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

