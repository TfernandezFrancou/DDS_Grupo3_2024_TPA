package org.example.repositorios;

import org.example.colaboraciones.Contribucion;

import java.util.ArrayList;
import java.util.List;

public class RepoContribucion {//TODO conectar con DB
    private List<Contribucion> contribuciones;

    private static RepoContribucion instancia = null;

    private RepoContribucion() {
        this.contribuciones = new ArrayList<>();
    }

    public static RepoContribucion getInstancia() {
        if (instancia == null) {
            RepoContribucion.instancia = new RepoContribucion();
        }
        return instancia;
    }

    public void agregarContribucion(Contribucion contribucion) {
        this.contribuciones.add(contribucion);
    }

    public void eliminarContribucion(Contribucion contribucion) {
        this.contribuciones.remove(contribucion);
    }
}
