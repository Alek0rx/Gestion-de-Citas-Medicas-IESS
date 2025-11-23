package gestion.gestion_citas_medicas.Logica;

import gestion.gestion_citas_medicas.ClasesSQL.Cita_MedicaSQL;
import gestion.gestion_citas_medicas.ClasesSQL.HorarioSQL;

public class CitaService {
    private Cita_MedicaSQL citaMedicaDAO;
    private HorarioSQL horarioDAO;

    public CitaService() {
        this.citaMedicaDAO = new Cita_MedicaSQL();
        this.horarioDAO = new HorarioSQL();
    }


    public void generarCita () {}
}
