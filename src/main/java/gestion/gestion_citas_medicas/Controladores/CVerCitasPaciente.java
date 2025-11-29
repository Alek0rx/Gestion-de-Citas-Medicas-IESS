package gestion.gestion_citas_medicas.Controladores;
import gestion.gestion_citas_medicas.ClasesNormales.Paciente;
import gestion.gestion_citas_medicas.ClasesNormales.SessionManager;
import gestion.gestion_citas_medicas.Logica.CitaService;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import gestion.gestion_citas_medicas.ClasesNormales.Cita_Medica;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;


public class CVerCitasPaciente implements ControladorInyectable {
    private CMainVentana mainController;

    private CitaService citaService = new CitaService();
    // Panes principales
    @FXML private Pane paneFondo;
    @FXML private Pane paneFrente;

    // TableView y sus columnas
    @FXML private TableView<Cita_Medica> tableCitas;

    @FXML private TableColumn<Cita_Medica, LocalDate> columnFechaCita;
    @FXML private TableColumn<Cita_Medica, String> columnHoraCita;
    @FXML private TableColumn<Cita_Medica, String> columnEspecialidad;
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
    public void initialize() {
        columnFechaCita.setCellValueFactory(new PropertyValueFactory<>("fechaCita"));
        columnFechaCita.setCellFactory(column -> {
            return new TableCell<Cita_Medica, LocalDate>() {
                // Define el formato que deseas (ej: AAAA-MM-DD)
                private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                @Override
                protected void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        setText(null);
                    } else {
                        // Convierte el objeto LocalDate a String formateado
                        setText(formatter.format(item));
                    }
                }
            };
        });
        columnHoraCita.setCellValueFactory(new PropertyValueFactory<>("horaRango"));
        columnEspecialidad.setCellValueFactory(new PropertyValueFactory<>("especialidadNombre"));
        columnDoctorCita.setCellValueFactory(new PropertyValueFactory<>("nombreDoctor"));
        columnEstadoCita.setCellValueFactory(new PropertyValueFactory<>("estado"));

        cargarCitasPaciente();

        deshabilitarBotonesAccion(true);
        tableCitas.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean isSelected = newSelection != null;
            deshabilitarBotonesAccion(!isSelected);
        });
    }

    private void cargarCitasPaciente() {
        try {
            int idPaciente = SessionManager.getPerfil(Paciente.class).getIdPaciente();
            System.out.println(idPaciente);

            List<Cita_Medica> citas = citaService.obtenerCitasParaTabla(idPaciente);
            tableCitas.getItems().setAll(citas);
        }
        catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                    null,
                    "Error al cargar citas: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void deshabilitarBotonesAccion(boolean deshabilitar) {
        buttonCancelarCita.setDisable(deshabilitar);
        buttonReprogramarCita.setDisable(deshabilitar);
    }

    @FXML
    private void clickCancelarCita() {
        Cita_Medica citaSeleccionada = tableCitas.getSelectionModel().getSelectedItem();

        if (citaSeleccionada != null) {
            // diálogo de confirmación
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmar Cancelación");
            alert.setHeaderText("¿Está seguro de que desea cancelar esta cita?");
            alert.setContentText("Cita para: " + citaSeleccionada.getEspecialidadNombre() +
                    " el " + citaSeleccionada.getFechaCita().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) +
                    " a las " + citaSeleccionada.getHoraRango());


            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                // El usuario aceptó la cancelación
                try {
                    // Llama al servicio para cambiar el estado a "Cancelado"
                    citaService.cancelarCita(citaSeleccionada.getIdCita());

                    // 3. Actualizar la interfaz (se actualizará el estado de la cita en la tabla)
                    cargarCitasPaciente();

                    // Mostrar éxito
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Éxito");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("La cita ha sido cancelada exitosamente.");
                    successAlert.showAndWait();

                } catch (Exception e) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Error de Cancelación");
                    errorAlert.setHeaderText("No se pudo cancelar la cita.");
                    errorAlert.setContentText("Detalles: " + e.getMessage());
                    errorAlert.showAndWait();
                }
            }
            else {
                tableCitas.getSelectionModel().clearSelection();
            }
        }
    }


    @FXML
    private void clickReprogramarCita() {
        Cita_Medica citaSeleccionada = tableCitas.getSelectionModel().getSelectedItem();

        if (citaSeleccionada != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Ventanas/ReprogramarCita.fxml"));
                Pane root = loader.load();
                CReprogramarCita dialogController = loader.getController();

                dialogController.setCitaData(citaSeleccionada, citaService);

                Stage stage = new Stage();
                stage.setTitle("Reprogramar Cita");
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();


                cargarCitasPaciente();

            }
            catch (IOException e) {
                // Manejar error de carga de FXML
                Alert errorAlert = new Alert(Alert.AlertType.ERROR, "No se pudo cargar la ventana de reprogramación.");
                errorAlert.showAndWait();
            }

        }
    }



    @FXML
    private void clickRegresarAlMenu() {
        mainController.volverAlMenuPrincipal("Paciente");
    }

}

