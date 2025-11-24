package gestion.gestion_citas_medicas.Logica;

import gestion.gestion_citas_medicas.ClasesNormales.Historial_Medico;
import gestion.gestion_citas_medicas.ClasesNormales.Medicamento;
import gestion.gestion_citas_medicas.ClasesNormales.Receta;
import gestion.gestion_citas_medicas.ClasesNormales.Tratamiento;
import gestion.gestion_citas_medicas.ClasesSQL.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HistorialService {
    private final Historial_MedicoSQL historialSQL = new Historial_MedicoSQL();
    private final TratamientoSQL tratamientoSQL = new TratamientoSQL();
    private final RecetaSQL recetaSQL = new RecetaSQL();
    private final MedicamentoSQL medicamentoSQL = new MedicamentoSQL();
    private final Cita_MedicaSQL citaSQL = new Cita_MedicaSQL();



    public void guardarSoloHistorial(Historial_Medico historial) throws Exception {
        try {
            historialSQL.insert(historial);
        }
        catch (SQLException ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public List<Historial_Medico> obtenerHistorialPorPaciente(int idPaciente) throws Exception {
        try {
            List<Historial_Medico> historiales = historialSQL.findById(idPaciente);
            return historiales;
        }
        catch (SQLException ex) {
            throw new Exception(ex.getMessage());
        }
    }
}