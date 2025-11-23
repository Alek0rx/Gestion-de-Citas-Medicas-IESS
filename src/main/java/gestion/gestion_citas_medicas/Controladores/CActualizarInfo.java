package gestion.gestion_citas_medicas.Controladores;
import gestion.gestion_citas_medicas.ClasesNormales.Doctor;
import gestion.gestion_citas_medicas.ClasesNormales.Operador;
import gestion.gestion_citas_medicas.ClasesNormales.Paciente;
import gestion.gestion_citas_medicas.ClasesNormales.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import java.time.LocalDate;

public class CActualizarInfo implements ControladorInyectable {

    private String rolUsuario;
    private CMainVentana mainController;
    @FXML private Pane paneFondo;
    @FXML private Pane paneFrente;
    @FXML private Label labelActualizarInfo;

    // Labels de los campos
    @FXML private Label labelNombreAI;
    @FXML private Label labelApellidoAI;
    @FXML private Label labelTelefonoAI;
    @FXML private Label labelCorreoAI;
    @FXML private Label labelDireccionAI;
    @FXML private Label labelGeneroAI;
    @FXML private Label labelFechaNacAI;
    @FXML private Label labelCedulaAI;

    // Campos de texto
    @FXML private TextField textfieldNombreAI;
    @FXML private TextField textfieldApellidoAI;
    @FXML private TextField textfieldTelefonoAI;
    @FXML private TextField textfieldCorreoAI;
    @FXML private TextField textfieldDireccionAI;
    @FXML private TextField textfieldGeneroAI;
    @FXML private TextField textfieldCedulaAI;

    // DatePicker
    @FXML private DatePicker selectorFechaAI;

    // Botones
    @FXML private Button buttonActualizarCredencialesAI;
    @FXML private Button buttonGuardarCambios;
    @FXML private Button buttonRegresarMenu;

    @FXML
    public void initialize() {

    }

    @Override
    public void setMainController(CMainVentana mainController) {
        this.mainController = mainController;
    }

    public void inicializarDatos(String rol) {
        this.rolUsuario = rol;
        textfieldCedulaAI.setEditable(false);
        textfieldGeneroAI.setEditable(false);
        selectorFechaAI.setEditable(false);
        cargarDatosUsuario();

    }

    @FXML
    private void clickGuardarCambios(ActionEvent e) {}

    @FXML
    private void clickRegresarMenu(ActionEvent e) {
        mainController.volverAlMenuPrincipal(rolUsuario);
    }
    @FXML
    private void clickActualizarCredenciales(ActionEvent e) {}

    public void cargarDatosUsuario () {
        Object perfil = switch (rolUsuario) {
            case "Paciente" -> SessionManager.getPerfil(Paciente.class);
            case "Doctor" -> SessionManager.getPerfil(Doctor.class);
            case "Operador" -> SessionManager.getPerfil(Operador.class);
            default -> null;
        };

        if (perfil != null) {
            cargarDatosGenericos(perfil);
        }
    }

    private void cargarDatosGenericos(Object perfil) {
        textfieldNombreAI.clear();
        textfieldApellidoAI.clear();
        textfieldTelefonoAI.clear();
        textfieldDireccionAI.clear();
        textfieldCorreoAI.clear();
        textfieldCedulaAI.clear();
        textfieldGeneroAI.clear();
        selectorFechaAI.setValue(null);

        if (perfil instanceof Paciente p) {

            textfieldNombreAI.setText(p.getNombre());
            textfieldApellidoAI.setText(p.getApellido());
            textfieldTelefonoAI.setText(p.getTelefono());
            textfieldDireccionAI.setText(p.getDireccion());
            textfieldCorreoAI.setText(p.getCorreo());
            textfieldCedulaAI.setText(p.getCedula());
            textfieldGeneroAI.setText(Character.toString(p.getGenero()));
            selectorFechaAI.setValue(p.getFechaNacimiento());

            return;
        }

        if (perfil instanceof Doctor d) {
            selectorFechaAI.setVisible(false);
            selectorFechaAI.setDisable(true);
            labelFechaNacAI.setVisible(false);
            labelGeneroAI.setVisible(false);
            textfieldGeneroAI.setVisible(false);

            textfieldNombreAI.setText(d.getNombre());
            textfieldApellidoAI.setText(d.getApellido());
            textfieldCorreoAI.setText(d.getCorreo());
            textfieldTelefonoAI.setText(d.getTelefono());
            textfieldDireccionAI.setText(d.getDireccion());
            textfieldCedulaAI.setText(d.getCedula());

            return;
        }

        if (perfil instanceof Operador op) {
            selectorFechaAI.setVisible(false);
            selectorFechaAI.setDisable(true);
            labelFechaNacAI.setVisible(false);
            labelGeneroAI.setVisible(false);
            textfieldGeneroAI.setVisible(false);

            textfieldDireccionAI.setText(op.getDireccion());
            textfieldCedulaAI.setText(op.getCedula());
            textfieldNombreAI.setText(op.getNombre());
            textfieldTelefonoAI.setText(op.getTelefono());
            textfieldApellidoAI.setText(op.getApellido());
            textfieldCorreoAI.setText(op.getCorreo());
        }
    }

}
