package gestion.gestion_citas_medicas.ClasesNormales;

public class Receta {
    private int idTratamiento;
    private int idMedicamento;
    private String indicaciones;

    // -- Constructor --
    public Receta () {}
    public Receta(int idTratamiento, int idMedicamento, String indicaciones) {
        this.idTratamiento = idTratamiento;
        this.idMedicamento = idMedicamento;
        this.indicaciones = indicaciones;
    }

    // -- Getters y Setters --

    public int getIdTratamiento() {
        return idTratamiento;
    }

    public void setIdTratamiento(int idTratamiento) {
        this.idTratamiento = idTratamiento;
    }

    public String getIndicaciones() {
        return indicaciones;
    }

    public void setIndicaciones(String indicaciones) {
        this.indicaciones = indicaciones;
    }

    public int getIdMedicamento() {
        return idMedicamento;
    }

    public void setIdMedicamento(int idMedicamento) {
        this.idMedicamento = idMedicamento;
    }
}
