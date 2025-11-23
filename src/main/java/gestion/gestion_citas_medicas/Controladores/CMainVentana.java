package gestion.gestion_citas_medicas.Controladores;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class CMainVentana {

    @FXML
    private StackPane mainContenedor;

    // Este método es el corazón de la navegación
    public void loadAndSetCenter(String fxmlPath, String rol) throws IOException {
        if (fxmlPath == null) return;

        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Node nuevaVista = loader.load();

        Object controller = loader.getController();

        if (controller instanceof ControladorInyectable) {
            if (controller instanceof CActualizarInfo && rol != null) {
                System.out.println("Rol: " + rol);
                ((CActualizarInfo) controller).inicializarDatos(rol);
            }
            ((ControladorInyectable) controller).setMainController(this);
        }


        mainContenedor.getChildren().clear();
        mainContenedor.getChildren().add(nuevaVista);
    }
    @FXML
    public void regresarALogin(ActionEvent event) {
        try {
            Node source = (Node) event.getSource();
            Stage stageActual = (Stage) source.getScene().getWindow();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Ventanas/Login.fxml"));
            Parent root = loader.load();

            Stage nuevaStage = new Stage();
            Scene scene = new Scene(root);

            nuevaStage.setScene(scene);
            nuevaStage.setTitle("Inicio de Sesión");
            nuevaStage.show();

            stageActual.close();

        }
        catch (IOException e) {
            System.err.println("Error al cargar la ventana de Login.");
            e.printStackTrace();
        }
    }

    public void volverAlMenuPrincipal(String rol) {

        String fxmlPath = switch (rol) {
            case "Paciente" -> "/Ventanas/MenuPaciente.fxml";
            case "Doctor" -> "/Ventanas/MenuDoctor.fxml";
            case "Administrador" -> "/Ventanas/MenuAdmin.fxml";
            case "Operador" -> "/Ventanas/MenuOperador.fxml";
            default -> null;
        };

        if (fxmlPath != null) {
            try {
                loadAndSetCenter(fxmlPath, null);
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Error al intentar volver al menú del rol: " + rol);
            }
        } else {
            System.err.println("Error: Rol no reconocido (" + rol + ")");
        }
    }
}
