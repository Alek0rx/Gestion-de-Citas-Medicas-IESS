package gestion.gestion_citas_medicas.Controladores;
import gestion.gestion_citas_medicas.ClasesNormales.*;
import gestion.gestion_citas_medicas.ClasesSQL.HorarioSQL;
import gestion.gestion_citas_medicas.ClasesSQL.PacienteSQL;
import gestion.gestion_citas_medicas.Logica.crearMensaje;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

import javax.swing.*;

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
        cargarInformacionEspecialidadDoctor();
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

crearMensaje c = new crearMensaje();
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

}
