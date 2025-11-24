package gestion.gestion_citas_medicas.ClasesSQL;

import gestion.gestion_citas_medicas.ClasesNormales.ElementosEstaticos;
import gestion.gestion_citas_medicas.ClasesNormales.Paciente;
import gestion.gestion_citas_medicas.ConexionBD.Conexion_BD;
import gestion.gestion_citas_medicas.ClasesNormales.Doctor;

import javax.print.Doc;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DoctorSQL {

    public void insert(Doctor d) throws Exception {
        String sql = "INSERT INTO doctor (id_especialidad, nombre, apellido, cedula_doc, estado, correo, telefono, direccion, consultorio) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = Conexion_BD.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, d.getIdUsuario());
            stmt.setInt(2, d.getIdEspecialidad());
            stmt.setString(3, d.getNombre());
            stmt.setString(4, d.getApellido());
            stmt.setString(5, d.getCedula());
            stmt.setString(6, d.getEstado());
            stmt.setString(7, d.getCorreo());
            stmt.setString(8, d.getTelefono());
            stmt.setString(9, d.getDireccion());
            stmt.setString(10, d.getConsultorio());

            stmt.executeUpdate();
        }
    }


    public void update(Doctor d) throws Exception {
        String sql = "UPDATE doctor SET id_usuario=?, id_especialidad=?, nombre=?, apellido=?, cedula_doc=?, estado=?, correo=?, telefono=?, direccion=?, consultorio=? " +
                "WHERE id_doctor=?";

        try (Connection con = Conexion_BD.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, d.getIdUsuario());
            stmt.setInt(2, d.getIdEspecialidad());
            stmt.setString(3, d.getNombre());
            stmt.setString(4, d.getApellido());
            stmt.setString(5, d.getCedula());
            stmt.setString(6, d.getEstado());
            stmt.setString(7, d.getCorreo());
            stmt.setString(8, d.getTelefono());
            stmt.setString(9, d.getDireccion());
            stmt.setString(10, d.getConsultorio());
            stmt.setInt(11, d.getIdDoctor());

            stmt.executeUpdate();
        }
    }


    public void delete(int id) throws Exception {
        String sql = "DELETE FROM doctor WHERE id_doctor=?";

        try (Connection con = Conexion_BD.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }


    public Doctor findById(int id) throws Exception {
        String sql = "SELECT * FROM doctor WHERE id_doctor=?";
        Doctor d = null;

        try (Connection con = Conexion_BD.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                d = new Doctor(
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("cedula_doc"),
                        rs.getString("correo"),
                        rs.getString("telefono"),
                        rs.getString("direccion"),
                        rs.getInt("id_doctor"),
                        rs.getInt("id_usuario"),
                        rs.getInt("id_especialidad"),
                        rs.getString("estado"),
                        rs.getString("consultorio")
                );
            }
        }

        return d;
    }


    public List<Doctor> findAll() throws Exception {
        String sql = "SELECT * FROM doctor";
        List<Doctor> lista = new ArrayList<>();

        try (Connection con = Conexion_BD.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new Doctor(
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("cedula_doc"),
                        rs.getString("correo"),
                        rs.getString("telefono"),
                        rs.getString("direccion"),
                        rs.getInt("id_doctor"),
                        rs.getInt("id_usuario"),
                        rs.getInt("id_especialidad"),
                        rs.getString("estado"),
                        rs.getString("consultorio")
                ));
            }
        }
        return lista;
    }

    public Object obtenerDoctorPorIdUsuario(int idUsuario) throws Exception {
        String sql = "SELECT * FROM doctor WHERE id_usuario=?";
        Doctor d = null;

        try (Connection con = Conexion_BD.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                d = new Doctor(
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("cedula_doc"),
                        rs.getString("correo"),
                        rs.getString("telefono"),
                        rs.getString("direccion"),
                        rs.getInt("id_doctor"),
                        rs.getInt("id_usuario"),
                        rs.getInt("id_especialidad"),
                        rs.getString("estado"),
                        rs.getString("consultorio")
                );
            }
        }
        return d;
    }



    public Map<Integer, String> obtenerMapaNombresDoctores() throws Exception {
        // 1. Incluimos el id_doctor en la consulta SQL
        String sql = "SELECT id_doctor, nombre, apellido FROM doctor WHERE estado = 'activo'"; // Solo doctores activos
        Map<Integer, String> mapaNombres = new HashMap<>();

        try (Connection con = Conexion_BD.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int idDoctor = rs.getInt("id_doctor");
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");

                // 2. Construimos el String del nombre completo
                String nombreCompleto = nombre + " " + apellido;

                // 3. Añadimos al mapa: ID (clave) -> Nombre Completo (valor)
                mapaNombres.put(idDoctor, nombreCompleto);
            }
        }
        return mapaNombres;
    }
    public String obtenerNombreCompleto(int idUsuario) {
        String sql = "SELECT nombre, apellido FROM doctor WHERE id_usuario = ?";
        try (Connection con = Conexion_BD.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("nombre") + " " + rs.getString("apellido");
            }
        } catch (Exception e) { e.printStackTrace(); }
        return "Desconocido";
    }

    public String obtenerEspecialidadPorIdUsuario(int idUsuario) {
        String sql = "SELECT id_especialidad FROM doctor WHERE id_usuario = ?";

        try (Connection con = Conexion_BD.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int idEspecialidad = rs.getInt("id_especialidad");

                return ElementosEstaticos.especialidad.getOrDefault(idEspecialidad, "No Asignada");
            }
        } catch (Exception e) {
            System.err.println("Error al obtener especialidad: " + e.getMessage());
        }
        return "N/A";
    }

    public void insertarDoctor(Doctor d) throws SQLException {
        // 🚨 Importante: Ajusta el SQL para incluir todas las columnas de tu tabla 'doctor'
        String sql = "INSERT INTO doctor (nombre, apellido, cedula_doc, telefono, correo, direccion, id_especialidad, id_usuario, estado, consultorio) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?,?,?)";

        try (Connection con = Conexion_BD.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, d.getNombre());
            stmt.setString(2, d.getApellido());
            stmt.setString(3, d.getCedula());
            stmt.setString(4, d.getTelefono());
            stmt.setString(5, d.getCorreo());
            stmt.setString(6, d.getDireccion());

            // Si el objeto Doctor ya tiene el ID de especialidad:
            stmt.setInt(7, d.getIdEspecialidad()); // Suponiendo que el objeto Doctor ya tiene el ID de especialidad

            // Clave Foránea
            stmt.setInt(8, d.getIdUsuario()); // El FK obtenido de la tabla 'usuario'
            stmt.setString(9, d.getEstado());
            stmt.setString(10, d.getConsultorio());
            stmt.executeUpdate();
        }
    }

}
