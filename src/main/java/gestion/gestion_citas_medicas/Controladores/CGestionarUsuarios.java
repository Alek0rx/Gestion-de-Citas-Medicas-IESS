package gestion.gestion_citas_medicas.Controladores;

import gestion.gestion_citas_medicas.ClasesNormales.SessionManager;
import gestion.gestion_citas_medicas.ClasesNormales.Usuario;
import gestion.gestion_citas_medicas.Logica.AdminService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class CGestionarUsuarios implements ControladorInyectable {

    // Panes
    @FXML public Pane paneFondo;
    @FXML public Pane paneFrente;
    @FXML public Label labelRolAdmin;

    // Labels
    @FXML public Label labelBienvenidaAdmin;
    @FXML public Label labelEspecialidadAdmin;
    @FXML public Label labelBuscarUsuarioAdmin;

    private AdminService adminService = new AdminService();

    // Listas para el filtrado en tiempo real
    private ObservableList<Usuario> masterData = FXCollections.observableArrayList();
    private FilteredList<Usuario> filteredData;

    // --- FXML ---
    @FXML private TextField textfieldBuscarUsuarioAdmin;
    @FXML private ChoiceBox<String> choiceboxRolAdmin;
    @FXML private ChoiceBox<String> choiceboxEspecialidadAdmin;

    // Tabla
    @FXML private TableView<Usuario> tableUsuariosAdmin;
    @FXML private TableColumn<Usuario, Integer> columnIdUsuario;
    @FXML private TableColumn<Usuario, String> columnUsuarioAdmin;
    @FXML private TableColumn<Usuario, String> columnPasswordAdmin;
    @FXML private TableColumn<Usuario, String> columnEstadoAdmin;
    @FXML private TableColumn<Usuario, String> columnNombreApellidoAdmin; // Esta mostrará el nombre combinado

    // Botones
    @FXML private Button btnMarcarEstado;
    @FXML private Button buttonRegresarAlMenu;
    @FXML private Button buttonEliminarUsuario;
    @FXML public Button buttonAgregarUsuario;

    private CMainVentana mainController;

    @Override
    public void setMainController(CMainVentana mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void initialize() {
        choiceboxEspecialidadAdmin.setDisable(true);
        columnIdUsuario.setCellValueFactory(new PropertyValueFactory<>("idUsuario"));
        columnUsuarioAdmin.setCellValueFactory(new PropertyValueFactory<>("usuario"));
        columnPasswordAdmin.setCellValueFactory(new PropertyValueFactory<>("password"));
        columnEstadoAdmin.setCellValueFactory(new PropertyValueFactory<>("estado"));
        columnNombreApellidoAdmin.setCellValueFactory(new PropertyValueFactory<>("nombreUsuario"));

        choiceboxRolAdmin.getItems().addAll("Todos", "Paciente", "Doctor", "Operador");
        choiceboxRolAdmin.setValue("Todos");

        cargarEspecialidades();
        choiceboxRolAdmin.valueProperty().addListener((obs, oldVal, newVal) -> {

            boolean esDoctor = "Doctor".equalsIgnoreCase(newVal);
            choiceboxEspecialidadAdmin.setDisable(!esDoctor);
            if (!esDoctor) {
                choiceboxEspecialidadAdmin.setValue("Todos");
            }

            actualizarFiltros();
        });

        choiceboxEspecialidadAdmin.valueProperty().addListener((observable, oldValue, newValue) -> {
            actualizarFiltros();
        });
        cargarDatosYConfigurarFiltros();
    }

    private void cargarDatosYConfigurarFiltros() {
        try {
            masterData.clear();
            masterData.addAll(adminService.obtenerUsuariosParaTabla());


            filteredData = new FilteredList<>(masterData, p -> true);

            textfieldBuscarUsuarioAdmin.textProperty().addListener((observable, oldValue, newValue) -> {
                actualizarFiltros();
            });

            // 3. Listener en el ComboBox de Rol
            choiceboxRolAdmin.valueProperty().addListener((observable, oldValue, newValue) -> {
                actualizarFiltros();
            });

            SortedList<Usuario> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(tableUsuariosAdmin.comparatorProperty());

            tableUsuariosAdmin.setItems(sortedData);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Esta función decide qué filas se muestran
    private void actualizarFiltros() {
        String texto = textfieldBuscarUsuarioAdmin.getText().toLowerCase();
        String rol = choiceboxRolAdmin.getValue();
        String especialidad = choiceboxEspecialidadAdmin.getValue();

        filteredData.setPredicate(usuario -> {
            if (rol != null && !rol.equals("Todos") && !usuario.getRol().equalsIgnoreCase(rol)) {
                return false; // Si el rol no coincide, fuera.
            }

            if ("Doctor".equalsIgnoreCase(rol)) {
                if (especialidad != null && !especialidad.equals("Todos")) {
                    if (!usuario.getEspecialidad().equalsIgnoreCase(especialidad)) {
                        return false;
                    }
                }
            }

            if (texto == null || texto.isEmpty()) {
                return true;
            }

            if (usuario.getNombreUsuario().toLowerCase().contains(texto)) {
                return true;
            }

            if (usuario.getUsuario().toLowerCase().contains(texto)) {
                return true;
            }

            return false;
        });
    }

    @FXML
    public void clickCambiarEstado(ActionEvent event) {
        Usuario seleccionado = tableUsuariosAdmin.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            mostrarAlerta("Selecciona un usuario primero.");
            return;
        }

        // Determinar el nuevo estado (Invertir el actual)
        String nuevoEstado = seleccionado.getEstado().equalsIgnoreCase("activo") ? "inactivo" : "activo";

        try {
            // 1. Llamar al servicio para actualizar BD
            adminService.actualizarEstadoUsuario(seleccionado.getIdUsuario(), nuevoEstado);

            // 2. Actualizar la tabla visualmente (sin recargar todo desde BD)
            seleccionado.setEstado(nuevoEstado);
            tableUsuariosAdmin.refresh(); // Refresca la vista

            mostrarAlerta("Usuario marcado como " + nuevoEstado);

        } catch (Exception e) {
            mostrarAlerta("Error al cambiar estado: " + e.getMessage());
        }
    }

    @FXML
    public void clickRegresarAlMenu() {
        mainController.volverAlMenuPrincipal("Administrador");
    }

    private void mostrarAlerta(String mensaje) {
        Alert a = new Alert(AlertType.INFORMATION);
        a.setContentText(mensaje);
        a.show();
    }
    private void cargarEspecialidades() {
        try {
            ObservableList<String> especialidades = adminService.obtenerEspecialidades();
            choiceboxEspecialidadAdmin.setItems(especialidades);
            choiceboxEspecialidadAdmin.setValue("Todos");
        } catch (
                Exception e) {
            mostrarAlerta("Error al cargar especialidades: " + e.getMessage());
        }
    }

    @FXML
    public void clickAgregarUsuario(ActionEvent actionEvent) {
        abrirFormularioUsuario(null);
    }

    private void abrirFormularioUsuario(Usuario usuarioParaEditar) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Ventanas/IngresarUsuario.fxml"));

            Parent root = loader.load();


            CIngresarUsuario controller = loader.getController();
            controller.setAdminService(adminService);
            controller.setup();

            // 4. Mostrar como diálogo modal
            Stage stage = new Stage();
            stage.setTitle("Crear Nuevo Usuario");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.showAndWait();

            cargarDatosYConfigurarFiltros();

        } catch (
                IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error al cargar el formulario de usuario: " + e.getMessage());
        }
    }

    public void clickEliminarUsuario(ActionEvent actionEvent) {
        Usuario usuarioSeleccionado = tableUsuariosAdmin.getSelectionModel().getSelectedItem();

        if (usuarioSeleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Selección Requerida",
                    "Por favor, selecciona un usuario de la tabla para eliminar.");
            return;
        }

        // 2. No permitir la auto-eliminación si es el administrador actual (Opcional, pero recomendado)
        if (usuarioSeleccionado.getIdUsuario() == SessionManager.getUsuarioBase().getIdUsuario()) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Permiso",
                    "No puedes eliminar tu propia cuenta de administrador.");
            return;
        }

        // 3. Pedir confirmación (Similar a la cancelación de cita)
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Eliminación");
        alert.setHeaderText("Eliminar Usuario: " + usuarioSeleccionado.getUsuario());
        alert.setContentText("¿Estás seguro de que deseas eliminar permanentemente a este usuario y todos sus datos asociados (incluyendo historial médico si es paciente)?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // 4. Llamar al servicio para la eliminación
                adminService.eliminarUsuario(usuarioSeleccionado.getIdUsuario());

                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito",
                        "El usuario " + usuarioSeleccionado.getUsuario() + " ha sido eliminado permanentemente.");

                // 5. Recargar la tabla
                cargarDatosYConfigurarFiltros();

            } catch (Exception e) {
                e.printStackTrace();
                mostrarAlerta(Alert.AlertType.ERROR, "Error de Eliminación",
                        "Fallo al eliminar el usuario: " + e.getMessage());
            }
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText("ÉXITO"); // Opcional, dependiendo de tu diseño
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}