package gestion.gestion_citas_medicas.Controladores;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import gestion.gestion_citas_medicas.ClasesNormales.Cita_Medica;


public class CVerCitasPaciente implements ControladorInyectable {
    private CMainVentana mainController;
    // Panes principales
    @FXML private Pane paneFondo;
    @FXML private Pane paneFrente;

    // TableView y sus columnas
    @FXML private TableView<Cita_Medica> tableCitas;

    @FXML private TableColumn<Cita_Medica, String> columnFechaCita;
    @FXML private TableColumn<Cita_Medica, String> columnHoraCita;
    @FXML private TableColumn<Cita_Medica, String> columnHoraEspecialidad;
    @FXML private TableColumn<Cita_Medica, String> columnDoctorCita;
    @FXML private TableColumn<Cita_Medica, String> columnEstadoCita;

    // Etiqueta título
    @FXML private Label labelVerCitas;

    // Botones
    @FXML private Button buttonRegresarAlMenu;
    @FXML private Button buttonCancelarCita;
    @FXML private Button buttonReprogramarCita;

    @Override
    public void setMainController(CMainVentana mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void clickRegresarAlMenu() {
        mainController.volverAlMenuPrincipal("Paciente");
    }

    @FXML
    private void clickCancelarCita() {

    }

    @FXML
    private void clickReprogramarCita() {

    }
}

