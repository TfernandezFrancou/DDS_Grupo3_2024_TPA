package org.example.repositorios;

import org.example.personas.contacto.Mensaje;

import java.util.ArrayList;
import java.util.List;

public class RepoMensajes { //TODO conectar con DB
    private List<Mensaje> mensajes;

    private static RepoMensajes instancia = null;

    private RepoMensajes() {
        this.mensajes = new ArrayList<>();
    }

    public static RepoMensajes getInstancia() {
        if (instancia == null) {
            RepoMensajes.instancia = new RepoMensajes();
        }
        return instancia;
    }

    public void agregarMensaje(Mensaje mensaje) {
        this.mensajes.add(mensaje);
    }

    public void quitarMensaje(Mensaje mensaje) {
        this.mensajes.remove(mensaje);
    }
}