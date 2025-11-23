package gestion.gestion_citas_medicas.Controladores;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
public class CVisualizarHistorialMedico implements ControladorInyectable {
    private CMainVentana mainController;
    // Panes principales
    @FXML private Pane paneFondo;
    @FXML private Pane paneFrente;

    // TableView y sus columnas
    @FXML private TableView<?> tableHistorialCitas;  // Cambia el <?> por tu modelo si lo tienes

    @FXML private TableColumn<?, ?> columnIdHistorial;
    @FXML private TableColumn<?, ?> columnFechaHistorial;
    @FXML private TableColumn<?, ?> columnDiagnosticoHistorial;

    // Etiquetas
    @FXML private Label labelHistorialClinico;

    // Botones
    @FXML private Button buttonRegresarAlMenu;
    @FXML private Button buttonVerDetalles;

    // Función para asignar el controlador del contenedor principal
    public void setMainController(CMainVentana mainController) {
        this.mainController = mainController;
    }
    // Método que se ejecuta automáticamente después de cargar el FXML
    @FXML
    private void initialize() {

    }

    @FXML
    private void clickRegresarAlMenu() {
        mainController.volverAlMenuPrincipal("Paciente");
    }

    @FXML
    private void clickVerDetalles() {

    }
}
