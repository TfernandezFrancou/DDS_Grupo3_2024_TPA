package org.example.autenticacion;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.config.Configuracion;
import org.example.personas.Persona;
import org.example.personas.contacto.MedioDeContacto;
import org.example.personas.contacto.Mensaje;
import org.example.personas.documentos.Documento;
import org.example.repositorios.RepoMensajes;

import javax.mail.MessagingException;
import javax.persistence.*;
import java.util.UUID;

@Entity
@NoArgsConstructor
public class Usuario {

    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idUsuario;

    @Getter
    @Setter
    private String googleId;

    @OneToOne
    private Documento documento;

    @Getter
    @Setter
    private String nombreDeUsuario;

    @Getter
    @Setter
    @Column(columnDefinition = "TEXT")
    private String foto;

    @OneToOne(cascade = CascadeType.PERSIST)
    private Persona colaborador;
    private String contrasenia;//la contraseña se guarda hasheada

    @Getter
    @Setter
    @Column(columnDefinition = "INT")
    private Boolean estanDatosCorrectos;

    @Getter
    @Setter
    @Column(columnDefinition = "INT")
    private Boolean estanDatosCompletos;

    public Usuario(String nombreDeUsuario, String contrasenia) {
        this.nombreDeUsuario = nombreDeUsuario;
        this.contrasenia = HashGenerator.hash(contrasenia);// aca se debe hashear la contraseña para mas seguridad
        this.estanDatosCompletos = false;
    }

    public Usuario(String nombre, Documento documento, Persona colaborador) {
        this.nombreDeUsuario = nombre;
        this.documento = documento;
        this.colaborador = colaborador;
        this.contrasenia = generarContrasenia();//no se hashea, se espera a que el usuario lo cambie
        this.estanDatosCompletos = false;
    }

    public Documento getDocumento() {
        return documento;
    }

    public void setDocumento(Documento documento) {
        this.documento = documento;
    }

    public Persona getColaborador() {
        return colaborador;
    }

    public void setColaborador(Persona colaborador) {
        this.colaborador = colaborador;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }


    public void enviarCredenciales(MedioDeContacto medioDeContacto) throws MessagingException {
        //enviarCredenciales via Mail
        String cuerpo = Configuracion.obtenerProperties("mail.mensajeDeBienvenida.cuerpo");
        String asunto = Configuracion.obtenerProperties("mail.mensajeDeBienvenida.asunto");
        String mensajeDeBienvenida = cuerpo
                .replace("{username}", nombreDeUsuario)
                .replace("{password}", contrasenia);
        Mensaje mensaje = new Mensaje(asunto, mensajeDeBienvenida, colaborador);
        medioDeContacto.notificar(mensaje);
        RepoMensajes.getInstancia().agregarMensaje(mensaje);
        this.contrasenia = HashGenerator.hash(contrasenia);//encripto contraseña para poder compararla
    }

    private String generarContrasenia(){
        // generar credenciales
        return UUID.randomUUID().toString();
    }
}
