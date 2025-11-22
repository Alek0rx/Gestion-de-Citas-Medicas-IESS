package gestion.gestion_citas_medicas.ClasesSQL;

import gestion.gestion_citas_medicas.ConexionBD.Conexion_BD;
import gestion.gestion_citas_medicas.ClasesNormales.Cita_Medica;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Cita_MedicaSQL {
    public void insert(Cita_Medica c) throws Exception {
        String sql = "INSERT INTO cita_medica (id_doctor, id_paciente, id_horario, id_especialidad, estado, fecha) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = Conexion_BD.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            // id_cita_medica NO se envía (AUTO_INCREMENT)
            stmt.setInt(1, c.getIdDoctor());
            stmt.setInt(2, c.getIdPaciente());
            stmt.setInt(3, c.getIdHorario());
            stmt.setInt(4, c.getIdTipo());   // id_especialidad
            stmt.setString(5, c.getEstado());
            stmt.setDate(6, Date.valueOf(c.getFechaCita()));

            stmt.executeUpdate();
        }
    }

    public void update(Cita_Medica c) throws Exception {
        String sql = "UPDATE cita_medica SET id_doctor=?, id_paciente=?, id_horario=?, id_especialidad=?, estado=?, fecha=? " +
                "WHERE id_cita_medica=?";

        try (Connection con = Conexion_BD.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, c.getIdDoctor());
            stmt.setInt(2, c.getIdPaciente());
            stmt.setInt(3, c.getIdHorario());
            stmt.setInt(4, c.getIdTipo());
            stmt.setString(5, c.getEstado());
            stmt.setDate(6, Date.valueOf(c.getFechaCita()));
            stmt.setInt(7, c.getIdCita());

            stmt.executeUpdate();
        }
    }

    public void delete(int id) throws Exception {
        String sql = "DELETE FROM cita_medica WHERE id_cita_medica=?";

        try (Connection con = Conexion_BD.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public Cita_Medica findById(int id) throws Exception {
        String sql = "SELECT * FROM cita_medica WHERE id_cita_medica=?";
        Cita_Medica c = null;

        try (Connection con = Conexion_BD.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                c = new Cita_Medica(
                        rs.getInt("id_cita_medica"),
                        rs.getDate("fecha").toLocalDate(),
                        rs.getString("estado"),
                        rs.getInt("id_especialidad"),
                        rs.getInt("id_doctor"),
                        rs.getInt("id_paciente"),
                        rs.getInt("id_horario")
                );
            }
        }

        return c;
    }

    public List<Cita_Medica> findAll() throws Exception {
        String sql = "SELECT * FROM cita_medica";
        List<Cita_Medica> lista = new ArrayList<>();

        try (Connection con = Conexion_BD.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new Cita_Medica(
                        rs.getInt("id_cita_medica"),
                        rs.getDate("fecha").toLocalDate(),
                        rs.getString("estado"),
                        rs.getInt("id_especialidad"),
                        rs.getInt("id_doctor"),
                        rs.getInt("id_paciente"),
                        rs.getInt("id_horario")
                ));
            }
        }
        return lista;
    }
}
