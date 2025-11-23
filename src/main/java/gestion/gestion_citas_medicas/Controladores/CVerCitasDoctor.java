package gestion.gestion_citas_medicas.Controladores;
import gestion.gestion_citas_medicas.ClasesNormales.Cita_Medica;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

public class CVerCitasDoctor implements ControladorInyectable {
    private CMainVentana mainController;

    // Panes
    @FXML private Pane paneFondo;
    @FXML private Pane paneFrente;

    // labels
    @FXML private Label labelCMD;
    @FXML private Label labelEspecialidadCMD;

    @FXML private TextField textfieldEspecialidadCMD;

    // Tabla
    @FXML private TableView<Cita_Medica> tableCMD;

    // Columnas
    @FXML private TableColumn<Cita_Medica, Integer> columnIdCitaDoctor;
    @FXML private TableColumn<Cita_Medica, String> columnHoraCitaDoctor;
    @FXML private TableColumn<Cita_Medica, String> columnFechaCitaDoctor;
    @FXML private TableColumn<Cita_Medica, String> columnEstadoCitaDoctor;
    @FXML private TableColumn<Cita_Medica, String> columnPacienteDoctor;

    @FXML private Button buttonRegresarAlMenu;

    // Este botón NO tiene fx:id en tu FXML → ¡debes agregarlo!
    @FXML private Button buttonVerDetallesCita;

    @Override
    public void setMainController(CMainVentana mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void clickRegresarAlMenu() {
        mainController.volverAlMenuPrincipal("Doctor");
    }

    @FXML
    private void clickVerDetallesCita() {

    }
}
