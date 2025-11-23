package gestion.gestion_citas_medicas.Controladores;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

// Ajusta según tu modelo de datos
import gestion.gestion_citas_medicas.ClasesNormales.Cita_Medica;

public class CGestionarCitasOp implements ControladorInyectable{
    private CMainVentana mainController;
    // Panes principales
    @FXML private Pane paneFondo;
    @FXML private Pane paneFrente;

    // Labels
    @FXML private Label labelBienvenidaOp;
    @FXML private Label labelNombreOp;
    @FXML private Label labelEspecialidadOp;
    @FXML private Label labelDoctorOp;

    // Campos de texto
    @FXML private TextField textfieldNombreOp;

    // ChoiceBox para filtros
    @FXML private ChoiceBox<String> choiceboxEspecialidadOp;
    @FXML private ChoiceBox<String> choiceboxDoctorOp;

    // Tabla de citas pendientes
    @FXML private TableView<Cita_Medica> tableCitasOp;

    // Columnas
    @FXML private TableColumn<Cita_Medica, Integer> columnIdCitaOp;
    @FXML private TableColumn<Cita_Medica, String>  columnFechaCitaOp;
    @FXML private TableColumn<Cita_Medica, String>  columnHoraCitaOp;
    @FXML private TableColumn<Cita_Medica, String>  columnPacienteCitaOp;

    // Botones
    @FXML private Button buttonAsignarDoctor;
    @FXML private Button buttonRegresarAlMenu;


    @Override
    public void setMainController(CMainVentana mainController) {
        this.mainController = mainController;
    }

    public void clickAsignarDoctor(ActionEvent actionEvent) {
    }

    public void clickRegresarAlMenu(ActionEvent actionEvent) {
        mainController.volverAlMenuPrincipal("Operador");
    }
}
