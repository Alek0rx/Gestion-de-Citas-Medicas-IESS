package gestion.gestion_citas_medicas.Controladores;
import gestion.gestion_citas_medicas.ClasesNormales.*;
import gestion.gestion_citas_medicas.ClasesSQL.Cita_MedicaSQL;
import gestion.gestion_citas_medicas.ClasesSQL.HorarioSQL;
import gestion.gestion_citas_medicas.ClasesSQL.PacienteSQL;
import gestion.gestion_citas_medicas.Logica.VerCitas;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;

import javax.swing.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

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


    public List<Cita_Medica> recolectarCitasDoctor() throws Exception {
        Doctor doctorLogueado = SessionManager.getPerfil(Doctor.class);
        if (doctorLogueado == null) {
            return new ArrayList<>();
        }
        int idDoctor = doctorLogueado.getIdDoctor();
        Cita_MedicaSQL citaSQL = new Cita_MedicaSQL();
        return citaSQL.findByDoctor(idDoctor);
    }

    public void iniciarTabla() {

        // Configurar columnas
        columnIdCitaDoctor.setCellValueFactory(new PropertyValueFactory<>("idCita"));

        columnFechaCitaDoctor.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFechaCita().toString())
        );

        columnEstadoCitaDoctor.setCellValueFactory(new PropertyValueFactory<>("estado"));

        // Hora desde HorarioSQL
        HorarioSQL horarioSQL = new HorarioSQL();
        columnHoraCitaDoctor.setCellValueFactory(cellData -> {
            try {
                Horario h = horarioSQL.findById(cellData.getValue().getIdHorario());
                return new SimpleStringProperty(h.getHoraInicio().toString());
            } catch (Exception e) {
                return new SimpleStringProperty("N/A");
            }
        });

        // Paciente desde PacienteSQL
        PacienteSQL pacienteSQL = new PacienteSQL();
        columnPacienteDoctor.setCellValueFactory(cellData -> {
            try {
                Paciente p = pacienteSQL.findById(cellData.getValue().getIdPaciente());
                return new SimpleStringProperty(p.getNombre());
            } catch (Exception e) {
                return new SimpleStringProperty("N/A");
            }
        });

        // Llenar tabla
        try {
            List<Cita_Medica> citas = recolectarCitasDoctor();
            tableCMD.setItems(FXCollections.observableArrayList(citas));
        } catch (Exception e) {
            e.printStackTrace();
        }
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

}
