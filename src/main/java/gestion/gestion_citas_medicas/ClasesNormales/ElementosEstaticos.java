package gestion.gestion_citas_medicas.ClasesNormales;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class ElementosEstaticos {

    public static final Map<Integer, String> horarios = new LinkedHashMap<>();
    public static final Map<Integer, String> especialidad = new LinkedHashMap<>();


    private static final Map<Integer, String> PREFIJOS_CONSULTORIO = Map.ofEntries(
            // Clave (ID) -> Valor (CÓDIGO GENÉRICO)
            Map.entry(1,  "MG-00M"),
            Map.entry(2,  "PED-00P"),
            Map.entry(3,  "GIN-00G"),
            Map.entry(4,  "DER-00D"),
            Map.entry(5,  "CAR-00C"),
            Map.entry(6,  "OFT-00O"),
            Map.entry(7,  "ODO-00O"),
            Map.entry(8,  "TRA-00T"),
            Map.entry(9,  "PSI-00P"),
            Map.entry(10, "NUT-00N"),
            Map.entry(11, "END-00E"),
            Map.entry(12, "URO-00U")
    );

    static {
        horarios.put(1, "08:00 - 08:20");
        horarios.put(2, "08:20 - 08:40");
        horarios.put(3, "08:40 - 09:00");
        horarios.put(4, "09:00 - 09:20");
        horarios.put(5, "09:20 - 09:40");
        horarios.put(6, "09:40 - 10:00");
        horarios.put(7, "10:00 - 10:20");
        horarios.put(8, "10:20 - 10:40");
        horarios.put(9, "10:40 - 11:00");
        horarios.put(10, "11:00 - 11:20");
        horarios.put(11, "11:20 - 11:40");
        horarios.put(12, "11:40 - 12:00");
        horarios.put(13, "12:00 - 12:20");
        horarios.put(14, "12:20 - 12:40");
        horarios.put(15, "12:40 - 13:00");
        horarios.put(16, "14:00 - 14:20");
        horarios.put(17, "14:20 - 14:40");
        horarios.put(18, "14:40 - 15:00");
        horarios.put(19, "15:00 - 15:20");
        horarios.put(20, "15:20 - 15:40");
        horarios.put(21, "15:40 - 16:00");
        horarios.put(22, "16:00 - 16:20");
        horarios.put(23, "16:20 - 16:40");
        horarios.put(24, "16:40 - 17:00");
    }
    static {
        especialidad.put(1, "Medicina General");
        especialidad.put(2, "Pediatría");
        especialidad.put(3, "Ginecología y Obstetricia");
        especialidad.put(4, "Dermatología");
        especialidad.put(5, "Cardiología");
        especialidad.put(6, "Oftalmología");
        especialidad.put(7, "Odontología");
        especialidad.put(8, "Traumatología y Ortopedia");
        especialidad.put(9, "Psicología/Psiquiatría");
        especialidad.put(10, "Nutrición");
        especialidad.put(11, "Endocrinología");
        especialidad.put(12, "Urología");
    }

    public static Collection<String> getSoloHorarios() {
        return horarios.values();
    }

    public static String getHorarioPorId(int id) {
        return horarios.get(id);
    }

    public static String getEspecialidadPorId(int id) {
        return especialidad.get(id);
    }
    public static int getIdEspecialidad(String tipo) {
        for (Map.Entry<Integer, String> entry : especialidad.entrySet()) {
            if (entry.getValue().equals(tipo)) {
                return entry.getKey();
            }
        }
        throw new RuntimeException("Error, no se encontró la especialidad");
    }

    public static int getIdHorario (String horario) {
        for (Map.Entry<Integer, String> entry : horarios.entrySet()) {
            if (entry.getValue().equals(horario)) {
                return entry.getKey();
            }
        }
        throw new RuntimeException("Error, no se encontró el horario");
    }
    public static String generarConsultorioGenerico(int idEspecialidad) {
        return PREFIJOS_CONSULTORIO.getOrDefault(idEspecialidad, "N/A");
    }

}
