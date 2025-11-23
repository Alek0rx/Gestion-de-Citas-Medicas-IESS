package gestion.gestion_citas_medicas.Controladores;

import gestion.gestion_citas_medicas.ClasesNormales.Usuario;
import gestion.gestion_citas_medicas.Logica.UsuarioService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;

public class CLogin {
    private UsuarioService usuarioService =  new UsuarioService();

    // Panes principales
    @FXML private Pane paneFondo;
    @FXML private Pane paneFrente;

    // Campos de entrada
    @FXML private TextField textfieldUsuarioLogin;
    @FXML private PasswordField passwordfieldUsuarioLogin;

    // ChoiceBox para seleccionar tipo de usuario
    @FXML private ChoiceBox<String> choiceBoxRolLogin;

    // Labels
    @FXML private Label labelBienvenidaLogin;
    @FXML private Label labelUsuarioLogin;
    @FXML private Label labelPasswordLogin;
    @FXML private Label labelRolLogin;

    // Botón
    @FXML private Button buttonSesionLogin;
    @FXML
    public void initialize() {
        choiceBoxRolLogin.getItems().addAll("Administrador", "Doctor", "Paciente", "Operador");
        choiceBoxRolLogin.setValue("Paciente");
    }

    @FXML
    private void clickIniciarSesion(ActionEvent event) {

        String user = textfieldUsuarioLogin.getText();
        String password = passwordfieldUsuarioLogin.getText();
        String rol = choiceBoxRolLogin.getValue();

        try {
            System.out.println("Usuario login: " + user);
            System.out.println("Password login: " + password);
            System.out.println("Rol login: " + rol);
            Usuario usuario = usuarioService.iniciarSesion(user, password, rol);

            usuarioService.establecerUsuario(usuario);
            JOptionPane.showMessageDialog(null,
                    "Inicio de Sesión Exitoso",
                    "¡Exito!",
                    JOptionPane.INFORMATION_MESSAGE
                    );
            cargarPantalla(rol,event);
        }
        catch (IllegalArgumentException iae) {
            JOptionPane.showMessageDialog(null, iae.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (RuntimeException re) {
            // aquí verás mensajes de fallo real (perfil no encontrado, BD, cast, etc.)
            JOptionPane.showMessageDialog(null, re.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            // caso extremo: excepción que declaraste en la firma
            JOptionPane.showMessageDialog(null, "Error inesperado: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

//        catch (IllegalArgumentException e) {
//            JOptionPane.showMessageDialog(
//                    null, "Error, escriba credenciales válidas", "Error", JOptionPane.ERROR_MESSAGE);
//        }
//
//        catch (Exception e) {
//            JOptionPane.showMessageDialog(
//                    null, "Error, usuario, contraseña y/o rol incorrectos", "Error", JOptionPane.ERROR_MESSAGE);
//        }
    }

    private void cargarPantalla(String rol, ActionEvent event) {
        try {
            // Cargar el Marco Principal (El contenedor vacío)
            FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/Ventanas/MainVentana.fxml"));
            Parent mainRoot = mainLoader.load();

            // Obtener el controlador del Marco Principal
            CMainVentana mainController = mainLoader.getController();

            // Decirle al Marco Principal qué vista interna cargar (Paciente, Admin, etc.)
            String fxmlRol = obtenerFXMLsegunRol(rol);
            mainController.loadAndSetCenter(fxmlRol, null);

            //  Crear la nueva Escena y Ventana (Stage)
            Stage mainStage = new Stage();
            mainStage.setScene(new Scene(mainRoot));
            mainStage.setTitle("Sistema IESS - Panel de " + rol);
            mainStage.show();

            // Cerrar la ventana de Login actual
            Stage loginStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            loginStage.close();

        }
        catch (IOException e) {
            System.err.println("Error critico al cargar la ventana principal: " + e.getMessage());
        }
    }

    private String obtenerFXMLsegunRol(String rol) {
        return switch (rol) {
            case "Administrador" -> "/Ventanas/MenuAdmin.fxml";
            case "Doctor" -> "/Ventanas/MenuDoctor.fxml";
            case "Paciente" -> "/Ventanas/MenuPaciente.fxml";
            case "Operador" -> "/Ventanas/MenuOperador.fxml";
            default -> null;
        };
    }
}