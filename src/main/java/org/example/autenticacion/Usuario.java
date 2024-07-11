package org.example.autenticacion;

import org.example.excepciones.PasswordException;
import org.example.personas.Persona;
import org.example.personas.documentos.Documento;
import org.example.validaciones.VerificadorContrasenia;

import java.time.LocalDateTime;

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

    public Usuario() {
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

}
