package org.example.autenticacion;

import org.example.config.Configuracion;
import org.example.excepciones.PasswordException;
import org.example.personas.Persona;
import org.example.personas.contacto.MedioDeContacto;
import org.example.personas.contacto.Mensaje;
import org.example.personas.documentos.Documento;
import org.example.personas.roles.Colaborador;
import org.example.validaciones.VerificadorContrasenia;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.UUID;

public class Usuario {
    private Documento documento;
    private String nombreDeUsuario;
    private Persona colaborador;
    private String contrasenia;
    private LocalDateTime fechaExpiracionContrasenia;

    public Usuario(String nombreDeUsuario, String contrasenia, LocalDateTime fechaExpiracionContrasenia) {
        this.nombreDeUsuario = nombreDeUsuario;
        this.contrasenia = contrasenia;
        this.fechaExpiracionContrasenia = fechaExpiracionContrasenia;
    }

    public Usuario(String nombre, Documento documento, Persona colaborador) {
        this.nombreDeUsuario = nombre;
        this.documento = documento;
        this.colaborador = colaborador;
        this.contrasenia = generarContrasenia();
    }

    public void cambiarContrasenia(String nuevaContrasenia){
        try{
            VerificadorContrasenia.getInstancia().validarContrasenia(nuevaContrasenia);;
            this.setContrasenia(nuevaContrasenia);
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
    }

    private String generarContrasenia(){
        //TODO generar credenciales
        return UUID.randomUUID().toString();
    }
}
