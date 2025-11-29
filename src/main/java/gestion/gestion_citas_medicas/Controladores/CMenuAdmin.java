package gestion.gestion_citas_medicas.Controladores;

import gestion.gestion_citas_medicas.ClasesNormales.*;
import gestion.gestion_citas_medicas.Logica.ActualizarDatos;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import javax.swing.*;
import java.io.IOException;

public class CMenuAdmin implements ControladorInyectable {
    private CMainVentana mainController;

    // Panes principales
    @FXML private Pane paneFondo;
    @FXML private Pane paneFrente;

    // Labels
    @FXML private Label labelBienvenidaAdmin;
    @FXML private Label labelUsuarioAdmin;

    // TextField con el nombre del administrador
    @FXML private TextField textfieldUsuarioAdmin;

    // Botones
    @FXML private Button buttonActualizarCredenciales;
    @FXML private Button buttonGestionarUsuarios;
    @FXML private Button buttonRegresarAlLogin;


    @Override
    public void setMainController(CMainVentana mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void initialize() {
        cargarInformacionAdmin();
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
            mainController.loadAndSetCenter(fxmlPath, null);
        }
        catch (IOException e) {
            System.err.println("Error al cargar el FXML: " + fxmlPath);
            e.printStackTrace();
        }
    }

    private String obtenerFXMLsegunBoton(String botonId) {
        return switch (botonId) {
            case "buttonActualizarDatos" -> "/Ventanas/ActualizarInfo.fxml";
            case "buttonGestionarUsuarios" -> "/Ventanas/GestionarUsuarios.fxml";
            default -> "Error";
        };
    }
    @FXML
    private void clickRegresarALogin(ActionEvent event) {
        mainController.regresarALogin(event);
    }
    public void cargarInformacionAdmin() {
        textfieldUsuarioAdmin.setEditable(false);
        textfieldUsuarioAdmin.setText(SessionManager.getUsuarioBase().getUsuario());
    }
    @FXML
    private void clickActualizarCredenciales(ActionEvent e) {
        //Ingresar las nuevas credenciales
        String nuevoUsuario= JOptionPane.showInputDialog("Ingrese el nuevo usuario: ");
        if(nuevoUsuario == null || nuevoUsuario.isBlank()){
            JOptionPane.showMessageDialog(null, "El usuario no puede estar vacío.");
            return;
        }

        String nuevaContrasena = JOptionPane.showInputDialog("Ingrese nueva contraseña: ");
        String confirmarContrasena = JOptionPane.showInputDialog("Confirme la nueva contraseña: ");

        if(nuevaContrasena == null || confirmarContrasena == null || nuevaContrasena.isBlank() || confirmarContrasena.isBlank()){
            JOptionPane.showMessageDialog(null, "La contraseña no puede estar vacía.");
            return;
        }

        if(!nuevaContrasena.equals(confirmarContrasena)){
            JOptionPane.showMessageDialog(null, "Las contraseñas no coinciden.");
            return;
        }

        Usuario perfil = SessionManager.getPerfil(Usuario.class);

        if(perfil == null){
            JOptionPane.showMessageDialog(null, "Error: no se encontró el perfil.");
            return;
        }

        //Actualiza las credenciales
        int idUsuario = perfil.getIdUsuario();

        boolean actualizado = ActualizarDatos.actualizarCredenciales(idUsuario, nuevoUsuario, nuevaContrasena);

        if (actualizado) {
            JOptionPane.showMessageDialog(null, "Credenciales actualizadas correctamente.");
        } else {
            JOptionPane.showMessageDialog(null, "No se pudieron actualizar las credenciales.");
        }

    }
}
