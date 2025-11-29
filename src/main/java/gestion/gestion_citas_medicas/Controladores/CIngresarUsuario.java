package gestion.gestion_citas_medicas.Controladores;

import gestion.gestion_citas_medicas.ClasesNormales.*;
import gestion.gestion_citas_medicas.Logica.AdminService;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class CIngresarUsuario {

    private static final String REGEX_NOMBRE_APELLIDO = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$";
    private static final String REGEX_CEDULA = "^[0-9]{10}$";
    private static final String REGEX_TELEFONO = "^09[0-9]{8}$";
    private static final String REGEX_CORREO = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";



    // Inyectamos el servicio para lógica de negocio
    private AdminService adminService;

    // === Contenedor principal
    @FXML private Pane paneFondo;

    // === Etiquetas (Necesarias para habilitar/deshabilitar)
    @FXML private Label labelTitulo;
    @FXML private Label labelRol;
    @FXML private Label labelEspecialidad;
    @FXML public Label labelCredenciales;
    @FXML public Label labelUsuario;
    @FXML public Label labelCedula;
    @FXML public Label labelFechaNacimiento;
    @FXML public Label labelGenero;
    @FXML public Label labelDireccion;
    @FXML public Label labelCorreo;
    @FXML public Label labelTelefono;
    @FXML public Label labelApellido;
    @FXML public Label labelNombre;
    @FXML public Label labelDatosPersonales;
    @FXML public Label labelPassword;

    // === Campos de texto y contraseña
    @FXML private TextField textfieldUsuario;
    @FXML private PasswordField passwordField;
    @FXML private TextField textfieldNombre;
    @FXML private TextField textfieldApellido;
    @FXML private TextField textfieldTelefono;
    @FXML private TextField textfieldCorreo;
    @FXML private TextField textfieldDireccion;
    @FXML private TextField textfieldCedula;

    // === ComboBox
    @FXML private ChoiceBox<String> choiceboxRol;
    @FXML private ChoiceBox<String> choiceboxEspecialidad;
    @FXML private ChoiceBox<String> choiceboxGenero;

    // === DatePicker
    @FXML private DatePicker datePickerNacimiento;

    // === Botones
    @FXML private Button buttonCrearUsuario;
    @FXML private Button buttonRegresarMenu;


    public void setAdminService(AdminService service) {
        this.adminService = service;
    }


    @FXML
    private void initialize() {
        // 1. Deshabilitar todos los campos al inicio
        setCamposPersonalesHabilitados(false);
        setCamposCredencialesHabilitados(false);
        setCamposPacienteHabilitados(false);
        buttonCrearUsuario.setDisable(true);
        // 4. Configurar el Binding para habilitar el botón GUARDAR
        configurarBotonCrearBinding();
        datePickerNacimiento.setDisable(true);
    }

    public void setup() {
        cargarDatosIniciales();
        configurarListeners();
    }

    private void cargarDatosIniciales() {
        // Roles (todos los que pueden ser creados, excluimos Admin)
        choiceboxRol.getItems().addAll("Paciente", "Doctor", "Operador");

        // Especialidades (asumiendo que el AdminService ya trae la lista)
        choiceboxEspecialidad.setItems(adminService.obtenerEspecialidades());
        choiceboxEspecialidad.setDisable(true);
        // Género
        choiceboxGenero.getItems().addAll("Hombre", "Mujer");
    }

    private void configurarListeners() {
        // Listener principal: Rol
        choiceboxRol.valueProperty().addListener((obs, oldVal, newRol) -> {

            // Paso 1: Determinar el estado de la Especialidad
            boolean esDoctor = "Doctor".equals(newRol);
            choiceboxEspecialidad.setDisable(!esDoctor);
            labelEspecialidad.setDisable(!esDoctor);

            // Paso 2: Habilitar el siguiente paso
            setCamposCredencialesHabilitados(true);
            setCamposPersonalesHabilitados(true);

            // Paso 3: Ocultar/Mostrar campos específicos de Paciente
            if ("Paciente".equals(newRol)) {
                setCamposPacienteHabilitados(true);
            } else {
                setCamposPacienteHabilitados(false);
            }
        });

        // Listener para Doctor: Solo habilitar campos personales si la especialidad está seleccionada (opcional)
        // Por simplicidad, habilitamos los campos personales DESPUÉS de escoger el rol.

    }

    // --- Métodos de Control de Habilitación ---

    private void setCamposCredencialesHabilitados(boolean habilitar) {
        textfieldUsuario.setDisable(!habilitar);
        passwordField.setDisable(!habilitar);
        // ... (otras etiquetas de credenciales si las tienes) ...
    }

    private void setCamposPersonalesHabilitados(boolean habilitar) {
        // Campos comunes a todos
        textfieldNombre.setDisable(!habilitar);
        textfieldApellido.setDisable(!habilitar);
        textfieldTelefono.setDisable(!habilitar);
        textfieldCorreo.setDisable(!habilitar);
        textfieldDireccion.setDisable(!habilitar);
        textfieldCedula.setDisable(!habilitar);
    }

    private void setCamposPacienteHabilitados(boolean habilitar) {
        // Campos específicos de Paciente (Fecha y Género)
        datePickerNacimiento.setDisable(!habilitar);
        choiceboxGenero.setDisable(!habilitar);
        labelFechaNacimiento.setVisible(habilitar);
        labelGenero.setVisible(habilitar);
        choiceboxGenero.setVisible(habilitar);
        datePickerNacimiento.setVisible(habilitar);
    }

    // --- Lógica del Botón Crear ---

    // 4. Configurar el Binding para habilitar el botón GUARDAR
    private void configurarBotonCrearBinding() {
        // Define un stream de todas las propiedades de texto/valor que deben estar llenas.
        // Incluye los campos base (Usuario, Pass, Nombre, Apellido, Cédula, etc.)
        BooleanBinding camposComunesLlenos = textfieldUsuario.textProperty().isEmpty()
                .or(passwordField.textProperty().isEmpty())
                .or(textfieldNombre.textProperty().isEmpty())
                .or(textfieldApellido.textProperty().isEmpty())
                .or(textfieldCedula.textProperty().isEmpty());

        // Define la validación extra para el rol Doctor (requiere especialidad)
        BooleanBinding esDoctorYFaltaEspecialidad = choiceboxRol.valueProperty().isEqualTo("Doctor")
                .and(choiceboxEspecialidad.valueProperty().isNull());

        // Define la validación extra para el rol Paciente (requiere Fecha y Género)
        BooleanBinding esPacienteYFaltanDatos = choiceboxRol.valueProperty().isEqualTo("Paciente")
                .and(datePickerNacimiento.valueProperty().isNull())
                .or(choiceboxRol.valueProperty().isEqualTo("Paciente").and(choiceboxGenero.valueProperty().isNull()));

        // El botón estará deshabilitado si:
        // 1. Los campos comunes NO están llenos (negación de camposComunesLlenos)
        // 2. O si es Doctor y falta la Especialidad
        // 3. O si es Paciente y faltan Género/Fecha
        buttonCrearUsuario.disableProperty().bind(
                camposComunesLlenos.or(esDoctorYFaltaEspecialidad).or(esPacienteYFaltanDatos)
        );
    }


    @FXML
    private void clickRegresarAlMenu() {
        // Lógica para cerrar la ventana modal
        ((Stage) buttonRegresarMenu.getScene().getWindow()).close();
    }

    public ObservableList<String> obtenerEspecialidades() {
        List<String> nombresEspecialidades = new ArrayList<>(
                ElementosEstaticos.especialidad.values()
        );

        nombresEspecialidades.add(0, "Todos");

        return FXCollections.observableArrayList(nombresEspecialidades);
    }

    private char convertirGeneroAUsuario(String generoUI) {
        if (generoUI == null) return ' ';

        return switch (generoUI.toLowerCase()) {
            case "hombre" ->
                    'H';
            case "mujer" ->
                    'M';
            default ->
                    'O';
        };
    }

    // Método de validación general
    private boolean validarCampo(String valor, String regex) {
        if (valor == null || valor.trim().isEmpty()) return false;
        return valor.matches(regex);
    }


    private String validarTodosLosCampos() {
        StringBuilder errores = new StringBuilder();
        String rol = choiceboxRol.getValue();

        if (rol == null) {
            return "Debe seleccionar un Tipo de Rol.";
        }

        if (!validarCampo(textfieldUsuario.getText(), ".+")) {
            errores.append("- El campo Usuario no puede estar vacío.\n");
        }
        if (passwordField.getText().length() < 6) { // Añadimos una validación mínima de longitud
            errores.append("- La Contraseña debe tener al menos 6 caracteres.\n");
        }

        if (!validarCampo(textfieldNombre.getText(), REGEX_NOMBRE_APELLIDO)) {
            errores.append("- El Nombre solo debe contener letras y espacios.\n");
        }
        if (!validarCampo(textfieldApellido.getText(), REGEX_NOMBRE_APELLIDO)) {
            errores.append("- El Apellido solo debe contener letras y espacios.\n");
        }
        if (!validarCampo(textfieldCedula.getText(), REGEX_CEDULA)) {
            errores.append("- La Cédula debe ser de 10 dígitos numéricos.\n");
        }

        if ("Doctor".equals(rol) && (choiceboxEspecialidad.getValue() == null || choiceboxEspecialidad.getValue().equals("Todos"))) {
            errores.append("- Debe seleccionar una Especialidad válida para el Doctor.\n");
        }

        if (!textfieldTelefono.getText().isEmpty() && !validarCampo(textfieldTelefono.getText(), REGEX_TELEFONO)) {
            errores.append("- El Teléfono debe ser un número que empiece con '09' y tener 10 dígitos.\n");
        }
        if (!textfieldCorreo.getText().isEmpty() && !validarCampo(textfieldCorreo.getText(), REGEX_CORREO)) {
            errores.append("- El formato del Correo electrónico es inválido.\n");
        }

        if ("Paciente".equals(rol)) {
            if (datePickerNacimiento.getValue() == null) {
                errores.append("- Debe seleccionar la Fecha de Nacimiento.\n");
            } else {
                // Regla de Negocio: No permitir menores de edad
                if (datePickerNacimiento.getValue().isAfter(LocalDate.now().minusYears(18))) {
                    errores.append("- El paciente debe ser mayor de 18 años.\n");
                }
            }
            if (choiceboxGenero.getValue() == null) {
                errores.append("- Debe seleccionar el Género.\n");
            }
        }

        // Si el StringBuilder tiene contenido, devuelve el string de errores. Si está vacío, devuelve null.
        return errores.length() > 0 ? errores.toString() : null;
    }


    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    private void clickCrearUsuario() {
        String error = validarTodosLosCampos();

        if (error != null) {
            // Si hay errores, muestra el StringBuilder completo
            mostrarAlerta(Alert.AlertType.ERROR, "Errores de Validación", error);
            return;
        }

        try {
            Usuario nuevoUser = new Usuario();
            nuevoUser.setUsuario(textfieldUsuario.getText());
            nuevoUser.setPassword(passwordField.getText());
            nuevoUser.setRol(choiceboxRol.getValue());
            nuevoUser.setEstado("Activo");

            Object perfil = construirObjetoPerfil(nuevoUser.getRol());
            System.out.println(perfil);

            // TODO: eliminar esta linea
            System.out.println("1");
            adminService.crearNuevoUsuario(nuevoUser, perfil);

            // TODO: eliminar esta linea
            System.out.println("2");

            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Usuario " + nuevoUser.getUsuario() + " creado con éxito.");

            ((Stage) buttonCrearUsuario.getScene().getWindow()).close();

        }
        catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Base de Datos", "Fallo al crear el usuario: " + e.getMessage());
        }
    }


    // Método auxiliar para construir el objeto de perfil correcto
    private Object construirObjetoPerfil(String rol) {

        String nombre = textfieldNombre.getText();
        String apellido = textfieldApellido.getText();
        String telefono = textfieldTelefono.getText();
        String correo = textfieldCorreo.getText();
        String direccion = textfieldDireccion.getText();
        String cedula = textfieldCedula.getText();

        switch (rol) {
            case "Paciente":
                Paciente p = new Paciente();
                p.setNombre(nombre);
                p.setApellido(apellido);
                p.setCedula(cedula);
                p.setTelefono(telefono);
                p.setCorreo(correo);
                p.setDireccion(direccion);
                p.setFechaNacimiento(datePickerNacimiento.getValue());
                char c = convertirGeneroAUsuario(choiceboxGenero.getValue());
                System.out.println(c);
                p.setGenero(c);
                return p;

            case "Doctor":
                Doctor d = new Doctor();
                d.setNombre(nombre);
                d.setApellido(apellido);
                d.setCedula(cedula);
                d.setTelefono(telefono);
                d.setEstado("activo");
                d.setCorreo(correo);
                d.setDireccion(direccion);
                int idEspecialidad = ElementosEstaticos.getIdEspecialidad(choiceboxEspecialidad.getValue());
                d.setIdEspecialidad(idEspecialidad);
                d.setIdEspecialidad(idEspecialidad);
                String consultorioGenerico = ElementosEstaticos.generarConsultorioGenerico(idEspecialidad);
                d.setConsultorio(consultorioGenerico);
                return d;

            case "Operador":
                Operador o = new Operador();
                o.setNombre(nombre);
                o.setApellido(apellido);
                o.setCedula(cedula);
                o.setTelefono(telefono);
                o.setCorreo(correo);
                o.setDireccion(direccion);
                return o;

            default:
                return null;
        }
    }

}