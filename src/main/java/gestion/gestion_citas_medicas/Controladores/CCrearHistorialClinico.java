package gestion.gestion_citas_medicas.Controladores;


import gestion.gestion_citas_medicas.ClasesNormales.Cita_Medica;
import gestion.gestion_citas_medicas.ClasesNormales.Historial_Medico;
import gestion.gestion_citas_medicas.Logica.HistorialService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;

import javax.swing.*;
import java.time.LocalDate;

public class CCrearHistorialClinico {

    // ATRIBUTOS DE SOPORTE
    private Cita_Medica citaActual;
    private final HistorialService historialService = new HistorialService();

    // === Contenedores principales
    @FXML private Pane paneFondo;
    @FXML private Pane paneFrente;

    // === Etiquetas
    @FXML private Label labelBienvenidaCH;
    @FXML private Label labelDiagnosticoCH;
    @FXML private Label labelSignosVitalesCH;
    @FXML private Label labelFechaCH;

    // === Controles de entrada
    @FXML private DatePicker selectorFechaCH;
    @FXML private TextArea textareaDiagnosticoCH;
    @FXML private TextArea textareaSignosVitalesCH;

    // === Botón
    @FXML private Button buttonCrearHistorial;


    @FXML
    private void initialize() {

    }

    public void setCitaMedica(Cita_Medica cita) {
        this.citaActual = cita;
        cargarDatosIniciales();
    }

    private void cargarDatosIniciales() {
        if (citaActual != null) {
            // La fecha de registro siempre será la de hoy, pero se muestra para el usuario
            selectorFechaCH.setValue(LocalDate.now());
            configurarSelectorFecha();
        }
    }

    @FXML
    private void crearHistorial(ActionEvent event) {
        if (citaActual == null) {
            JOptionPane.showMessageDialog(null, "Error: No se ha cargado la información de la cita.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (textareaDiagnosticoCH.getText().trim().isEmpty() || textareaSignosVitalesCH.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar el Diagnóstico y los Signos Vitales.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (selectorFechaCH.getValue() == null) {
            JOptionPane.showMessageDialog(null, "Error: La fecha de registro es nula. Por favor, reinicie la ventana.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Historial_Medico historial = new Historial_Medico(
                    citaActual.getIdCita(),
                    textareaDiagnosticoCH.getText().trim(),
                    textareaSignosVitalesCH.getText().trim(),
                    selectorFechaCH.getValue()
            );

            System.out.println(selectorFechaCH.getValue().toString());
            historialService.guardarSoloHistorial(historial);

            JOptionPane.showMessageDialog(null, "Historial Clínico creado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

            // 5. Cerrar el modal
            Stage stage = (Stage) buttonCrearHistorial.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Error al guardar el historial en la base de datos: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    public void configurarSelectorFecha() {
        final LocalDate hoy = LocalDate.now();

        final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);

                        // Si la fecha (item) es anterior a hoy, la deshabilita
                        if (item.isBefore(hoy)) {
                            setDisable(true);
                            // Opcional: Cambiar el estilo para que se vea gris
                            setStyle("-fx-background-color: #ffc0cb;");
                        }
                    }
                };
            }
        };
        selectorFechaCH.setDayCellFactory(dayCellFactory);
    }
}