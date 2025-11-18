package gestion.gestion_citas_medicas.ClasesSQL;

import gestion.gestion_citas_medicas.ConexionBD.Conexion_BD;
import gestion.gestion_citas_medicas.clasesNormales.Paciente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PacienteSQL {

    public void insert(Paciente p) throws Exception {
        String sql = "INSERT INTO paciente (id, nombre, apellido, cedula, telefono) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = Conexion_BD.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, p.getNombre());
            stmt.setString(2, p.getApellido());
            stmt.setString(3, p.getCedula());
            stmt.setString(4, p.getTelefono());

            stmt.executeUpdate();
        }
    }


    public void update(Paciente p) throws Exception {
        String sql = "UPDATE paciente SET nombre=?, apellido=?, cedula=?, telefono=? WHERE id=?";

        try (Connection con = Conexion_BD.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, p.getNombre());
            stmt.setString(2, p.getApellido());
            stmt.setString(3, p.getCedula());
            stmt.setString(4, p.getTelefono());
            stmt.setInt(5, p.getIdPaciente());

            stmt.executeUpdate();
        }
    }

    public void delete(int id) throws Exception {
        String sql = "DELETE FROM paciente WHERE id=?";

        try (Connection con = Conexion_BD.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public Paciente findById(int id) throws Exception {
        String sql = "SELECT * FROM paciente WHERE id=?";
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
                        rs.getInt("id"),
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
                        rs.getInt("id"),
                        rs.getDate("fechaNacimiento").toLocalDate()
                ));
            }
        }
        return lista;
    }
}
