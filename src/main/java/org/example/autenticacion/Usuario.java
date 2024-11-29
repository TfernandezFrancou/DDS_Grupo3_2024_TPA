package org.example.autenticacion;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.config.Configuracion;
import org.example.excepciones.PasswordException;
import org.example.personas.Persona;
import org.example.personas.contacto.MedioDeContacto;
import org.example.personas.contacto.Mensaje;
import org.example.personas.documentos.Documento;
import org.example.personas.roles.Colaborador;
import org.example.validaciones.VerificadorContrasenia;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.mail.MessagingException;
import javax.persistence.*;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
public class Usuario {

    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idUsuario;

    @OneToOne
    private Documento documento;

    private String nombreDeUsuario;

    @OneToOne(cascade = CascadeType.PERSIST)
    private Persona colaborador;
    private String contrasenia;//la contraseña se debe guardar hasheada, pero bueno
    private LocalDateTime fechaExpiracionContrasenia;

    @Getter
    @Setter
    @Column(columnDefinition = "INT")
    private Boolean estanDatosCorrectos;

    @Getter
    @Setter
    @Column(columnDefinition = "INT")
    private Boolean estanDatosCompletos;

    public Usuario(String nombreDeUsuario, String contrasenia, LocalDateTime fechaExpiracionContrasenia) {
        this.nombreDeUsuario = nombreDeUsuario;
        this.contrasenia = HashGenerator.hash(contrasenia);// aca se debe hashear la contraseña para mas seguridad
        this.fechaExpiracionContrasenia = fechaExpiracionContrasenia;
    }

    public Usuario(String nombre, Documento documento, Persona colaborador) {
        this.nombreDeUsuario = nombre;
        this.documento = documento;
        this.colaborador = colaborador;
        this.contrasenia = generarContrasenia();//no se hashea, se espera a que el usuario lo cambie
    }

    public void cambiarContrasenia(String nuevaContrasenia){
        try{
            VerificadorContrasenia.getInstancia().validarContrasenia(nuevaContrasenia);;
            this.setContrasenia(HashGenerator.hash(nuevaContrasenia));
            this.setFechaExpiracionContrasenia(LocalDateTime.now());
        } catch (PasswordException pEx){
            System.out.println(pEx.getMessage());
        }
    }

    public String getNombreDeUsuario() {
        return nombreDeUsuario;
    }

    public void setNombreDeUsuario(String nombreDeUsuario) {
        this.nombreDeUsuario = nombreDeUsuario;
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

    public LocalDateTime getFechaExpiracionContrasenia() {
        return fechaExpiracionContrasenia;
    }

    public void setFechaExpiracionContrasenia(LocalDateTime fechaExpiracionContrasenia) {
        this.fechaExpiracionContrasenia = fechaExpiracionContrasenia;
    }

    public void enviarCredenciales(MedioDeContacto medioDeContacto) throws MessagingException {
        //enviarCredenciales via Mail
        String cuerpo = Configuracion.obtenerProperties("mail.mensajeDeBienvenida.cuerpo");
        String asunto = Configuracion.obtenerProperties("mail.mensajeDeBienvenida.asunto");
        String mensajeDeBienvenida = cuerpo
                .replace("{username}", nombreDeUsuario)
                .replace("{password}", contrasenia);
        medioDeContacto.notificar(new Mensaje(asunto, mensajeDeBienvenida, colaborador));

        this.contrasenia = HashGenerator.hash(contrasenia);//encripto contraseña para poder compararla
    }

    private String generarContrasenia(){
        // generar credenciales
        return UUID.randomUUID().toString();
    }
}
