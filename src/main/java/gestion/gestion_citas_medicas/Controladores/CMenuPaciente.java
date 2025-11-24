package gestion.gestion_citas_medicas.Controladores;

import gestion.gestion_citas_medicas.ClasesNormales.Paciente;
import gestion.gestion_citas_medicas.ClasesNormales.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class CMenuPaciente implements ControladorInyectable {
    private CMainVentana mainController;

    // textfield
    @FXML public TextField textfieldPacienteNombre;

    // labels
    @FXML public Label labelPacienteNombre;
    @FXML public Label labelBienvenidaPaciente;

    // botones
    @FXML public Button buttonAgendarCita;
    @FXML public Button buttonActualizarDatos;
    @FXML public Button buttonVisualizarHistorial;
    @FXML public Button buttonVerCitas;
    @FXML public Button buttonRegresarLogin;

    // panes
    @FXML public Pane paneFrente;
    @FXML public Pane paneFondo;



    @Override
    public void setMainController(CMainVentana mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void initialize(){

        cargarInformacionPaciente();
    }

    private String obtenerFXMLsegunBoton(String botonId) {
        return switch (botonId) {
            case "buttonAgendarCita" -> "/Ventanas/AgendarCita.fxml";
            case "buttonActualizarDatos" -> "/Ventanas/ActualizarInfo.fxml";
            case "buttonVisualizarHistorial" -> "/Ventanas/VisualizarHistorialMedico.fxml";
            case "buttonVerCitas" -> "/Ventanas/VerCitasPaciente.fxml";
            default -> "Error";
        };
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
            mainController.loadAndSetCenter(fxmlPath, (botonId.equals("buttonActualizarDatos")) ? "Paciente" : null);
        }
        catch (IOException e) {
            System.err.println("Error al cargar el FXML: " + fxmlPath);
            e.printStackTrace();
        }
    }

    @FXML
    private void regresarALogin(ActionEvent event) {
        mainController.regresarALogin(event);
    }

    public void cargarInformacionPaciente() {
        textfieldPacienteNombre.setEditable(false);
        Paciente p = SessionManager.getPerfil(Paciente.class);
        String nombre = p.getNombre() + " " + p.getApellido();
        textfieldPacienteNombre.setText(nombre);
    }
}
