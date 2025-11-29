package gestion.gestion_citas_medicas.Controladores;
import gestion.gestion_citas_medicas.ClasesNormales.*;
import gestion.gestion_citas_medicas.ClasesSQL.Cita_MedicaSQL;
import gestion.gestion_citas_medicas.ClasesSQL.HorarioSQL;
import gestion.gestion_citas_medicas.ClasesSQL.PacienteSQL;
import gestion.gestion_citas_medicas.Logica.VerCitas;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CVerCitasDoctor implements ControladorInyectable {
    public Button buttonAsignarTratamiento;
    private CMainVentana mainController;
    private final VerCitas servicioCitas = new VerCitas();


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

    // Botones
    @FXML private Button buttonRegresarAlMenu;
    @FXML public Button buttonCrearEstadoCita;
    @FXML public Button buttonCrearHistorial;
    @FXML private Button buttonVerDetallesCita;

    @Override
    public void setMainController(CMainVentana mainController) {
        this.mainController = mainController;
        cargarInformacionEspecialidadDoctor();
        iniciarTabla();
    }

    @FXML
    private void clickRegresarAlMenu() {
        mainController.volverAlMenuPrincipal("Doctor");
    }

    @FXML
    public void cargarInformacionEspecialidadDoctor() {
        textfieldEspecialidadCMD.setEditable(false);
        textfieldEspecialidadCMD.setEditable(false);
        Doctor d = SessionManager.getPerfil(Doctor.class);
        textfieldEspecialidadCMD.setText(ElementosEstaticos.getEspecialidadPorId(d.getIdEspecialidad()));
    }



    // Dentro de CVerCitasDoctor

    public void iniciarTabla() {
        columnIdCitaDoctor.setCellValueFactory(new PropertyValueFactory<>("idCita"));

        columnFechaCitaDoctor.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFechaCita().toString())
        );

        columnEstadoCitaDoctor.setCellValueFactory(new PropertyValueFactory<>("estado"));

        columnHoraCitaDoctor.setCellValueFactory(cellData -> {
            int idHorario = cellData.getValue().getIdHorario();
            String horarioTexto = ElementosEstaticos.getHorarioPorId(idHorario);
            return new SimpleStringProperty(horarioTexto != null ? horarioTexto : "N/A");
        });

        // Paciente: AHORA LLAMA AL SERVICIO
        columnPacienteDoctor.setCellValueFactory(cellData ->
                new SimpleStringProperty(servicioCitas.obtenerNombrePaciente(cellData.getValue().getIdPaciente()))
        );

        // Carga de datos: AHORA LLAMA AL SERVICIO
        try {
            List<Cita_Medica> citas = servicioCitas.obtenerCitasDoctor();
            tableCMD.setItems(FXCollections.observableArrayList(citas));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Configuración del Listener y botón de historial (Esto se queda en el controlador)
        tableCMD.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            actualizarEstadoBotonHistorial(newSelection);
            actualizarEstadoBotonCambiarEstado(newSelection);
        });

        buttonCrearEstadoCita.setDisable(true);
        buttonCrearHistorial.setDisable(true);
    }




    VerCitas c = new VerCitas();
    @FXML
    private void clickVerDetallesCita() {
        Cita_Medica citaSeleccionada = tableCMD.getSelectionModel().getSelectedItem();

        if (citaSeleccionada == null) {
            JOptionPane.showMessageDialog(null,
                    "Por favor, seleccione una cita de la tabla.",
                    "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String mensaje = c.generarMensajeDetalles(citaSeleccionada);
            c.mostrarMensaje(mensaje);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Ocurrió un error al cargar los detalles.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }


    public void actualizarEstadoBotonHistorial(Cita_Medica citaSeleccionada) {
        if (citaSeleccionada != null && "realizada".equalsIgnoreCase(citaSeleccionada.getEstado())) {
            buttonCrearHistorial.setDisable(false);
        } else {
            buttonCrearHistorial.setDisable(true);
        }
    }


    @FXML
    public void clickCambiarEstadoCita(ActionEvent actionEvent) {
        Cita_Medica citaSeleccionada = tableCMD.getSelectionModel().getSelectedItem();
        // ... (validaciones de null y estado 'pendiente' se mantienen) ...

        if ("pendiente".equalsIgnoreCase(citaSeleccionada.getEstado())) {
            int opcion = JOptionPane.showConfirmDialog(
                    null,
                    "¿Desea marcar la cita como 'realizada'?",
                    "Confirmar cambio de estado",
                    JOptionPane.YES_NO_OPTION);

            if (opcion == JOptionPane.YES_OPTION) {
                try {
                    // AHORA LLAMA AL SERVICIO para la actualización
                    servicioCitas.actualizarEstadoCita(citaSeleccionada.getIdCita(), "realizada");

                    citaSeleccionada.setEstado("realizada");
                    iniciarTabla(); // Refrescar la tabla

                    JOptionPane.showMessageDialog(null,
                            "Estado de la cita actualizado a 'realizada'.",
                            "Éxito", JOptionPane.INFORMATION_MESSAGE);

                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null,
                            "Error al actualizar el estado de la cita en la base de datos: " + e.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    public void actualizarEstadoBotonCambiarEstado(Cita_Medica citaSeleccionada) {
        // Si la cita no es nula Y su estado es "pendiente" (ignorando mayúsculas/minúsculas)
        if (citaSeleccionada != null && "pendiente".equalsIgnoreCase(citaSeleccionada.getEstado())) {
            buttonCrearEstadoCita.setDisable(false);
        } else {
            buttonCrearEstadoCita.setDisable(true);
        }
    }



// Dentro de CVerCitasDoctor.java

    @FXML
    public void clickCrearHistorial(ActionEvent actionEvent) {
        Cita_Medica citaSeleccionada = tableCMD.getSelectionModel().getSelectedItem();

        // 1. Validación de cita seleccionada y estado "realizada"
        if (citaSeleccionada == null || !"realizada".equalsIgnoreCase(citaSeleccionada.getEstado())) {
            JOptionPane.showMessageDialog(null,
                    "Solo se puede crear el historial de citas 'realizadas'.",
                    "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 2. Lógica para cargar el modal
        try {
            // Llamada al método del controlador principal para cargar la ventana modal
            // y pasar la cita seleccionada.
            mainController.cargarCreacionHistorial(citaSeleccionada);


            iniciarTabla();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Error al intentar abrir la ventana de creación de historial.",
                    "Error de Carga", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void clickCrearTratamiento(ActionEvent actionEvent) {
        Cita_Medica citaSeleccionada = tableCMD.getSelectionModel().getSelectedItem();

        // 1. Validar selección y estado
        // Solo se debe poder asignar un tratamiento si la cita fue 'realizada'
        if (citaSeleccionada == null || !"realizada".equalsIgnoreCase(citaSeleccionada.getEstado())) {
            JOptionPane.showMessageDialog(null,
                    "Debe seleccionar una cita con estado 'realizada' para asignar un tratamiento.",
                    "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // 2. Llamar al controlador principal para cargar el modal
            // La lógica del modal se delega al CMainVentana.
            mainController.cargarCreacionTratamiento(citaSeleccionada);

            // Opcional: Refrescar la tabla si la asignación del tratamiento afecta el estado de la cita
            // iniciarTabla();

        } catch (
                IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Error al intentar abrir la ventana de asignación de tratamiento.",
                    "Error de Carga", JOptionPane.ERROR_MESSAGE);
        }
    }
}

