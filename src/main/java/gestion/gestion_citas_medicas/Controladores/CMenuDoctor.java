package gestion.gestion_citas_medicas.Controladores;
import gestion.gestion_citas_medicas.ClasesNormales.Doctor;
import gestion.gestion_citas_medicas.ClasesNormales.ElementosEstaticos;
import gestion.gestion_citas_medicas.ClasesNormales.Paciente;
import gestion.gestion_citas_medicas.ClasesNormales.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class CMenuDoctor implements ControladorInyectable {



    private CMainVentana mainController;

    // Panes principales
    @FXML private Pane paneFondo;
    @FXML private Pane paneFrente;

    // Labels
    @FXML private Label labelBienvenidaDoctor;
    @FXML private Label labelNombreDoctor;
    @FXML private Label labelEspecialidadDoctor;

    // TextFields (solo lectura o editables según necesites)
    @FXML private TextField textfieldNombreDoctor;
    @FXML private TextField textfieldEspecialidadDoctor;

    // Botones
    @FXML private Button buttonActualizarDatos;
    @FXML private Button buttonVerCitasDoctor;
    @FXML private Button buttonRegresarAlLogin;

    @Override
    public void setMainController(CMainVentana mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void initialize() {

        cargarInformacionDoctor();
    }

    @FXML
    private void cambiarContenidoPanel(ActionEvent event) {
        Button botonPresionado = (Button) event.getSource();
        String botonId = botonPresionado.getId();
        System.out.println("botonId: " + botonId);
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
            mainController.loadAndSetCenter(fxmlPath, (botonId.equals("buttonActualizarDatos")) ? "Doctor" : null);
        }
        catch (IOException e) {
            System.err.println("Error al cargar el FXML: " + fxmlPath);
            e.printStackTrace();
        }
    }

    private String obtenerFXMLsegunBoton(String botonId) {
        return switch (botonId) {
            case "buttonActualizarDatos" -> "/Ventanas/ActualizarInfo.fxml";
            case "buttonVerCitasDoctor" -> "/Ventanas/VerCitasDoctor.fxml";
            default -> "Error";
        };
    }
    @FXML
    private void clickRegresarALogin(ActionEvent event) {
        mainController.regresarALogin(event);
    }
    public void cargarInformacionDoctor() {
        textfieldNombreDoctor.setEditable(false);
        textfieldEspecialidadDoctor.setEditable(false);
        Doctor d = SessionManager.getPerfil(Doctor.class);
        String nombre = d.getNombre() + " " +d.getApellido();
        textfieldNombreDoctor.setText(nombre);
        textfieldEspecialidadDoctor.setText(ElementosEstaticos.getEspecialidadPorId(d.getIdEspecialidad()));
    }
}
