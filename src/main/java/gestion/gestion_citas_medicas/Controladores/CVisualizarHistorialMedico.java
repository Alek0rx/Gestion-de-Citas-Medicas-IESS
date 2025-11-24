package gestion.gestion_citas_medicas.Controladores;

import gestion.gestion_citas_medicas.ClasesNormales.DetalleTratamiento;
import gestion.gestion_citas_medicas.ClasesNormales.Historial_Medico;
import gestion.gestion_citas_medicas.ClasesNormales.Paciente;
import gestion.gestion_citas_medicas.ClasesNormales.SessionManager;
import gestion.gestion_citas_medicas.Logica.HistorialService; // Nuevo servicio
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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

    @FXML
    public void clickVerDetalles(ActionEvent actionEvent) {
        // 1. Obtener el historial seleccionado
        Historial_Medico historialSeleccionado = tableHistorialCitas.getSelectionModel().getSelectedItem();

        if (historialSeleccionado == null) {
            JOptionPane.showMessageDialog(null, "Por favor, seleccione un registro de historial.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // 2. Obtener los detalles del tratamiento/receta
            DetalleTratamiento detalles = historialService.obtenerDetallesTratamiento(
                    historialSeleccionado.getIdHistorial()
            );

            // 3. Mostrar los detalles en un Alert
            mostrarDetallesAlert(detalles);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al cargar los detalles: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Construye y muestra un Alert de JavaFX con los detalles del tratamiento y la receta.
     */
    private void mostrarDetallesAlert(DetalleTratamiento detalles) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Detalles de Tratamiento y Receta");
        alert.setHeaderText("Historial registrado el " + detalles.getFechaInicio());

        StringBuilder contenido = new StringBuilder();

        // --- Tratamiento ---
        contenido.append("### TRATAMIENTO ASIGNADO ###\n");
        contenido.append("Descripción: ").append(detalles.getDescripcionTratamiento()).append("\n");
        contenido.append("Inicio: ").append(detalles.getFechaInicio()).append("\n");

        // La Fecha Fin puede ser nula en la DB
        String fechaFin = (detalles.getFechaFin() != null) ? detalles.getFechaFin().toString() : "Indefinido";
        contenido.append("Fin: ").append(fechaFin).append("\n\n");

        // --- Receta (Opcional) ---
        if (detalles.getNombreMedicamento() != null && !detalles.getNombreMedicamento().isEmpty()) {
            contenido.append("### RECETA MÉDICA ###\n");
            contenido.append("Medicamento: ").append(detalles.getNombreMedicamento()).append("\n");
            contenido.append("Indicaciones: ").append(detalles.getIndicacionesReceta()).append("\n");
        } else {
            contenido.append("### RECETA MÉDICA ###\n");
            contenido.append("No se asignó receta de medicamentos.\n");
        }

        // Establecer el contenido del Alert (usando TextArea para mejor formato)
        TextArea textArea = new TextArea(contenido.toString());
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);

        alert.getDialogPane().setContent(textArea);

        alert.showAndWait();
    }
}
