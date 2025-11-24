package gestion.gestion_citas_medicas.Controladores;
import gestion.gestion_citas_medicas.ClasesNormales.Cita_Medica;
import gestion.gestion_citas_medicas.Logica.CitaService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.util.Callback;

import javax.swing.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class CReprogramarCita implements ControladorInyectable{

    private Cita_Medica citaOriginal;
    private CMainVentana mainController;
    private CitaService citaService = new CitaService();
    // Panes principales
    @FXML private Pane paneFondo;
    @FXML private Pane paneFrente;

    // Controles principales
    @FXML private DatePicker selectorFechaRC;
    @FXML private ComboBox<String> choiceBoxHorarioRC;

    @FXML private TextField textfieldEspecialidadCitaRC;

    // Labels
    @FXML private Label labelAC;
    @FXML private Label labelFechaRC;
    @FXML private Label labelHorarioRC;
    @FXML private Label labelEspecialidadRC;

    // Botones
    @FXML private Button buttonReagendarCitaRC;
    @FXML private Button buttonRegresarMenuRC;


    @Override
    public void setMainController(CMainVentana mainController) {
        this.mainController = mainController;
    }



    public void setCitaData(Cita_Medica cita, CitaService service) {
        this.citaOriginal = cita;
        this.citaService = service;

        cargarDatosIniciales();
    }

    @FXML
    public void initialize() {
        configurarSelectorFecha();
        selectorFechaRC.valueProperty().addListener((obs, oldDate, newDate) -> {
            if (newDate != null && citaOriginal != null) {
                // Llama al método que usa el servicio para obtener horarios
                cargarHorariosDisponibles(newDate);
            } else {
                choiceBoxHorarioRC.getItems().clear();
            }
        });
    }


    private void cargarDatosIniciales() {
        if (citaOriginal != null) {
            textfieldEspecialidadCitaRC.setText(citaOriginal.getEspecialidadNombre());
            textfieldEspecialidadCitaRC.setEditable(false);

            selectorFechaRC.setValue(citaOriginal.getFechaCita());
            cargarHorariosDisponibles(citaOriginal.getFechaCita());
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
        selectorFechaRC.setDayCellFactory(dayCellFactory);
    }


    @FXML
    private void seleccionarHorario() {
        buttonReagendarCitaRC.setDisable(choiceBoxHorarioRC.getValue() == null);
    }

    private void cargarHorariosDisponibles(LocalDate fecha) {
        try {

            String especialidad = citaOriginal.getEspecialidadNombre();
            int idCita = citaOriginal.getIdCita();
            List<String> horarios = citaService.obtenerHorariosDisponibles(fecha, especialidad, idCita);

            choiceBoxHorarioRC.getItems().setAll(horarios);

            if (horarios.isEmpty()) {
                choiceBoxHorarioRC.setDisable(true);
                choiceBoxHorarioRC.setValue(null);

                JOptionPane.showMessageDialog(
                        null,
                        "No hay horarios disponibles para esta fecha.",
                        "Horarios Agotados",
                        JOptionPane.WARNING_MESSAGE
                );

            }
            else {
                // Hay horarios disponibles.

                boolean esFechaOriginal = fecha.isEqual(citaOriginal.getFechaCita());
                String horarioActual = citaOriginal.getHoraRango();
                if (esFechaOriginal && horarios.contains(horarioActual)) {
                    // Si estamos en la fecha original y el horario está libre, lo seleccionamos.
                    choiceBoxHorarioRC.getSelectionModel().select(horarioActual);
                }
                else {
                    // Si cambiamos de fecha, o si el horario original está ocupado, seleccionamos el primero.
                    choiceBoxHorarioRC.getSelectionModel().selectFirst();
                }

                choiceBoxHorarioRC.setDisable(false);
            }

        } catch (RuntimeException e) {
            // Manejo de error si falla la conexión o la lógica
            choiceBoxHorarioRC.getItems().clear();
            JOptionPane.showMessageDialog(
                    null,
                    "Error al cargar horarios: " + e.getMessage(),
                    "Error de reprogramación",
                    JOptionPane.ERROR_MESSAGE
            );
            choiceBoxHorarioRC.setDisable(true);
        }
    }

    @FXML
    public void clickReagendarCita(ActionEvent actionEvent) {
        LocalDate nuevaFecha = selectorFechaRC.getValue();
        String nuevoHorarioRango = choiceBoxHorarioRC.getValue();

        // 1. Validar que se haya seleccionado algo
        if (nuevaFecha == null || nuevoHorarioRango == null || nuevoHorarioRango.isEmpty()) {
            JOptionPane.showMessageDialog(
                    null,
                    "Debe seleccionar una fecha y un horario para reagendar la cita.",
                    "Error de Datos",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        try {

            int idCita = citaOriginal.getIdCita();
            citaService.reprogramarCita(idCita, nuevaFecha, nuevoHorarioRango);
            JOptionPane.showMessageDialog(
                    null,
                    "La cita ha sido reprogramada exitosamente para el " + nuevaFecha.toString() + " a las " + nuevoHorarioRango + ".",
                    "Éxito de Reprogramación",
                    JOptionPane.INFORMATION_MESSAGE
            );

            // 5. CERRAR la ventana del diálogo
            // Dado que se cargó como Stage modal (en CVerCitasPaciente), usamos getScene().getWindow().hide()
            ((Button) actionEvent.getSource()).getScene().getWindow().hide();

        } catch (Exception e) {
            // Manejar cualquier error de la capa de servicio (Validación o SQL)
            JOptionPane.showMessageDialog(
                    null,
                    e.getMessage(),
                    "Error de Reprogramación",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    @FXML
    public void clickRegresarAlMenu(ActionEvent actionEvent) {
        ((Button) actionEvent.getSource()).getScene().getWindow().hide();
    }
}
