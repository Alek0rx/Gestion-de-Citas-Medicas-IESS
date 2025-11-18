package gestion.gestion_citas_medicas.ConexionBD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion_BD {
    private static final String URL = "jdbc:mysql://localhost:3306/citas_medicas";
    private static final String USER = "root";
    private static final String PASSWORD = "root";
    private static Connection conexion;


    public static Connection getConnection() throws SQLException {
        if (conexion == null || conexion.isClosed()) {
            conexion = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return conexion;
    }

}
