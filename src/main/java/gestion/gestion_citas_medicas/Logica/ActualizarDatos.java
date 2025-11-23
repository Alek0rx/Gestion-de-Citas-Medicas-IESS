package gestion.gestion_citas_medicas.Logica;

import gestion.gestion_citas_medicas.ClasesNormales.*;
import gestion.gestion_citas_medicas.ClasesSQL.DoctorSQL;
import gestion.gestion_citas_medicas.ClasesSQL.OperadorSQL;
import gestion.gestion_citas_medicas.ClasesSQL.PacienteSQL;
import gestion.gestion_citas_medicas.ClasesSQL.UsuarioSQL;
import gestion.gestion_citas_medicas.ConexionBD.Conexion_BD;

import java.sql.Connection;
import java.time.LocalDate;

public class ActualizarDatos {
    public static boolean actualizarPerfil(Object perfil, String nombre, String apellido, String telefono,
                                           String correo, String direccion) {
        String antes = perfil.toString();
        if(perfil instanceof Paciente p){
            p.setNombre(nombre);
            p.setApellido(apellido);
            p.setTelefono(telefono);
            p.setCorreo(correo);
            p.setDireccion(direccion);
        }

        if(perfil instanceof Doctor d){
            d.setNombre(nombre);
            d.setApellido(apellido);
            d.setTelefono(telefono);
            d.setCorreo(correo);
            d.setDireccion(direccion);
        }

        if(perfil instanceof Operador o){
            o.setNombre(nombre);
            o.setApellido(apellido);
            o.setTelefono(telefono);
            o.setCorreo(correo);
            o.setDireccion(direccion);
        }

        boolean cambios = false;

        if(perfil instanceof Paciente p){
            if(!p.getNombre().equals(nombre)){
                p.setNombre(nombre);
                cambios = true;
            }
            if(!p.getApellido().equals(apellido)){
                p.setApellido(apellido);
                cambios = true;
            }
            if(!p.getTelefono().equals(telefono)){
                p.setTelefono(telefono);
                cambios = true;
            }
            if(!p.getCorreo().equals(correo)){
                p.setCorreo(correo);
                cambios = true;
            }
            if(!p.getDireccion().equals(direccion)){
                p.setDireccion(direccion);
                cambios = true;
            }
        }

        if(perfil instanceof Doctor d){
            if(!d.getNombre().equals(nombre)){
                d.setNombre(nombre);
                cambios = true;
            }
            if(!d.getApellido().equals(apellido)){
                d.setApellido(apellido);
                cambios = true;
            }
            if(!d.getTelefono().equals(telefono)){
                d.setTelefono(telefono);
                cambios = true;
            }
            if(!d.getCorreo().equals(correo)){
                d.setCorreo(correo);
                cambios = true;
            }
            if(!d.getDireccion().equals(direccion)){
                d.setDireccion(direccion);
                cambios = true;
            }
        }

        if(perfil instanceof Operador o){
            if(!o.getNombre().equals(nombre)){
                o.setNombre(nombre);
                cambios = true;
            }
            if(!o.getApellido().equals(apellido)){
                o.setApellido(apellido);
                cambios = true;
            }
            if(!o.getTelefono().equals(telefono)){
                o.setTelefono(telefono);
                cambios = true;
            }
            if(!o.getCorreo().equals(correo)){
                o.setCorreo(correo);
                cambios = true;
            }
            if(!o.getDireccion().equals(direccion)){
                o.setDireccion(direccion);
                cambios = true;
            }
        }

        // Si no hubo cambios, no hacemos update
        if(!cambios){
            return false;
        }

        try{
            if(perfil instanceof Paciente p){
                new PacienteSQL().update(p);
            }else if(perfil instanceof Doctor d){
                new DoctorSQL().update(d);
            }else if(perfil instanceof Operador o){
                new OperadorSQL().actualizar(o);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

        SessionManager.actualizarPerfil(perfil);

        return true;
    }

    public static boolean actualizarCredenciales(int idUsuario, String nuevoUsuario, String nuevaPassword) {
        return new UsuarioSQL().actualizarCredenciales(idUsuario, nuevoUsuario, nuevaPassword);
    }
}
