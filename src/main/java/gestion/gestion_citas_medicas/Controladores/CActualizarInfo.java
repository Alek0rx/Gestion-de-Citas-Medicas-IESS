package gestion.gestion_citas_medicas.Controladores;
import gestion.gestion_citas_medicas.ClasesNormales.Doctor;
import gestion.gestion_citas_medicas.ClasesNormales.Operador;
import gestion.gestion_citas_medicas.ClasesNormales.Paciente;
import gestion.gestion_citas_medicas.ClasesNormales.SessionManager;
import gestion.gestion_citas_medicas.ClasesSQL.UsuarioSQL;
import gestion.gestion_citas_medicas.Logica.ActualizarDatos;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import javax.swing.*;
import java.time.LocalDate;

public class CActualizarInfo implements ControladorInyectable {

    private String rolUsuario;
    private CMainVentana mainController;
    @FXML private Pane paneFondo;
    @FXML private Pane paneFrente;
    @FXML private Label labelActualizarInfo;

    // Labels de los campos
    @FXML private Label labelNombreAI;
    @FXML private Label labelApellidoAI;
    @FXML private Label labelTelefonoAI;
    @FXML private Label labelCorreoAI;
    @FXML private Label labelDireccionAI;
    @FXML private Label labelGeneroAI;
    @FXML private Label labelFechaNacAI;
    @FXML private Label labelCedulaAI;

    // Campos de texto
    @FXML private TextField textfieldNombreAI;
    @FXML private TextField textfieldApellidoAI;
    @FXML private TextField textfieldTelefonoAI;
    @FXML private TextField textfieldCorreoAI;
    @FXML private TextField textfieldDireccionAI;
    @FXML private TextField textfieldGeneroAI;
    @FXML private TextField textfieldCedulaAI;

    // DatePicker
    @FXML private DatePicker selectorFechaAI;

    // Botones
    @FXML private Button buttonActualizarCredencialesAI;
    @FXML private Button buttonGuardarCambios;
    @FXML private Button buttonRegresarMenu;

    @FXML
    public void initialize() {
    }

    @Override
    public void setMainController(CMainVentana mainController) {
        this.mainController = mainController;
    }

    public void inicializarDatos(String rol) {
        this.rolUsuario = rol;
        textfieldCedulaAI.setEditable(false);
        textfieldGeneroAI.setEditable(false);
        selectorFechaAI.setEditable(false);
        cargarDatosUsuario();

    }

    @FXML
    private void clickGuardarCambios(ActionEvent e) {
        //Obtiene la informacion
        String nombre = textfieldNombreAI.getText();
        String apellido = textfieldApellidoAI.getText();
        String telefono = textfieldTelefonoAI.getText();
        String correo = textfieldCorreoAI.getText();
        String direccion = textfieldDireccionAI.getText();

        //Informacion actual
        Object perfil = switch (rolUsuario) {
            case "Paciente" -> SessionManager.getPerfil(Paciente.class);
            case "Doctor" -> SessionManager.getPerfil(Doctor.class);
            case "Operador" -> SessionManager.getPerfil(Operador.class);
            default -> null;
        };

        if(perfil == null){
            JOptionPane.showMessageDialog(null, "Error al cargar el perfil");
            return;
        }

        boolean actualizado =  ActualizarDatos.actualizarPerfil( perfil, nombre, apellido, telefono,correo, direccion);

        if(actualizado){
            JOptionPane.showMessageDialog(null, "Información actualizada correctamente.");
        }else{
            JOptionPane.showMessageDialog(null, "No se realizaron cambios.");
        }
    }

    @FXML
    private void clickRegresarMenu(ActionEvent e) {
        mainController.volverAlMenuPrincipal(rolUsuario);
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

        Object perfil = switch (rolUsuario){
            case "Paciente" -> SessionManager.getPerfil(Paciente.class);
            case "Doctor" -> SessionManager.getPerfil(Doctor.class);
            case "Operador" -> SessionManager.getPerfil(Operador.class);
            default -> null;
        };

        if(perfil == null){
            JOptionPane.showMessageDialog(null, "Error: no se encontró el perfil.");
            return;
        }

        //Actualiza las credenciales
        int idUsuario = 0;
        if (perfil instanceof Paciente p) idUsuario = p.getIdUsuario();
        if (perfil instanceof Doctor d) idUsuario = d.getIdUsuario();
        if (perfil instanceof Operador o) idUsuario = o.getIdUsuario();

        boolean actualizado = ActualizarDatos.actualizarCredenciales(idUsuario, nuevoUsuario, nuevaContrasena);

        if (actualizado) {
            JOptionPane.showMessageDialog(null, "Credenciales actualizadas correctamente.");
        } else {
            JOptionPane.showMessageDialog(null, "No se pudieron actualizar las credenciales.");
        }

    }

    public void cargarDatosUsuario () {
        Object perfil = switch (rolUsuario) {
            case "Paciente" -> SessionManager.getPerfil(Paciente.class);
            case "Doctor" -> SessionManager.getPerfil(Doctor.class);
            case "Operador" -> SessionManager.getPerfil(Operador.class);
            default -> null;
        };

        if (perfil != null) {
            cargarDatosGenericos(perfil);
        }
    }

    private void cargarDatosGenericos(Object perfil) {
        textfieldNombreAI.clear();
        textfieldApellidoAI.clear();
        textfieldTelefonoAI.clear();
        textfieldDireccionAI.clear();
        textfieldCorreoAI.clear();
        textfieldCedulaAI.clear();
        textfieldGeneroAI.clear();
        selectorFechaAI.setValue(null);

        if (perfil instanceof Paciente p) {

            textfieldNombreAI.setText(p.getNombre());
            textfieldApellidoAI.setText(p.getApellido());
            textfieldTelefonoAI.setText(p.getTelefono());
            textfieldDireccionAI.setText(p.getDireccion());
            textfieldCorreoAI.setText(p.getCorreo());
            textfieldCedulaAI.setText(p.getCedula());
            textfieldGeneroAI.setText(Character.toString(p.getGenero()));
            selectorFechaAI.setValue(p.getFechaNacimiento());

            return;
        }

        if (perfil instanceof Doctor d) {
            selectorFechaAI.setVisible(false);
            selectorFechaAI.setDisable(true);
            labelFechaNacAI.setVisible(false);
            labelGeneroAI.setVisible(false);
            textfieldGeneroAI.setVisible(false);

            textfieldNombreAI.setText(d.getNombre());
            textfieldApellidoAI.setText(d.getApellido());
            textfieldCorreoAI.setText(d.getCorreo());
            textfieldTelefonoAI.setText(d.getTelefono());
            textfieldDireccionAI.setText(d.getDireccion());
            textfieldCedulaAI.setText(d.getCedula());

            return;
        }

        if (perfil instanceof Operador op) {
            selectorFechaAI.setVisible(false);
            selectorFechaAI.setDisable(true);
            labelFechaNacAI.setVisible(false);
            labelGeneroAI.setVisible(false);
            textfieldGeneroAI.setVisible(false);

            textfieldDireccionAI.setText(op.getDireccion());
            textfieldCedulaAI.setText(op.getCedula());
            textfieldNombreAI.setText(op.getNombre());
            textfieldTelefonoAI.setText(op.getTelefono());
            textfieldApellidoAI.setText(op.getApellido());
            textfieldCorreoAI.setText(op.getCorreo());
        }
    }

}
