package gestion.gestion_citas_medicas.Controladores;
import gestion.gestion_citas_medicas.ClasesNormales.Operador;
import gestion.gestion_citas_medicas.ClasesNormales.Paciente;
import gestion.gestion_citas_medicas.ClasesNormales.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class CMenuOperador implements ControladorInyectable {

    private CMainVentana mainController;
    // Panes principales
    @FXML private Pane paneFondo;
    @FXML private Pane paneFrente;

    // Labels
    @FXML private Label labelBienvenidaOp;
    @FXML private Label labelNombreOp;
    @FXML private Label labelEspecialidadOp;

    // TextFields
    @FXML private TextField textfieldNombreOp;

    // Botones
    @FXML private Button buttonActualizarDatos;
    @FXML private Button buttonGestionarCitas;
    @FXML private Button buttonRegresarAlLogin;


    @Override
    public void setMainController(CMainVentana mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void initialize() {
        cargarInformacionOp();
    }
    @FXML
    private void cambiarContenidoPanel(ActionEvent event) {
        Button botonPresionado = (Button) event.getSource();
        String botonId = botonPresionado.getId();
        String fxmlPath = obtenerFXMLsegunBoton(botonId);

        if (mainController == null) {
            System.err.println("Error: No se ha establecido la referencia al CMainVentana.");
            return;
        }

        if (fxmlPath.equals("Error")) {
            System.err.println("Error: No se encontró ruta FXML para el botón: " + botonId);
            return;
        }
        try {
            mainController.loadAndSetCenter(fxmlPath, (botonId.equals("buttonActualizarDatos")) ? "Operador" : null);
        }
        catch (IOException e) {
            System.err.println("Error al cargar el FXML: " + fxmlPath);
            e.printStackTrace();
        }
    }

    private String obtenerFXMLsegunBoton(String botonId) {
        return switch (botonId) {
            case "buttonActualizarDatos" -> "/Ventanas/ActualizarInfo.fxml";
            case "buttonGestionarCitas" -> "/Ventanas/GestionarCitasOp.fxml";
            default -> "Error";
        };
    }
    @FXML
    private void clickRegresarALogin(ActionEvent event) {
        mainController.regresarALogin(event);
    }
    public void cargarInformacionOp() {
        textfieldNombreOp.setEditable(false);
        Operador op = SessionManager.getPerfil(Operador.class);
        String nombre = op.getNombre() + " " + op.getApellido();
        textfieldNombreOp.setText(nombre);
    }

}
