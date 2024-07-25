package org.example.repositorios;

import org.example.personas.contacto.Mensaje;

import java.util.ArrayList;
import java.util.List;

public class RepoMensaje {
    private List<Mensaje> mensajes;

    private static RepoMensaje instancia = null;

    private RepoMensaje() {
        this.mensajes = new ArrayList<>();
    }

    public static RepoMensaje getInstancia() {
        if (instancia == null) {
            RepoMensaje.instancia = new RepoMensaje();
        }
        return instancia;
    }

    public void agregar(Mensaje mensaje) {
        this.mensajes.add(mensaje);
    }

    public void eliminar(Mensaje mensaje) {
        this.mensajes.remove(mensaje);
    }
}