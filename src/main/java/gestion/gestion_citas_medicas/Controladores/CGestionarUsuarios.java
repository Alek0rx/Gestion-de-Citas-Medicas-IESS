package gestion.gestion_citas_medicas.Controladores;

import gestion.gestion_citas_medicas.ClasesNormales.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

public class CGestionarUsuarios implements ControladorInyectable {

    // Panes principales
    @FXML private Pane paneFondo;
    @FXML private Pane paneFrente;

    // Labels
    @FXML private Label labelBienvenidaAdmin;
    @FXML private Label labelRolAdmin;
    @FXML private Label labelEspecialidadAdmin;
    @FXML private Label labelBuscarUsuarioAdmin;
    @FXML private Label labelCriterioBusquedaAdmin;

    // Campo de búsqueda
    @FXML private TextField textfieldBuscarUsuarioAdmin;

    // ChoiceBoxes
    @FXML private ChoiceBox<String> choiceboxCriterioBusquedaAdmin;
    @FXML public ChoiceBox<String> choiceboxRolAdmin;
    @FXML public ChoiceBox<String> choiceboxEspecialidadAdmin;


    // Tabla de usuarios
    @FXML private TableView<Usuario> tableUsuariosAdmin;
    @FXML private TableColumn<Usuario, Integer> columnIdUsuario;
    @FXML private TableColumn<Usuario, String>  columnUsuarioAdmin;
    @FXML private TableColumn<Usuario, String>  columnPasswordAdmin;
    @FXML private TableColumn<Usuario, String>  columnEstadoAdmin;
    @FXML private TableColumn<Usuario, String>  columnNombreApellidoAdmin;

    // Botones
    @FXML private Button buttonAgregarUsuario;
    @FXML private Button buttonEliminarUsuario;
    @FXML private Button buttonRegresarAlMenu;

    private CMainVentana mainController;

    @Override
    public void setMainController(CMainVentana mainController) {
        this.mainController = mainController;
    }

    public void clickAgregarUsuario(ActionEvent actionEvent) {
    }

    public void clickRegresarAlMenu(ActionEvent actionEvent) {
        mainController.volverAlMenuPrincipal("Administrador");
    }

    public void clickEliminarUsuario(ActionEvent actionEvent) {
    }
}
