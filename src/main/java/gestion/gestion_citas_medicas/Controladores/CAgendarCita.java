package gestion.gestion_citas_medicas.Controladores;

import gestion.gestion_citas_medicas.ClasesNormales.Cita_Medica;
import gestion.gestion_citas_medicas.ClasesNormales.ElementosEstaticos;
import gestion.gestion_citas_medicas.Logica.CitaService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.util.Callback;

import javax.swing.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class CAgendarCita implements ControladorInyectable{
    private CMainVentana mainController;
    private CitaService citaService = new CitaService();
    @FXML
    public Pane paneFondo;
    @FXML
    public Pane paneFrente;
    @FXML
    public DatePicker selectorFechaAC;
    @FXML
    public ComboBox<String> choiceBoxHorarioAC;
    @FXML
    public Button buttonAgendarCitaAC;
    @FXML
    public Button buttonRegresarMenuAC;
    @FXML
    public Label labelAC;
    @FXML
    public Label labelFechaAC;
    @FXML
    public Label labelHorarioAC;
    @FXML
    public RadioButton rbMedicinaGeneralAC;
    @FXML
    public RadioButton rbDermatologiaAC;
    @FXML
    public RadioButton rbPediatriaAC;
    @FXML
    public RadioButton rbGinecologiaAC;
    @FXML
    public Label labelEspecialidadAC;
    @FXML
    public RadioButton rbCardiologiaAC;
    @FXML
    public RadioButton rbOdontologiaAC;
    @FXML
    public RadioButton rbOftalmologiaAC;
    @FXML
    public RadioButton rbTraumatologiaAC;
    @FXML
    public RadioButton rbPsicologiaAC;
    @FXML
    public RadioButton rbEndocrinologiaAC;
    @FXML
    public RadioButton rbNutricionAC;
    @FXML
    public RadioButton rbUrologiaAC;

    private ToggleGroup groupEspecialidadAC;


    // Función para asignar el controlador del contenedor principal
    public void setMainController(CMainVentana mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void initialize(){
        // Agrupa radiobuttons
        groupEspecialidadAC = new ToggleGroup();
         rbCardiologiaAC.setToggleGroup(groupEspecialidadAC);
         rbOdontologiaAC.setToggleGroup(groupEspecialidadAC);
         rbOftalmologiaAC.setToggleGroup(groupEspecialidadAC);
         rbTraumatologiaAC.setToggleGroup(groupEspecialidadAC);
         rbPsicologiaAC.setToggleGroup(groupEspecialidadAC);
         rbEndocrinologiaAC.setToggleGroup(groupEspecialidadAC);
         rbNutricionAC.setToggleGroup(groupEspecialidadAC);
         rbUrologiaAC.setToggleGroup(groupEspecialidadAC);
         rbMedicinaGeneralAC.setToggleGroup(groupEspecialidadAC);
         rbPediatriaAC.setToggleGroup(groupEspecialidadAC);
         rbDermatologiaAC.setToggleGroup(groupEspecialidadAC);
         rbGinecologiaAC.setToggleGroup(groupEspecialidadAC);

         selectorFechaAC.setDisable(true);
         selectorFechaAC.setValue(null);
         choiceBoxHorarioAC.setDisable(true);
         choiceBoxHorarioAC.setValue(null);
         buttonAgendarCitaAC.setDisable(true);
         groupEspecialidadAC.selectToggle(null);
         configurarSelectorFecha();
    }

    private void cargarHorarios() {
        LocalDate fecha = selectorFechaAC.getValue();
        if (fecha == null) {
            choiceBoxHorarioAC.getItems().clear();
            choiceBoxHorarioAC.setDisable(true);
            return;
        }

        RadioButton selected = (RadioButton) groupEspecialidadAC.getSelectedToggle();
        if (selected == null) {
            return;
        }
        String especialidad = selected.getText();
        List<String> horarioDisponible = citaService.obtenerHorariosDisponibles(fecha, especialidad);

        if (horarioDisponible.isEmpty()) {

            JOptionPane.showMessageDialog(
                    null,
                    "Todos los horarios para la especialidad de " + especialidad + " están ocupados el día " + fecha.toString() + ". Por favor, seleccione otra fecha.",
                    "Horarios Agotados",
                    JOptionPane.WARNING_MESSAGE
            );

            choiceBoxHorarioAC.setDisable(true);
        }
        else {
            choiceBoxHorarioAC.getItems().clear();
            choiceBoxHorarioAC.getItems().addAll(horarioDisponible);
            choiceBoxHorarioAC.setDisable(false);
        }
    }


    public void clickAgendarCita(ActionEvent actionEvent) {
        generarCita();
    }

    @FXML
    private void clickRegresarAlMenu(ActionEvent event) throws IOException {
        mainController.volverAlMenuPrincipal("Paciente");
    }

    @FXML
    private void seleccionarEspecialidad (ActionEvent event) throws IOException {
        selectorFechaAC.setDisable(false);
    }

    @FXML
    private void seleccionarFecha (ActionEvent event) throws IOException {
        LocalDate fechaSeleccionada = selectorFechaAC.getValue();

        if (fechaSeleccionada == null) {
            choiceBoxHorarioAC.getItems().clear();
            choiceBoxHorarioAC.setValue(null);
            choiceBoxHorarioAC.setDisable(true);
            buttonAgendarCitaAC.setDisable(true);
            return;
        }
        choiceBoxHorarioAC.setDisable(false);
        cargarHorarios();
    }

    @FXML
    private void seleccionarHorario (ActionEvent event) throws IOException {
        buttonAgendarCitaAC.setDisable(false);


    }
    public void generarCita () {
        LocalDate date = selectorFechaAC.getValue();
        String horario = choiceBoxHorarioAC.getValue();
        RadioButton selected = (RadioButton) groupEspecialidadAC.getSelectedToggle();
        String especialidad = selected.getText();

        try {
            citaService.generarCita(especialidad, date, horario);
            JOptionPane.showMessageDialog(
                    null,
                    "Cita agendada satisfactoriamente",
                    "Éxito!",
                    JOptionPane.INFORMATION_MESSAGE
            );
            resetearFormulario();
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Error al generar el cita: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
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
        selectorFechaAC.setDayCellFactory(dayCellFactory);
    }

    private void resetearFormulario() {
        selectorFechaAC.setValue(null);
        groupEspecialidadAC.selectToggle(null);
        choiceBoxHorarioAC.getItems().clear();
        choiceBoxHorarioAC.setValue(null);
        choiceBoxHorarioAC.setDisable(true);
        selectorFechaAC.setDisable(true);
        buttonAgendarCitaAC.setDisable(true);
    }

}
