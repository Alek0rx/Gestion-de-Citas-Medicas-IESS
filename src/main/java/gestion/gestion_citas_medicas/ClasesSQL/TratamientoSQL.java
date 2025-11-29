package gestion.gestion_citas_medicas.ClasesSQL;

import gestion.gestion_citas_medicas.ConexionBD.Conexion_BD;
import gestion.gestion_citas_medicas.ClasesNormales.Tratamiento;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TratamientoSQL {

    public int insertar(Tratamiento tratamiento) throws SQLException {
        String sql = "INSERT INTO tratamiento (id_cita_medica, descripcion, fecha_inicio, fecha_fin) VALUES (?, ?, ?, ?)";
        try (Connection con = Conexion_BD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, tratamiento.getIdCita());
            ps.setString(2, tratamiento.getDescripcion());
            ps.setDate(3, Date.valueOf(tratamiento.getFechaInicio()));

            if (tratamiento.getFechaFin() != null) {
                ps.setDate(4, Date.valueOf(tratamiento.getFechaFin()));
            } else {
                ps.setNull(4, Types.DATE);
            }

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    public Tratamiento obtener(int idTratamiento) throws SQLException {
        String sql = "SELECT * FROM tratamiento WHERE id_tratamiento = ?";
        try (Connection con = Conexion_BD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idTratamiento);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    LocalDate fechaInicio = rs.getDate("fecha_inicio").toLocalDate();

                    Date fechaFinSQL = rs.getDate("fecha_fin");
                    LocalDate fechaFin = fechaFinSQL != null ? fechaFinSQL.toLocalDate() : null;

                    return new Tratamiento(
                            rs.getInt("id_tratamiento"),
                            fechaInicio, fechaFin,
                            rs.getString("descripcion"),
                            rs.getInt("id_cita_medica")
                    );
                }
            }
        }
        return null;
    }

    public List<Tratamiento> obtenerPorCita(int idCita) throws SQLException {
        String sql = "SELECT * FROM tratamiento WHERE id_cita_medica = ?";
        List<Tratamiento> lista = new ArrayList<>();

        try (Connection con = Conexion_BD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idCita);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    LocalDate fechaInicio = rs.getDate("fecha_inicio").toLocalDate();

                    Date fechaFinSQL = rs.getDate("fecha_fin");
                    LocalDate fechaFin = fechaFinSQL != null ? fechaFinSQL.toLocalDate() : null;

                    lista.add(new Tratamiento(
                            rs.getInt("id_tratamiento"),
                            fechaInicio, fechaFin,
                            rs.getString("descripcion"),
                            rs.getInt("id_cita_medica")
                    ));
                }
            }
        }
        return lista;
    }

    public void actualizar(Tratamiento tratamiento) throws SQLException {
        String sql = "UPDATE tratamiento SET id_cita_medica = ?, descripcion = ?, fecha_inicio = ?, fecha_fin = ? WHERE id_tratamiento = ?";
        try (Connection con = Conexion_BD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, tratamiento.getIdCita());
            ps.setString(2, tratamiento.getDescripcion());
            ps.setDate(3, Date.valueOf(tratamiento.getFechaInicio()));

            if (tratamiento.getFechaFin() != null) {
                ps.setDate(4, Date.valueOf(tratamiento.getFechaFin()));
            } else {
                ps.setNull(4, Types.DATE);
            }

            ps.setInt(5, tratamiento.getIdTratamiento());
            ps.executeUpdate();
        }
    }

    public void eliminar(int idTratamiento) throws SQLException {
        String sql = "DELETE FROM tratamiento WHERE id_tratamiento = ?";
        try (Connection con = Conexion_BD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idTratamiento);
            ps.executeUpdate();
        }
    }

    public int insert(Connection con, Tratamiento t) throws SQLException {
        String sql = "INSERT INTO tratamiento (id_cita_medica, descripcion, fecha_inicio, fecha_fin) VALUES (?, ?, ?, ?)";
        int idTratamiento = -1;

        try (PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, t.getIdCita());
            stmt.setString(2, t.getDescripcion());
            stmt.setDate(3, Date.valueOf(t.getFechaInicio()));

            // Manejar fecha_fin nula
            if (t.getFechaFin() != null) {
                stmt.setDate(4, Date.valueOf(t.getFechaFin()));
            } else {
                stmt.setNull(4, java.sql.Types.DATE);
            }

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    idTratamiento = rs.getInt(1);
                }
            }
        }
        return idTratamiento;
    }
}

