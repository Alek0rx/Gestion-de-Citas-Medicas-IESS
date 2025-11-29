package gestion.gestion_citas_medicas.Controladores;

import gestion.gestion_citas_medicas.ClasesNormales.Cita_Medica;
import gestion.gestion_citas_medicas.ClasesNormales.ElementosEstaticos;
import gestion.gestion_citas_medicas.ClasesNormales.Medicamento;
import gestion.gestion_citas_medicas.ClasesNormales.Receta;
import gestion.gestion_citas_medicas.ClasesNormales.Tratamiento;
import gestion.gestion_citas_medicas.Logica.TratamientoRecetaService; // Asumiendo este servicio existe
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javax.swing.JOptionPane;
import java.time.LocalDate;

public class CCrearTratamiento {

    // --- Atributos de soporte ---
    private Cita_Medica citaActual;
    private final TratamientoRecetaService servicio = new TratamientoRecetaService(); // Servicio para guardar

    // --- Controles FXML ---
    @FXML private TextArea textAreaTratamiento;
    @FXML private DatePicker selectorFechaInicio;
    @FXML private DatePicker selectorFechaFin;
    @FXML private CheckBox checkboxReceta;
    @FXML private TextArea textAreaReceta;
    @FXML private ChoiceBox<Medicamento> choiceboxMedicamentos;
    @FXML private Button buttonAsignarTratamientoReceta;

    // --- Inyección de datos ---
    public void setCitaMedica(Cita_Medica cita) {
        this.citaActual = cita;
    }

    // -----------------------------------------------------------------------
    // INICIALIZACIÓN Y FLUJO SECUENCIAL
    // -----------------------------------------------------------------------

    @FXML
    private void initialize() {
        // 1. Cargar Medicamentos (desde ElementosEstaticos o tu servicio)
        cargarMedicamentos();

        // 2. Establecer el estado inicial de la interfaz
        setRecetaControlsDisabled(true); // Receta deshabilitada por defecto

        // 3. Implementar la lógica de flujo estricto (bindings y listeners)
        setupSequentialFlow();

        // 4. Configurar restricciones de fechas
        setupDateRestrictions();
    }

    private void cargarMedicamentos() {
        // Asumiendo que ElementosEstaticos ahora tiene el método para obtener la lista
        try {
            // Ejemplo: Carga desde tu ElementosEstaticos simulado o un servicio real
            choiceboxMedicamentos.setItems(ElementosEstaticos.getMedicamentosObservableList());
        } catch (Exception e) {
            System.err.println("Error al cargar medicamentos: " + e.getMessage());
            // Manejo de error más robusto si es necesario
        }
    }

    private void setupSequentialFlow() {

        // --- SECUENCIA DE TRATAMIENTO (Habilitación) ---

        // 1. Fecha Inicio: Se habilita cuando la Descripción tiene texto
        selectorFechaInicio.disableProperty().bind(
                textAreaTratamiento.textProperty().isEmpty()
        );

        // 2. Fecha Fin: Se habilita cuando la Fecha Inicio tiene valor
        selectorFechaFin.disableProperty().bind(
                selectorFechaInicio.valueProperty().isNull()
        );

        // --- SECUENCIA DE RECETA (Activación) ---

        // El estado de los campos de receta sigue al checkbox
        checkboxReceta.selectedProperty().addListener((obs, oldValue, newValue) -> {
            setRecetaControlsDisabled(!newValue);
        });

        // --- HABILITACIÓN FINAL DEL BOTÓN (Binding Complejo) ---

        // A) Lógica base para el Tratamiento (es obligatorio): Descripción y Fecha Inicio deben estar listos
        BooleanBinding isTreatmentReady = textAreaTratamiento.textProperty().isEmpty()
                .or(selectorFechaInicio.valueProperty().isNull());

        // B) Lógica para la Receta (solo si está marcada): Indicaciones y Medicamento deben estar llenos
        BooleanBinding isRecetaReady = textAreaReceta.textProperty().isEmpty()
                .or(choiceboxMedicamentos.valueProperty().isNull());

        // C) El botón se deshabilita si:
        //    (Tratamiento NO está listo)
        // OR (El CheckBox está marcado Y la Receta NO está lista)

        buttonAsignarTratamientoReceta.disableProperty().bind(
                isTreatmentReady.or(
                        checkboxReceta.selectedProperty().and(isRecetaReady)
                )
        );
    }

    private void setupDateRestrictions() {
        LocalDate today = LocalDate.now();

        // Restricción para Fecha Inicio: Solo hoy o futuro
        selectorFechaInicio.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(today)); // Deshabilita fechas pasadas
            }
        });

        // Listener para Fecha Fin: Inicializar con Fecha Inicio y no permitir anteriores a Inicio
        selectorFechaInicio.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                // 1. Inicializar Fecha Fin con la Fecha Inicio
                selectorFechaFin.setValue(newValue);

                // 2. Establecer restricción para Fecha Fin
                selectorFechaFin.setDayCellFactory(picker -> new DateCell() {
                    @Override
                    public void updateItem(LocalDate date, boolean empty) {
                        super.updateItem(date, empty);
                        // Deshabilita fechas anteriores a la Fecha Inicio seleccionada (newValue)
                        setDisable(empty || date.isBefore(newValue));
                    }
                });
            } else {
                selectorFechaFin.setDayCellFactory(null); // Quitar restricción si Inicio es nulo
            }
        });
    }

    private void setRecetaControlsDisabled(boolean disabled) {
        textAreaReceta.setDisable(disabled);
        choiceboxMedicamentos.setDisable(disabled);
    }

    // -----------------------------------------------------------------------
    // LÓGICA DE GUARDADO
    // -----------------------------------------------------------------------

    @FXML
    public void clickAsignarTratamientoReceta() {

        // Aquí no se necesitan validaciones de 'vacío' gracias a los bindings,
        // solo verificamos que la Cita esté cargada.
        if (citaActual == null) {
            JOptionPane.showMessageDialog(null, "Error: Cita no cargada.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {

            Tratamiento t = new Tratamiento(
                    textAreaTratamiento.getText().trim(),
                    selectorFechaInicio.getValue(),
                    selectorFechaFin.getValue(),
                    citaActual.getIdCita()
            );

            // 2. Crear POJO Receta (Opcional)
            Receta r = null;
            if (checkboxReceta.isSelected()) {
                Medicamento med = choiceboxMedicamentos.getValue();

                // Nota: Usamos 0 o cualquier valor temporal para idTratamiento,
                // ya que el ID real se asigna en el servicio después de la inserción.
                r = new Receta(
                        0,
                        med.getIdMedicamento(), // Asumiendo que el POJO Medicamento tiene getId()
                        textAreaReceta.getText().trim()
                );
            }

            // 3. Llamar al servicio (el servicio maneja la inserción secuencial y transaccional)
            servicio.asignarTratamientoCompleto(t, r);

            JOptionPane.showMessageDialog(null, "Tratamiento y Receta asignados exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

            // Cerrar el modal
            Stage stage = (Stage) buttonAsignarTratamientoReceta.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Error al asignar: " + e.getMessage(),
                    "Error DB", JOptionPane.ERROR_MESSAGE);
        }
    }
}