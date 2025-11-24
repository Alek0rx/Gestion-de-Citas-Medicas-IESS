package gestion.gestion_citas_medicas.ClasesNormales;

public class Operador extends Persona {
    private int idOperador;
    private int idUsuario;

    // -- Constructor --
    public Operador() {
        super();
    }

    public Operador(String nombre, String apellido, String cedula, String telefono, String correo, String direccion,
                    int idOperador, int idUsuario) {
        super(nombre, apellido, cedula, telefono, correo, direccion);
        this.idOperador = idOperador;
        this.idUsuario = idUsuario;
    }

    // -- Getters y Setters

    public int getIdOperador() {
        return idOperador;
    }

    public void setIdOperador(int idOperador) {
        this.idOperador = idOperador;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Override
    public String toString() {
        return super.toString();
    }

}