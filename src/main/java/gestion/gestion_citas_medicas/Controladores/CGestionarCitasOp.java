package gestion.gestion_citas_medicas.Controladores;

import gestion.gestion_citas_medicas.ClasesNormales.*;
import gestion.gestion_citas_medicas.Logica.OperadorService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CGestionarCitasOp implements ControladorInyectable {

    private CMainVentana mainController;
    private OperadorService operadorService;

    // --- MAPA CRÍTICO PARA LA ASIGNACIÓN ---
    // Clave: "Nombre Apellido", Valor: ID_Doctor
    private Map<String, Integer> mapaDoctores = new HashMap<>();

    @FXML
    private Pane paneFondo;
    @FXML
    private Pane paneFrente;
    @FXML
    private Label labelBienvenidaOp;
    @FXML
    private Label labelNombreOp;
    @FXML
    private Label labelEspecialidadOp; // Etiqueta estática
    @FXML
    private Label labelDoctorOp;       // Etiqueta estática
    @FXML
    private TextField textfieldNombreOp;

    @FXML
    private ChoiceBox<String> choiceboxEspecialidadOp;
    @FXML
    private ChoiceBox<String> choiceboxDoctorOp;

    @FXML
    private TableView<Cita_Medica> tableCitasOp;
    @FXML
    private TableColumn<Cita_Medica, Integer> columnIdCitaOp;
    @FXML
    private TableColumn<Cita_Medica, String> columnFechaCitaOp;
    @FXML
    private TableColumn<Cita_Medica, String> columnHoraCitaOp;
    @FXML
    private TableColumn<Cita_Medica, String> columnPacienteCitaOp;
    @FXML
    private TableColumn<Cita_Medica, String> columnDoctorAsignado; // Opcional: Para ver si ya tiene doctor

    @FXML
    private Button buttonAsignarDoctor;
    @FXML
    private Button buttonRegresarAlMenu;

    @Override
    public void setMainController(CMainVentana mainController) {
        this.mainController = mainController;
        this.operadorService = new OperadorService();

        try {
            this.operadorService.cargarMapaHorarios();
            cargarInformacionOperador();
            cargarEspecialidades();

            // Inicializar tabla y listeners
            inicializarTabla();
            configurarListeners();

            // Carga inicial (si hay un valor por defecto)
            choiceboxEspecialidadOp.getSelectionModel().selectFirst();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(AlertType.ERROR, "Error de Inicialización", "Fallo al cargar datos iniciales: " + e.getMessage());
        }
    }

    @FXML
    public void initialize() {
        // Método de JavaFX, se ejecuta antes de setMainController
    }

    private void cargarInformacionOperador() {
        textfieldNombreOp.setEditable(false);
        // Asegúrate de que SessionManager maneja nulos por si acaso
        try {
            Operador o = SessionManager.getPerfil(Operador.class);
            if (o != null) {
                String nombre = o.getNombre() + " " + o.getApellido();
                textfieldNombreOp.setText(nombre);
            }
        } catch (Exception e) {
            System.out.println("No hay sesión de operador activa o error al leerla.");
        }
    }

    private void cargarEspecialidades() {
        // Carga las especialidades en el ChoiceBox
        ObservableList<String> especialidades = operadorService.obtenerEspecialidades();
        choiceboxEspecialidadOp.setItems(especialidades);
    }

    private void configurarListeners() {
        // Cuando cambia la especialidad, recargamos la tabla y los doctores
        choiceboxEspecialidadOp.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                cargarCitasPendientes(newVal);
                cargarDoctores(newVal);
            }
        });
    }

    /**
     * Carga los doctores en el ChoiceBox basándose en la especialidad seleccionada.
     * Llena el MAPA para poder obtener el ID después.
     */
    private void cargarDoctores(String especialidad) {
        try {
            choiceboxDoctorOp.getItems().clear();
            mapaDoctores.clear();

            if (especialidad == null || especialidad.equals("Todas") || especialidad.equals("Todos")) {
                // Si quieres permitir asignar cualquier doctor, carga todos aquí.
                // O simplemente retorna vacío para obligar a filtrar.
                return;
            }

            List<Doctor> doctores = operadorService.obtenerDoctoresPorEspecialidad(especialidad);
            ObservableList<String> nombresDoctores = FXCollections.observableArrayList();

            for (Doctor doc : doctores) {
                String nombreCompleto = doc.getNombre() + " " + doc.getApellido();
                nombresDoctores.add(nombreCompleto);
                // GUARDA LA RELACIÓN: NOMBRE -> ID
                mapaDoctores.put(nombreCompleto, doc.getIdDoctor());
            }
            choiceboxDoctorOp.setItems(nombresDoctores);

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(AlertType.ERROR, "Error", "Error al cargar doctores: " + e.getMessage());
        }
    }

    private void cargarCitasPendientes(String especialidad) {
        try {
            List<Cita_Medica> citas = operadorService.obtenerCitasPendientes(especialidad);
            tableCitasOp.setItems(FXCollections.observableArrayList(citas));
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(AlertType.ERROR, "Error", "Error al cargar citas: " + e.getMessage());
        }
    }

    private void inicializarTabla() {
        columnIdCitaOp.setCellValueFactory(new PropertyValueFactory<>("idCita"));
        columnFechaCitaOp.setCellValueFactory(new PropertyValueFactory<>("fechaCita"));

        // Mapeo del Paciente (Concatenando nombre y apellido)
        columnPacienteCitaOp.setCellValueFactory(cellData -> {
            Cita_Medica c = cellData.getValue();
            return new SimpleStringProperty(c.getNombrePaciente() + " " + c.getApellidoPaciente());
        });

        // Mapeo de la HORA usando el mapa del servicio
        columnHoraCitaOp.setCellValueFactory(cellData -> {
            Cita_Medica c = cellData.getValue();
            Map<Integer, String> horarios = operadorService.getMapaHorarios();
            String horaStr = horarios.getOrDefault(c.getIdHorario(), "Hora desconocida");
            return new SimpleStringProperty(horaStr);
        });

        // (Opcional) Mostrar doctor actual si ya tiene uno asignado pero sigue pendiente
        if(columnDoctorAsignado != null) {
            columnDoctorAsignado.setCellValueFactory(cellData -> {
                Cita_Medica c = cellData.getValue();
                return new SimpleStringProperty(c.getNombreDoctor() + " " + c.getApellidoDoctor());
            });
        }
    }

    // =======================================================
    // MÉTODO DE ACCIÓN DEL BOTÓN (CORREGIDO E INTEGRADO)
    // =======================================================
    private void mostrarAlerta(AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public void clickRegresarAlMenu(ActionEvent actionEvent) {
        // Lógica para volver al menú principal
        if (mainController != null) {
            mainController.volverAlMenuPrincipal("Operador");
        }
    }

    public void clickAsignarDoctor(ActionEvent actionEvent) {
        // A. Validar que se seleccionó una cita de la tabla
        Cita_Medica citaSeleccionada = tableCitasOp.getSelectionModel().getSelectedItem();
        if (citaSeleccionada == null) {
            mostrarAlerta(AlertType.WARNING, "Atención", "Seleccione una cita de la tabla primero.");
            return;
        }

        // B. Validar que se seleccionó un doctor del ChoiceBox
        String nombreDoctorSeleccionado = choiceboxDoctorOp.getValue();
        if (nombreDoctorSeleccionado == null || nombreDoctorSeleccionado.isEmpty()) {
            mostrarAlerta(AlertType.WARNING, "Atención", "Seleccione un doctor de la lista.");
            return;
        }

        // C. Obtener el ID del doctor usando el Mapa
        Integer idDoctor = mapaDoctores.get(nombreDoctorSeleccionado);

        if (idDoctor == null) {
            mostrarAlerta(AlertType.ERROR, "Error Interno", "No se encontró el ID del doctor seleccionado en el sistema.");
            return;
        }

        // D. Ejecutar la actualización en Base de Datos
        try {
            operadorService.asignarDoctorACita(citaSeleccionada.getIdCita(), idDoctor);

            mostrarAlerta(AlertType.INFORMATION, "Éxito", "El doctor ha sido asignado correctamente.");

            // E. Refrescar la tabla para ver cambios
            String especialidadActual = choiceboxEspecialidadOp.getValue();
            if (especialidadActual != null) {
                cargarCitasPendientes(especialidadActual);
            }

            // Opcional: Limpiar la selección del doctor
            choiceboxDoctorOp.setValue(null);

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(AlertType.ERROR, "Error al Asignar", e.getMessage());
        }
    }
}