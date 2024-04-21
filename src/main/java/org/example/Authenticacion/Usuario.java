package org.example.Authenticacion;

import org.example.colaboradores.Colaborador;
import java.time.LocalDateTime;

public class Usuario {

    private String nombre;
    private String apellido;
    private String email;
    private Colaborador colaborador;
    private String contrasenia;
    private LocalDateTime fechaExpiracionContrasenia;

    public Usuario(String nombre, String contrasenia, String email, LocalDateTime fechaExpiracionContrasenia) {
        this.nombre = nombre;
        this.email = email;
        this.contrasenia = contrasenia;
        this.fechaExpiracionContrasenia = fechaExpiracionContrasenia;
    }

    public Usuario() {
    }

    void cambiarContrasenia(String nuevaContrasenia){
        this.setContrasenia(nuevaContrasenia);
        this.setFechaExpiracionContrasenia(LocalDateTime.now());
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Colaborador getColaborador() {
        return colaborador;
    }

    public void setColaborador(Colaborador colaborador) {
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
