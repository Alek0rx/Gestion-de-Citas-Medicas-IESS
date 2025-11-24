package gestion.gestion_citas_medicas.ClasesSQL;

import gestion.gestion_citas_medicas.ClasesNormales.DetalleTratamiento;
import gestion.gestion_citas_medicas.ClasesNormales.Historial_Medico;
import gestion.gestion_citas_medicas.ConexionBD.Conexion_BD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
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

    public List<Historial_Medico> findById(int idPaciente) throws Exception {
        List<Historial_Medico> lista = new ArrayList<>();

        // La consulta une historial_medico con cita_medica para filtrar por el paciente.
        String sql = "SELECT h.* " +
                "FROM historial_medico h " +
                "JOIN cita_medica c ON h.id_cita_medica = c.id_cita_medica " +
                "WHERE c.id_paciente = ? " +
                "ORDER BY h.fecha_registro DESC";

        try (Connection con = Conexion_BD.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, idPaciente);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(new Historial_Medico(
                        rs.getInt("id_historial"),
                        rs.getDate("fecha_registro").toLocalDate(),
                        rs.getString("signos_vitales"),
                        rs.getInt("id_cita_medica"),
                        rs.getString("diagnostico")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Error al obtener el historial del paciente: " + e.getMessage());
        }
        return lista;
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

    public DetalleTratamiento findTratamientoByHistorialId(int idHistorial) throws Exception {
        DetalleTratamiento detalles = null;

        // Consulta compleja:
        // 1. historial_medico (h) -> Proporciona la FK id_cita_medica y el diagnóstico.
        // 2. tratamiento (t) -> JOIN OBLIGATORIO (asumimos que si hay historial, hay tratamiento).
        // 3. receta (r) -> LEFT JOIN (puede no haber receta).
        // 4. medicamento (m) -> LEFT JOIN (si hay receta, obtenemos el nombre).
        String sql = "SELECT h.diagnostico, t.descripcion, t.fecha_inicio, t.fecha_fin, " +
                "r.indicaciones, m.nombre AS nombre_medicamento " +
                "FROM historial_medico h " +
                "LEFT JOIN tratamiento t ON h.id_cita_medica = t.id_cita_medica " + // <--- CAMBIO AQUÍ
                "LEFT JOIN receta r ON t.id_tratamiento = r.id_tratamiento " +
                "LEFT JOIN medicamento m ON r.id_medicamento = m.id_medicamento " +
                "WHERE h.id_historial = ?";

        try (Connection con = Conexion_BD.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, idHistorial);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Obtener fechas, manejando NULL para fecha_fin
                LocalDate fechaInicio = (rs.getDate("fecha_inicio") != null) ? rs.getDate("fecha_inicio").toLocalDate() : null;
                LocalDate fechaFin = (rs.getDate("fecha_fin") != null) ? rs.getDate("fecha_fin").toLocalDate() : null;

                detalles = new DetalleTratamiento(
                        // 1. diagnosticoHistorial (h.diagnostico)
                        rs.getString("diagnostico"),
                        // 2. descripcionTratamiento (t.descripcion)
                        rs.getString("descripcion"),
                        // 3. fechaInicio (t.fecha_inicio)
                        fechaInicio,
                        // 4. fechaFin (t.fecha_fin)
                        fechaFin,
                        // 5. indicacionesReceta (r.indicaciones)
                        rs.getString("indicaciones"),

                        // 6. nombreMedicamento (m.nombre AS nombre_medicamento)
                        rs.getString("nombre_medicamento")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Error al obtener los detalles del tratamiento: " + e.getMessage());
        }
        return detalles;
    }
}
