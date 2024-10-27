package org.example.personas;

import lombok.Getter;
import lombok.Setter;
import org.example.autenticacion.Usuario;
import org.example.excepciones.UserException;
import org.example.personas.contacto.CorreoElectronico;
import org.example.personas.contacto.Direccion;
import org.example.personas.contacto.MedioDeContacto;
import org.example.personas.documentos.Documento;
import org.example.personas.roles.Rol;
import org.example.repositorios.RepoUsuario;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public abstract class Persona {
    protected Direccion direccion;
    protected Rol rol;
    protected Documento documento;
    protected List<MedioDeContacto> mediosDeContacto;

    public Persona(){
        this.mediosDeContacto = new ArrayList<>();
    }

    public void addMedioDeContacto(MedioDeContacto medioDeContacto){
        this.mediosDeContacto.add(medioDeContacto);
    }

    abstract public String getNombre();

    public CorreoElectronico getEmail() {
        for (MedioDeContacto medioDeContacto: this.mediosDeContacto) {
            if (medioDeContacto instanceof CorreoElectronico) {
                return (CorreoElectronico) medioDeContacto;
            }
        }
        return null;
    }

    public Usuario buscarOCrearUsuario() throws MessagingException {
        try {
            return RepoUsuario.getInstancia().obtenerUsuarioPorDocumento(documento);
        } catch (UserException userException) {
            Usuario usuario = new Usuario(this.getNombre(), documento, this);
            RepoUsuario.getInstancia().agregarUsuarios(usuario);
            usuario.enviarCredenciales(this.getEmail());
            return usuario;
        }
    }

    public void cambiarRol(Rol nuevoRol){
        this.rol = nuevoRol;
    }
}
