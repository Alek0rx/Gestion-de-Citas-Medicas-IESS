package gestion.gestion_citas_medicas.ClasesNormales;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.*;

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

    public static final Map<Integer, String> MEDICAMENTOS = Map.ofEntries(
            Map.entry(1,  "Paracetamol"),
            Map.entry(2,  "Ibuprofeno 400mg"),
            Map.entry(3,  "Amoxicilina"),
            Map.entry(4,  "Losartán"),
            Map.entry(5,  "Omeprazol"),
            Map.entry(6,  "Metformina"),
            Map.entry(7,  "Atorvastatina"),
            Map.entry(8,  "Sertralina"),
            Map.entry(9,  "Salbutamol inhalador"),
            Map.entry(10, "Clotrimazol crema"),
            Map.entry(11, "Ácido fólico"),
            Map.entry(12, "Levotiroxina"),
            Map.entry(13, "Tamsulosina"),
            Map.entry(14, "Hidrocortisona crema"),
            Map.entry(15, "Diclofenac gel"),
            Map.entry(16, "Cetirizina"),
            Map.entry(17, "Aspirina"),
            Map.entry(18, "Timolol gotas"),
            Map.entry(19, "Metronidazol"),
            Map.entry(20, "Ranitidina")
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
        especialidad.put(1, "Medicina General / Familiar");
        especialidad.put(2, "Pediatría");
        especialidad.put(3, "Ginecología y Obstetricia");
        especialidad.put(4, "Dermatología");
        especialidad.put(5, "Cardiología");
        especialidad.put(6, "Oftalmología");
        especialidad.put(7, "Odontología");
        especialidad.put(8, "Traumatología y Ortopedia");
        especialidad.put(9, "Psicología/Psiquiatría");
        especialidad.put(10, "Nutrición y Dietética");
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

    public static ObservableList<Medicamento> getMedicamentosObservableList() {
        // 1. Crear una lista temporal para almacenar los objetos Medicamento
        List<Medicamento> lista = new ArrayList<>();

        // 2. Iterar sobre las entradas (EntrySet) del Map
        for (Map.Entry<Integer, String> entry : MEDICAMENTOS.entrySet()) {
            int id = entry.getKey();
            String nombre = entry.getValue();

            // 3. Crear el objeto Medicamento y agregarlo a la lista
            lista.add(new Medicamento(id, nombre));
        }

        // 4. Convertir la lista estándar en una ObservableList de JavaFX
        return FXCollections.observableArrayList(lista);
    }
}
