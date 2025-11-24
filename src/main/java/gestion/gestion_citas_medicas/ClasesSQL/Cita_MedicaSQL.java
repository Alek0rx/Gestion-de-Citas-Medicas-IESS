package gestion.gestion_citas_medicas.ClasesSQL;

import gestion.gestion_citas_medicas.ConexionBD.Conexion_BD;
import gestion.gestion_citas_medicas.ClasesNormales.Cita_Medica;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Cita_MedicaSQL {
    public void insert(Cita_Medica c) throws Exception {
        String sql = "INSERT INTO cita_medica (id_doctor, id_paciente, id_horario, id_especialidad, estado, fecha) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = Conexion_BD.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setNull(1, java.sql.Types.INTEGER);
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


    public List<Integer> encontrarCitas(int idEspecialidad, LocalDate fecha) throws Exception {
        String sql = "SELECT id_horario FROM cita_medica "
                + "WHERE fecha = ? AND id_especialidad = ? AND estado = 'pendiente' ORDER BY id_horario";

        List<Integer> horariosOcupados = new ArrayList<>();

        try (Connection con = Conexion_BD.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(fecha));
            stmt.setInt(2, idEspecialidad);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    horariosOcupados.add(rs.getInt("id_horario"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Error al obtener horarios ocupados.");
        }

        return horariosOcupados;
    }

    public List<Cita_Medica> encontrarCitasPorIdPaciente(int idPaciente) throws Exception {
        List<Cita_Medica> lista = new ArrayList<>();

        // Consulta SQL simplificada: Solo datos de la cita (incluyendo todos los IDs)
        String sql = "SELECT " +
                "    id_cita_medica, fecha, estado, id_especialidad, id_horario, id_doctor " +
                "FROM cita_medica " +
                "WHERE id_paciente = ? AND estado = ?" +
                "ORDER BY fecha, id_horario;";

        try (Connection con = Conexion_BD.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, idPaciente);
            stmt.setString(2, "pendiente");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {

                    Cita_Medica cita = new Cita_Medica();

                    cita.setIdCita(rs.getInt("id_cita_medica"));
                    cita.setFechaCita(rs.getDate("fecha").toLocalDate());
                    cita.setEstado(rs.getString("estado"));

                    cita.setIdTipo(rs.getInt("id_especialidad"));
                    cita.setIdHorario(rs.getInt("id_horario"));
                    cita.setIdDoctor(rs.getInt("id_doctor"));

                    lista.add(cita);
                }
            }
        }
        return lista;
    }

    public List<Cita_Medica> findByDoctor(int idDoctor) throws Exception {
        String sql = "SELECT * FROM cita_medica WHERE id_cita_medica=?";
        List<Cita_Medica> lista = new ArrayList<>();

        try (Connection con = Conexion_BD.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, idDoctor);
            ResultSet rs = stmt.executeQuery();

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

    public List<Cita_Medica> findByEspecialidad(int idEspecialidad) throws Exception {
        String sql = """
        SELECT c.*
        FROM cita_medica c
        JOIN doctor d ON c.id_doctor = d.id_doctor
        WHERE d.id_especialidad = ?
    """;

        List<Cita_Medica> lista = new ArrayList<>();

        Connection con = Conexion_BD.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, idEspecialidad);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Cita_Medica c = new Cita_Medica();
            c.setIdCita(rs.getInt("idCita"));
            c.setFechaCita(rs.getDate("fechaCita").toLocalDate());
            c.setEstado(rs.getString("estado"));
            c.setIdTipo(rs.getInt("idTipo"));
            c.setIdDoctor(rs.getInt("idDoctor"));
            c.setIdPaciente(rs.getInt("idPaciente"));
            c.setIdHorario(rs.getInt("idHorario"));

            lista.add(c);
        }

        return lista;
    }


}
