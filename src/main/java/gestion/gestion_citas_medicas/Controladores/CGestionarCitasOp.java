package gestion.gestion_citas_medicas.Controladores;

import gestion.gestion_citas_medicas.ClasesNormales.*;
import gestion.gestion_citas_medicas.ClasesSQL.DoctorSQL;
import gestion.gestion_citas_medicas.ClasesSQL.EspecialidadSQL;

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
import java.util.Optional;


public class CGestionarCitasOp implements ControladorInyectable {

    private CMainVentana mainController;

    private OperadorService operadorService;

    @FXML
    private Pane paneFondo;
    @FXML
    private Pane paneFrente;
    @FXML
    private Label labelBienvenidaOp;
    @FXML
    private Label labelNombreOp;
    @FXML
    private Label labelEspecialidadOp;
    @FXML
    private Label labelDoctorOp;
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
    private Button buttonAsignarDoctor;
    @FXML
    private Button buttonRegresarAlMenu;


    @Override

    public void setMainController(CMainVentana mainController) {
        this.mainController = mainController;
        this.operadorService = new OperadorService(); // Instanciamos el servicio


        try {
            this.operadorService.cargarMapaHorarios(); // Carga el mapa de horas a través del servicio
            cargarInformacionOperador();
            cargarDoctores();
            cargarEspecialidades();
            inicializarTabla();
            String especialidadInicial = choiceboxEspecialidadOp.getValue();
            if (especialidadInicial != null) {
                cargarCitasPendientes(especialidadInicial);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(AlertType.ERROR, "Error de Inicialización", "Fallo al cargar datos iniciales: " + e.getMessage());
        }

    }
    @FXML
    public void initialize() {}

    @FXML

    public void cargarInformacionOperador() {
        textfieldNombreOp.setEditable(false);
        Operador o = SessionManager.getPerfil(Operador.class);
        String nombre = o.getNombre() + " " + o.getApellido();
        textfieldNombreOp.setText(nombre);
    }


    private void inicializarTabla() {
        // Mapeo directo
        columnIdCitaOp.setCellValueFactory(new PropertyValueFactory<>("idCita"));
        columnFechaCitaOp.setCellValueFactory(new PropertyValueFactory<>("fechaCita"));

        // Columna HORA (Obtiene el mapa del servicio para la conversión manual)
        columnHoraCitaOp.setCellValueFactory(cellData -> {
            int idHorario = cellData.getValue().getIdHorario();
            return new SimpleStringProperty(convertirIdHorarioAHora(idHorario));
        });


        // Columna PACIENTE (Concatenación manual de Nombre y Apellido)
        columnPacienteCitaOp.setCellValueFactory(cellData -> {
            Cita_Medica cita = cellData.getValue();
            return new SimpleStringProperty(cita.getNombrePaciente() + " " + cita.getApellidoPaciente());
        });
    }


    private String convertirIdHorarioAHora(int idHorario) {
        Map<Integer, String> mapaHorarios = operadorService.getMapaHorarios();
        return mapaHorarios.getOrDefault(idHorario, "Hora Desconocida");
    }



    public void cargarEspecialidades() {
        try {
            EspecialidadSQL espSQL = new EspecialidadSQL();
            List<Especialidad> lista = espSQL.findAll();
            ObservableList<String> nombres = FXCollections.observableArrayList();
            nombres.add("Todas");
            for (Especialidad e : lista) {

                nombres.add(e.getNombre());

            }
            choiceboxEspecialidadOp.setItems(nombres);
            choiceboxEspecialidadOp.getSelectionModel().selectFirst();
            choiceboxEspecialidadOp.valueProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null && !newValue.equals(oldValue)) {

                    cargarCitasPendientes(newValue);

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(AlertType.ERROR, "Error", "Fallo al cargar especialidades: " + e.getMessage());
        }
    }

    public void cargarCitasPendientes(String especialidad) {

        try {
            List<Cita_Medica> listaCitas = operadorService.obtenerCitasPendientes(especialidad);
            ObservableList<Cita_Medica> data = FXCollections.observableArrayList(listaCitas);
            tableCitasOp.setItems(data);
            tableCitasOp.refresh();
        }
        catch (
                Exception e) {
            e.printStackTrace();
            mostrarAlerta(AlertType.ERROR, "Error de Carga", "No se pudieron cargar las citas. " + e.getMessage());
        }
    }


    public void cargarDoctores() {
        try {
            DoctorSQL doctorSQL = new DoctorSQL();
            List<Doctor> lista = doctorSQL.findAll();
            ObservableList<String> nombres = FXCollections.observableArrayList();
            nombres.add("Seleccionar Doctor");

            for (Doctor d : lista) {
                nombres.add(d.getNombre() + " " + d.getApellido());
            }
            choiceboxDoctorOp.setItems(nombres);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error cargando doctores: " + e.getMessage());
        }
    }

    @FXML
    public void clickAsignarDoctor(ActionEvent actionEvent) {
        Cita_Medica citaSeleccionada = tableCitasOp.getSelectionModel().getSelectedItem();
        if (citaSeleccionada == null) {
            mostrarAlerta(AlertType.WARNING, "Advertencia", "Por favor, seleccione una cita pendiente de la tabla.");
            return;
        }

        // Aseguramos que solo se asignen a citas PENDIENTES
        if (!"PENDIENTE".equalsIgnoreCase(citaSeleccionada.getEstado())) {
            mostrarAlerta(AlertType.WARNING, "Advertencia", "Solo se pueden asignar doctores a citas con estado 'PENDIENTE'.");
            return;
        }

        String especialidadSeleccionada = choiceboxEspecialidadOp.getValue();
        if (especialidadSeleccionada == null || especialidadSeleccionada.isEmpty() || "Todas".equals(especialidadSeleccionada)) {
            mostrarAlerta(AlertType.ERROR, "Error", "Debe seleccionar una especialidad específica (no 'Todas') para asignar un doctor.");
            return;
        }

        try {
            // Llama al servicio para obtener los doctores de esa especialidad
            List<Doctor> doctores = operadorService.obtenerDoctoresPorEspecialidad(especialidadSeleccionada);

            if (doctores.isEmpty()) {
                mostrarAlerta(AlertType.INFORMATION, "Información", "No hay doctores disponibles en la especialidad: " + especialidadSeleccionada);
                return;
            }

            // Mapeo: Nombre completo -> Objeto Doctor (para recuperar el ID)
            Map<String, Doctor> mapaNombresADoctores = new HashMap<>();
            ObservableList<String> nombresDoctores = FXCollections.observableArrayList();

            for (Doctor d : doctores) {
                String nombreCompleto = d.getNombre() + " " + d.getApellido();
                nombresDoctores.add(nombreCompleto);
                mapaNombresADoctores.put(nombreCompleto, d);
            }

            // --- Mostrar el Diálogo de Selección ---
            // Usamos el primer doctor como valor inicial, si existe la lista
            ChoiceDialog<String> dialogo = new ChoiceDialog<>(nombresDoctores.get(0), nombresDoctores);
            dialogo.setTitle("Asignar Doctor");
            dialogo.setHeaderText("Asignar doctor a la cita #" + citaSeleccionada.getIdCita() + " (" + especialidadSeleccionada + ")");
            dialogo.setContentText("Seleccione el doctor:");

            Optional<String> resultado = dialogo.showAndWait();

            // --- Manejar el Resultado de la Selección ---
            resultado.ifPresent(nombreSeleccionado -> {
                try {
                    Doctor doctorSeleccionado = mapaNombresADoctores.get(nombreSeleccionado);

                    if (doctorSeleccionado == null) {
                        mostrarAlerta(AlertType.ERROR, "Error Interno", "No se pudo mapear el nombre seleccionado al objeto Doctor.");
                        return;
                    }

                    // Llama al servicio para actualizar la DB
                    operadorService.asignarDoctorACita(citaSeleccionada.getIdCita(), doctorSeleccionado.getIdDoctor());

                    mostrarAlerta(AlertType.INFORMATION, "Éxito",
                            "Doctor " + nombreSeleccionado + " asignado a la cita #" + citaSeleccionada.getIdCita() + ". Estado cambiado a ASIGNADA.");

                    cargarCitasPendientes(choiceboxEspecialidadOp.getValue());
                } catch (Exception e) {
                    e.printStackTrace();
                    mostrarAlerta(AlertType.ERROR, "Error de Guardado", "Fallo al asignar el doctor: " + e.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(AlertType.ERROR, "Error de Consulta", "Fallo al obtener la lista de doctores: " + e.getMessage());
        }
    }

    public void clickRegresarAlMenu(ActionEvent actionEvent) {
        mainController.volverAlMenuPrincipal("Operador");
    }

    private void mostrarAlerta(AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

}
