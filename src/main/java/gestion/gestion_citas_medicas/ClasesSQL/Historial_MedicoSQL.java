package gestion.gestion_citas_medicas.ClasesSQL;

import gestion.gestion_citas_medicas.ClasesNormales.Historial_Medico;
import gestion.gestion_citas_medicas.ConexionBD.Conexion_BD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Historial_MedicoSQL {

    public void insert(Historial_Medico h) throws Exception {
        String sql = "INSERT INTO historial_medico (id_cita_medica, diagnostico, signos_vitales, fecha_registro) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection con = Conexion_BD.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, h.getIdCita());
            stmt.setString(2, h.getDiagnostico());
            stmt.setString(3, h.getSignosVitales());
            stmt.setDate(4, java.sql.Date.valueOf(h.getFechaRegistro()));

            stmt.executeUpdate();
        }
    }

    public void update(Historial_Medico h) throws Exception {
        String sql = "UPDATE historial_medico SET id_cita_medica=?, diagnostico=?, signos_vitales=?, fecha_registro=? " +
                "WHERE id_historial=?";

        try (Connection con = Conexion_BD.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, h.getIdCita());
            stmt.setString(2, h.getDiagnostico());
            stmt.setString(3, h.getSignosVitales());
            stmt.setDate(4, java.sql.Date.valueOf(h.getFechaRegistro()));
            stmt.setInt(5, h.getIdHistorial());

            stmt.executeUpdate();
        }
    }

    public void delete(int id) throws Exception {
        String sql = "DELETE FROM historial_medico WHERE id_historial=?";

        try (Connection con = Conexion_BD.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public Historial_Medico findById(int id) throws Exception {
        String sql = "SELECT * FROM historial_medico WHERE id_historial=?";
        Historial_Medico h = null;

        try (Connection con = Conexion_BD.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                h = new Historial_Medico(
                        rs.getInt("id_historial"),
                        rs.getDate("fecha_registro").toLocalDate(),
                        rs.getString("signos_vitales"),
                        rs.getInt("id_cita_medica"),
                        rs.getString("diagnostico")
                );
            }
        }

        return h;
    }

    public List<Historial_Medico> findAll() throws Exception {
        String sql = "SELECT * FROM historial_medico";
        List<Historial_Medico> lista = new ArrayList<>();

        try (Connection con = Conexion_BD.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new Historial_Medico(
                        rs.getInt("id_historial"),
                        rs.getDate("fecha_registro").toLocalDate(),
                        rs.getString("signos_vitales"),
                        rs.getInt("id_cita_medica"),
                        rs.getString("diagnostico")
                ));
            }
        }

        return lista;
    }
}
