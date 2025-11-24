package gestion.gestion_citas_medicas.Controladores;

import gestion.gestion_citas_medicas.ClasesNormales.*;
import gestion.gestion_citas_medicas.ClasesSQL.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;

import java.util.List;

// Ajusta según tu modelo de datos


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
        cargarInformacionDoctor();
        cargarEspecialidades();
        cargarDoctores();
    }

    @FXML
    public void cargarInformacionDoctor() {
        textfieldNombreOp.setEditable(false);
        textfieldNombreOp.setEditable(false);
        Operador o = SessionManager.getPerfil(Operador.class);
        String nombre = o.getNombre() + " " +o.getApellido();
        textfieldNombreOp.setText(nombre);
    }

    public void cargarEspecialidades() {

        try {
            EspecialidadSQL espSQL = new EspecialidadSQL();
            List<Especialidad> lista = espSQL.findAll();

            // Convertimos a ObservableList<String>
            ObservableList<String> nombres =
                    FXCollections.observableArrayList();

            for (Especialidad e : lista) {
                nombres.add(e.getNombre());
            }

            choiceboxEspecialidadOp.setItems(nombres);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error cargando especialidades: " + e.getMessage());
        }
    }

    public void cargarDoctores() {

        try {
            DoctorSQL doctorSQL = new DoctorSQL();
            List<Doctor> lista = doctorSQL.findAll();

            ObservableList<String> nombres = FXCollections.observableArrayList();

            for (Doctor d : lista) {
                nombres.add(d.getNombre() + " " + d.getApellido());
            }

            choiceboxDoctorOp.setItems(nombres);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error cargando doctores: " + e.getMessage());
        }
    }



    public void clickAsignarDoctor(ActionEvent actionEvent) {
    }

    public void clickRegresarAlMenu(ActionEvent actionEvent) {
        mainController.volverAlMenuPrincipal("Operador");
    }
}
