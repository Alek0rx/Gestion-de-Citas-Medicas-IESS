package gestion.gestion_citas_medicas.Logica;

import gestion.gestion_citas_medicas.ClasesNormales.Receta;
import gestion.gestion_citas_medicas.ClasesNormales.Tratamiento;
import gestion.gestion_citas_medicas.ClasesSQL.RecetaSQL;
import gestion.gestion_citas_medicas.ClasesSQL.TratamientoSQL;
import gestion.gestion_citas_medicas.ConexionBD.Conexion_BD;
import java.sql.Connection;
import java.sql.SQLException;

public class TratamientoRecetaService {
    private final TratamientoSQL tratamientoSQL = new TratamientoSQL();
    private final RecetaSQL recetaSQL = new RecetaSQL();

    /**
     * Asigna un Tratamiento y, opcionalmente, una Receta, utilizando una transacción DB.
     */
    public void asignarTratamientoCompleto(Tratamiento t, Receta r) throws Exception {
        Connection con = null;
        try {
            con = Conexion_BD.getConnection();
            con.setAutoCommit(false); // Iniciar Transacción

            // 1. Guardar Tratamiento y obtener su ID
            int idTratamiento = tratamientoSQL.insert(con, t);

            if (idTratamiento == -1) {
                throw new Exception("Error al obtener el ID del tratamiento.");
            }

            // 2. Si hay Receta, guardarla
            if (r != null) {
                r.setIdTratamiento(idTratamiento); // Asignar la FK
                recetaSQL.insert(con, r);
            }

            con.commit(); // Confirmar la transacción

        } catch (SQLException e) {
            if (con != null) {
                try {
                    con.rollback(); // Revertir la transacción si algo falla
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            throw new Exception("Error transaccional al asignar tratamiento/receta: " + e.getMessage());

        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true); // Restaurar AutoCommit
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

