package gestion.gestion_citas_medicas.ClasesSQL;

import gestion.gestion_citas_medicas.ClasesNormales.Paciente;
import gestion.gestion_citas_medicas.ConexionBD.Conexion_BD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PacienteSQL {

    public void insert(Paciente p) throws Exception {
        String sql = "INSERT INTO paciente ( nombre, apellido, cedula, telefono, correo, direccion, fechaNacimiento, genero) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = Conexion_BD.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, p.getNombre());
            stmt.setString(2, p.getApellido());
            stmt.setString(3, p.getCedula());
            stmt.setString(4, p.getTelefono());
            stmt.setString(5, p.getCorreo());
            stmt.setString(6, p.getDireccion());
            stmt.setDate(7, java.sql.Date.valueOf(p.getFechaNacimientoPaciente()));
            stmt.setString(8, String.valueOf(p.getGenero()));

            stmt.executeUpdate();
        }
    }

    public void update(Paciente p) throws Exception {
        String sql = "UPDATE paciente SET nombre=?, apellido=?, cedula=?, telefono=?, correo=?, direccion=? WHERE id_paciente=?";

        try (Connection con = Conexion_BD.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, p.getNombre());
            stmt.setString(2, p.getApellido());
            stmt.setString(3, p.getCedula());
            stmt.setString(4, p.getTelefono());
            stmt.setString(5, p.getCorreo());
            stmt.setString(6, p.getDireccion());

            // FALTA CRÍTICA: enviar id_paciente
            stmt.setInt(7, p.getIdPaciente());

            stmt.executeUpdate();
        }
    }


    public void delete(int id) throws Exception {
        String sql = "DELETE FROM paciente WHERE id_paciente=?";

        try (Connection con = Conexion_BD.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public Paciente findById(int id) throws Exception {
        String sql = "SELECT * FROM paciente WHERE id_paciente=?";
        Paciente p = null;

        try (Connection con = Conexion_BD.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                p = new Paciente(
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("cedula"),
                        rs.getString("telefono"),
                        rs.getString("correo"),
                        rs.getString("direccion"),
                        rs.getInt("id_paciente"),
                        rs.getInt("id_usuario"),
                        rs.getString("genero").charAt(0),
                        rs.getDate("fechaNacimiento").toLocalDate()

                );
            }
        }
        return p;
    }

    public List<Paciente> findAll() throws Exception {
        String sql = "SELECT * FROM paciente";
        List<Paciente> lista = new ArrayList<>();

        try (Connection con = Conexion_BD.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new Paciente(
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("cedula"),
                        rs.getString("telefono"),
                        rs.getString("correo"),
                        rs.getString("direccion"),
                        rs.getInt("id_paciente"),
                        rs.getInt("id_usuario"),
                        rs.getString("genero").charAt(0),
                        rs.getDate("fechaNacimiento").toLocalDate()
                ));
            }
        }
        return lista;
    }

    public Object obtenerPacientePorIdUsuario(int idUsuario) throws Exception {
        String sql = "SELECT * FROM paciente WHERE id_usuario=?";
        Paciente p = null;

        try (Connection con = Conexion_BD.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                p = new Paciente(
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("cedula_paciente"),
                        rs.getString("telefono"),
                        rs.getString("correo"),
                        rs.getString("direccion"),
                        rs.getInt("id_paciente"),
                        rs.getInt("id_usuario"),
                        rs.getString("genero").charAt(0),
                        rs.getDate("fecha_nacimiento").toLocalDate()
                );
            }
        }
        return p;
    }

}
