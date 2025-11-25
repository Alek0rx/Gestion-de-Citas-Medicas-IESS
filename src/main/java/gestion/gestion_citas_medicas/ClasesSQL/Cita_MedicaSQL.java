package gestion.gestion_citas_medicas.ClasesSQL;

import gestion.gestion_citas_medicas.ClasesNormales.ElementosEstaticos;
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


    public List<Integer> encontrarHorariosOcupadosPorPaciente(int idPaciente, LocalDate fecha, int idCitaExcluir) throws Exception {

        String sql = "SELECT id_horario FROM cita_medica " +
                "WHERE id_paciente = ? AND fecha = ? AND id_cita_medica != ? AND estado != 'Cancelado'";

        List<Integer> horariosOcupados = new ArrayList<>();

        try (Connection con = Conexion_BD.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, idPaciente);
            stmt.setDate(2, Date.valueOf(fecha));
            stmt.setInt(3, idCitaExcluir);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    horariosOcupados.add(rs.getInt("id_horario"));
                }
            }
        }
        return horariosOcupados;
    }

    public List<Integer> encontrarHorariosOcupadosPorPaciente(int idPaciente, LocalDate fecha) throws Exception {


        String sql = "SELECT id_horario FROM cita_medica " +
                "WHERE id_paciente = ? AND fecha = ? AND estado != 'Cancelado'";

        List<Integer> horariosOcupados = new ArrayList<>();

        try (Connection con = Conexion_BD.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, idPaciente);
            stmt.setDate(2, Date.valueOf(fecha));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    horariosOcupados.add(rs.getInt("id_horario"));
                }
            }
        }
        return horariosOcupados;
    }


    public void cancelarCitaMedica(int idCita) throws Exception {
        String sql = "UPDATE cita_medica SET estado=? " + "WHERE id_cita_medica=?;";
        try (Connection con = Conexion_BD.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1,"cancelada");
            stmt.setInt(2,idCita);

            stmt.executeUpdate();
        }
    }

    public void actualizarCita(int idCita, LocalDate nuevaFecha, int nuevoIdHorario) throws Exception {
        String sql = "UPDATE cita_medica SET fecha=?, id_horario=? WHERE id_cita_medica=?;";
        try (Connection con = Conexion_BD.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(nuevaFecha));
            stmt.setInt(2, nuevoIdHorario);
            stmt.setInt(3, idCita);

            stmt.executeUpdate();
        }
    }

//    public List<Cita_Medica> findByDoctor(int idDoctor) throws Exception {
//        String sql = "SELECT * FROM cita_medica WHERE id_doctor=?";
//        List<Cita_Medica> lista = new ArrayList<>();
//
//        try (Connection con = Conexion_BD.getConnection();
//             PreparedStatement stmt = con.prepareStatement(sql)) {
//
//            stmt.setInt(1, idDoctor);
//            ResultSet rs = stmt.executeQuery();
//
//            while (rs.next()) {
//
//                lista.add(new Cita_Medica(
//                        rs.getDate("fecha").toLocalDate(),
//                        rs.getString("estado"),
//                        rs.getInt("id_cita_medica"),
//                        rs.getInt("id_doctor"),
//                        rs.getInt("id_paciente"),
//                        rs.getInt("id_horario")
//                ));
//            }
//        }
//
//        return lista;
//    }

    public List<Cita_Medica> findByDoctor(int idDoctor) throws Exception {
        String sql = "SELECT * FROM cita_medica WHERE id_doctor=?";
        List<Cita_Medica> lista = new ArrayList<>();

        try (Connection con = Conexion_BD.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, idDoctor);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Cita_Medica cita = new Cita_Medica(); // Usar constructor simple

                // 1. ASIGNAR EL ID DE LA CITA DE LA BASE DE DATOS
                cita.setIdCita(rs.getInt("id_cita_medica"));


                cita.setFechaCita(rs.getDate("fecha").toLocalDate());
                cita.setEstado(rs.getString("estado"));
                cita.setIdTipo(rs.getInt("id_tipo"));
                cita.setIdDoctor(rs.getInt("id_doctor"));
                cita.setIdPaciente(rs.getInt("id_paciente"));
                cita.setIdHorario(rs.getInt("id_horario"));

                lista.add(cita);
            }
        }
        return lista;
    }

    public void actualizarEstado(int idCita, String nuevoEstado) throws Exception {
        String sql = "UPDATE cita_medica SET estado = ? WHERE id_cita_medica = ?";


        try (Connection conn = Conexion_BD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nuevoEstado);
            ps.setInt(2, idCita);
            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas == 0) {
                throw new Exception("No se encontró la cita con ID: " + idCita + " para actualizar.");
            }

        } catch (Exception e) {
            throw new Exception("Error al actualizar el estado de la cita: " + e.getMessage(), e);
        }
    }


    public void asignarDoctor(int idCita, int idDoctor) throws Exception {
        String sql = "UPDATE cita_medica SET id_doctor = ?, estado = 'asignada' WHERE id_cita_medica = ? AND estado = 'pendiente'";

        try (Connection conn = Conexion_BD.getConnection(); // Asumiendo que esta es tu clase de conexión
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idDoctor);
            ps.setInt(2, idCita);
            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas == 0) {
                throw new Exception("No se pudo asignar el doctor. Verifique si la cita con ID: " + idCita + " existe y está en estado 'pendiente'.");
            }

        } catch (Exception e) {
            throw new Exception("Error al asignar el doctor a la cita: " + e.getMessage(), e);
        }
    }

    // Clase: gestion.gestion_citas_medicas.ClasesSQL.Cita_MedicaSQL

    public List<Cita_Medica> findAllPendientes() throws Exception {
        String sql = "SELECT cm.id_cita_medica, cm.fecha, cm.id_horario, cm.id_especialidad, cm.estado, cm.id_paciente, " +
                "p.nombre AS nombre_paciente, p.apellido AS apellido_paciente, " +
                "D.nombre AS nombre_doctor, D.apellido AS apellido_doctor " + // <-- Campos del Doctor
                "FROM cita_medica cm " +
                "JOIN paciente p ON cm.id_paciente = p.id_paciente " +
                "JOIN especialidad e ON cm.id_especialidad = e.id_especialidad " +
                "LEFT JOIN doctor D ON cm.id_doctor = D.id_doctor " + // <-- LEFT JOIN para incluir Doctor (NULL si no está asignado)
                "WHERE cm.estado IN ('pendiente', 'asignada')"; // Mostramos pendientes y asignadas

        List<Cita_Medica> citas = new ArrayList<>();

        try (Connection conn = Conexion_BD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Cita_Medica cita = new Cita_Medica();
                cita.setFechaCita(rs.getDate("fecha").toLocalDate());
                cita.setIdCita(rs.getInt("id_cita_medica"));
                cita.setIdHorario(rs.getInt("id_horario"));
                cita.setNombrePaciente(rs.getString("nombre_paciente"));
                cita.setApellidoPaciente(rs.getString("apellido_paciente"));

                // 🌟 NUEVO: Mapeo del Doctor Asignado
                String nombreDoc = rs.getString("nombre_doctor");
                String apellidoDoc = rs.getString("apellido_doctor");

                if (nombreDoc == null) {
                    cita.setNombreDoctor("PENDIENTE");
                    cita.setApellidoDoctor(""); // Dejamos el apellido vacío o como "DE ASIGNAR"
                } else {
                    cita.setNombreDoctor(nombreDoc);
                    cita.setApellidoDoctor(apellidoDoc);
                }

                citas.add(cita);
            }

        } catch (Exception e) {
            throw new Exception("Error al obtener todas las citas: " + e.getMessage(), e);
        }
        return citas;
    }

    // Clase: gestion.gestion_citas_medicas.ClasesSQL.Cita_MedicaSQL

    public List<Cita_Medica> findPendientesByEspecialidad(String especialidad) throws Exception {

        String sql = "SELECT cm.id_cita_medica, cm.fecha, cm.id_horario, cm.id_especialidad, cm.estado, cm.id_paciente, " +
                "p.nombre AS nombre_paciente, p.apellido AS apellido_paciente, " +
                "D.nombre AS nombre_doctor, D.apellido AS apellido_doctor " + // <-- Campos del Doctor
                "FROM cita_medica cm " +
                "JOIN paciente p ON cm.id_paciente = p.id_paciente " +
                "JOIN especialidad e ON cm.id_especialidad = e.id_especialidad " +
                "LEFT JOIN doctor D ON cm.id_doctor = D.id_doctor " + // <-- LEFT JOIN para incluir Doctor
                "WHERE cm.estado IN ('pendiente', 'asignada') AND e.nombre = ?"; // Filtro por estado y especialidad

        List<Cita_Medica> citas = new ArrayList<>();

        try (Connection conn = Conexion_BD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, especialidad);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Cita_Medica cita = new Cita_Medica();
                    cita.setIdCita(rs.getInt("id_cita_medica"));
                    cita.setFechaCita(rs.getDate("fecha").toLocalDate());
                    cita.setIdHorario(rs.getInt("id_horario"));
                    cita.setNombrePaciente(rs.getString("nombre_paciente"));
                    cita.setApellidoPaciente(rs.getString("apellido_paciente"));

                    // 🌟 NUEVO: Mapeo del Doctor Asignado
                    String nombreDoc = rs.getString("nombre_doctor");
                    String apellidoDoc = rs.getString("apellido_doctor");

                    if (nombreDoc == null) {
                        cita.setNombreDoctor("PENDIENTE");
                        cita.setApellidoDoctor("");
                    } else {
                        cita.setNombreDoctor(nombreDoc);
                        cita.setApellidoDoctor(apellidoDoc);
                    }

                    citas.add(cita);
                }
            }

        } catch (Exception e) {
            throw new Exception("Error al obtener citas por especialidad: " + e.getMessage(), e);
        }
        return citas;
    }

}
