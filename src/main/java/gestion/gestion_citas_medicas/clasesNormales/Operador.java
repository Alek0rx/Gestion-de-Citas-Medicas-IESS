package gestion.gestion_citas_medicas.clasesNormales;

public class Operador extends Persona {
    private int idOperador;

    // -- Constructor --
    public Operador() {
        super();
    }

    public Operador(String nombre, String apellido, String cedula, String telefono, String correo, int idOperador) {
        super(nombre, apellido, cedula, telefono, correo);
        this.idOperador = idOperador;
    }

    // -- Getters y Setters

    public int getIdOperador() {
        return idOperador;
    }

    public void setIdOperador(int idOperador) {
        this.idOperador = idOperador;
    }
}
