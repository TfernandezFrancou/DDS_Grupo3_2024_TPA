package org.example.repositorios;

import org.example.personas.Persona;

import java.util.ArrayList;
import java.util.List;

public class RepoPersona {
    private List<Persona> personas;

    private static RepoPersona instancia = null;
    private RepoPersona() {
        this.personas = new ArrayList<>();
    }

    public static RepoPersona getInstancia() {
        if (instancia == null) {
            RepoPersona.instancia = new RepoPersona();
        }
        return instancia;
    }

    public void agregar(Persona persona) {
        this.personas.add(persona);
    }

    public void eliminar(Persona persona) {
        this.personas.remove(persona);
    }

    public Persona buscarPorNombre(String nombre) {
        return this.personas.stream()
                .filter(persona -> persona.getNombre().equals(nombre))
                .findFirst()
                .orElseThrow();
    }

    public Persona buscarPorNombreYRol(String nombre, Class<?> rol) {
        return this.personas.stream()
            .filter(persona -> persona.getNombre().equals(nombre) && persona.getRol().getClass().equals(rol))
            .findFirst()
            .orElseThrow();
    }
}