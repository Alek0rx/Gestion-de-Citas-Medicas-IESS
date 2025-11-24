package gestion.gestion_citas_medicas.ClasesSQL;

import gestion.gestion_citas_medicas.ClasesNormales.Especialidad;
import gestion.gestion_citas_medicas.ConexionBD.Conexion_BD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class EspecialidadSQL {

    public void insert(Especialidad e) throws Exception {
        String sql = "INSERT INTO especialidad (nombre, descripcion) VALUES (?, ?)";

        try (Connection con = Conexion_BD.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, e.getNombre());
            stmt.setString(2, e.getDescripcion());

            stmt.executeUpdate();
        }
    }

    public void update(Especialidad e) throws Exception {
        String sql = "UPDATE especialidad SET nombre=?, descripcion=? WHERE id_especialidad=?";

        try (Connection con = Conexion_BD.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, e.getNombre());
            stmt.setString(2, e.getDescripcion());
            stmt.setInt(3, e.getIdTipo());

            stmt.executeUpdate();
        }
    }

    public void delete(int id) throws Exception {
        String sql = "DELETE FROM especialidad WHERE id_especialidad=?";

        try (Connection con = Conexion_BD.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public Especialidad findById(int id) throws Exception {
        String sql = "SELECT * FROM especialidad WHERE id_especialidad=?";
        Especialidad e = null;

        try (Connection con = Conexion_BD.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                e = new Especialidad(
                        rs.getInt("id_especialidad"),
                        rs.getString("descripcion"),
                        rs.getString("nombre")
                );
            }
        }

        return e;
    }

    public List<Especialidad> findAll() throws Exception {
        String sql = "SELECT * FROM especialidad";
        List<Especialidad> lista = new ArrayList<>();

        try (Connection con = Conexion_BD.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new Especialidad(
                        rs.getInt("id_especialidad"),
                        rs.getString("descripcion"),
                        rs.getString("nombre")
                ));
            }
        }

        return lista;
    }

    public int findIdByNombre(String nombre) throws Exception {
        String sql = "SELECT idEspecialidad FROM especialidad WHERE nombre = ?";
        Connection con = Conexion_BD.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, nombre);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getInt("idEspecialidad");
        }
        return 0; // o lanzar excepción
    }

}
