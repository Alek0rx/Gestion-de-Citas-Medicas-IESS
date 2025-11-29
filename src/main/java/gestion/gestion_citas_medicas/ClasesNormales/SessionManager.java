package gestion.gestion_citas_medicas.ClasesNormales;
public class SessionManager {

    private static Usuario usuarioBase;
    private static Object perfil;  // Puede ser Paciente, Doctor u Operador
    private static Object especialidades;

    public static void setSesion(Usuario base, Object perfilData) {
        usuarioBase = base;
        perfil = perfilData;
    }

    public static Usuario getUsuarioBase() {
        return usuarioBase;
    }

    public static <T> T getPerfil(Class<T> tipo) {
        return tipo.cast(perfil); // Castea al tipo de perfil correcto
    }

    public static void cerrarSesion() {
        usuarioBase = null;
        perfil = null;
    }

    public static void actualizarPerfil(Object nuevoperfil) {
        perfil = nuevoperfil;
    }

}
