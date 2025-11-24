package gestion.gestion_citas_medicas.Controladores;

import gestion.gestion_citas_medicas.ClasesNormales.Historial_Medico;
import gestion.gestion_citas_medicas.ClasesNormales.Paciente;
import gestion.gestion_citas_medicas.ClasesNormales.SessionManager;
import gestion.gestion_citas_medicas.Logica.HistorialService; // Nuevo servicio
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.List;
import javax.swing.JOptionPane;

public class CVisualizarHistorialMedico implements ControladorInyectable {

    private CMainVentana mainController;
    private final HistorialService historialService = new HistorialService(); // Usamos el servicio de historial

    @FXML private TableView<Historial_Medico> tableHistorialCitas;
    @FXML private TableColumn<Historial_Medico, Integer> columnIdHistorial;
    @FXML private TableColumn<Historial_Medico, String> columnFechaHistorial;
    @FXML private TableColumn<Historial_Medico, String> columnDiagnosticoHistorial;
    @FXML private Button buttonVerDetalles;

    @Override
    public void setMainController(CMainVentana mainController) {
        this.mainController = mainController;
        iniciarTabla(); // Cargar datos al iniciar la vista
    }

    private void iniciarTabla() {
        columnIdHistorial.setCellValueFactory(new PropertyValueFactory<>("idHistorial"));
        columnFechaHistorial.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFechaRegistro().toString())
        );
        columnDiagnosticoHistorial.setCellValueFactory(new PropertyValueFactory<>("diagnostico"));
        try {
            // Obtener el ID del paciente logueado
            Paciente paciente = SessionManager.getPerfil(Paciente.class);
            int idPaciente = paciente.getIdPaciente();

            // Llamar a la capa de servicio
            List<Historial_Medico> historiales = historialService.obtenerHistorialPorPaciente(idPaciente);

            tableHistorialCitas.setItems(FXCollections.observableArrayList(historiales));

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Error al cargar el historial: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @FXML
    private void clickRegresarAlMenu() {
        mainController.volverAlMenuPrincipal("Paciente");
    }

    public void clickVerDetalles(ActionEvent actionEvent) {

    }
}
