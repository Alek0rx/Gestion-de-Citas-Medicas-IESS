package gestion.gestion_citas_medicas.Controladores;

import gestion.gestion_citas_medicas.ClasesNormales.ElementosEstaticos;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class CAgendarCita implements ControladorInyectable{
    private CMainVentana mainController;
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
        // Carga las opciones de horarios
        choiceBoxHorarioAC.getItems().addAll(ElementosEstaticos.getSoloHorarios());

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

    }


    public void clickAgendarCita(ActionEvent actionEvent) {}

    @FXML
    private void clickRegresarAlMenu(ActionEvent event) throws IOException {
        mainController.volverAlMenuPrincipal("Paciente");
    }

}
