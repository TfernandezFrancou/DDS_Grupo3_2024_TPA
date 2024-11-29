package org.example.personas;

import lombok.Getter;
import lombok.Setter;
import org.example.autenticacion.Usuario;
import org.example.excepciones.UserException;
import org.example.migracion.TipoColaboracion;
import org.example.personas.contacto.CorreoElectronico;
import org.example.colaboraciones.contribuciones.heladeras.Direccion;
import org.example.personas.contacto.MedioDeContacto;
import org.example.personas.documentos.Documento;
import org.example.personas.roles.Rol;
import org.example.repositorios.RepoUsuario;

import javax.mail.MessagingException;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idPersona;

    @OneToOne(cascade = CascadeType.PERSIST)
    protected Direccion direccion;
    @OneToOne(cascade = CascadeType.PERSIST)
    protected Rol rol;
    @OneToOne(cascade = CascadeType.PERSIST)
    protected Documento documento;
    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name ="id_persona")
    protected List<MedioDeContacto> mediosDeContacto;

    @Enumerated(value = EnumType.STRING)
    @ElementCollection(targetClass = TipoContribucion.class)
    @CollectionTable(
            name = "persona_tipo_contribucion",
            joinColumns = @JoinColumn(name = "idPersona")
    )
    protected List<TipoContribucion> contribucionesQuePuedeHacer;

    public Persona(){
        this.mediosDeContacto = new ArrayList<>();
        this.contribucionesQuePuedeHacer = new ArrayList<>();
    }

    public void addMedioDeContacto(MedioDeContacto medioDeContacto){
        this.mediosDeContacto.add(medioDeContacto);
    }

    abstract public String getNombre();

    public void addContribucionesQuePuedeHacer(TipoContribucion contribucion){
        this.contribucionesQuePuedeHacer.add(contribucion);
    }

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
            usuario.enviarCredenciales(this.getEmail());
            RepoUsuario.getInstancia().agregarUsuarios(usuario);
            return usuario;
        }
    }

    public void cambiarRol(Rol nuevoRol){
        this.rol = nuevoRol;
    }
}
