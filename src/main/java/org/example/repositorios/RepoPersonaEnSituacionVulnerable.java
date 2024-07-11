package org.example.repositorios;

import org.example.personas.roles.PersonaEnSituacionVulnerable;
import org.example.colaboraciones.RegistroDePersonaEnSituacionVulnerable;

import java.util.ArrayList;
import java.util.List;

public class RepoPersonaEnSituacionVulnerable {
    private List<RegistroDePersonaEnSituacionVulnerable> registros;

    private static RepoPersonaEnSituacionVulnerable instancia = null;
    private RepoPersonaEnSituacionVulnerable() {
        this.registros = new ArrayList<>();
    }

    public static RepoPersonaEnSituacionVulnerable getInstancia() {
        if (instancia == null) {
            RepoPersonaEnSituacionVulnerable.instancia = new RepoPersonaEnSituacionVulnerable();
        }
        return instancia;
    }

    public void registrarPersona(RegistroDePersonaEnSituacionVulnerable registro) {
        this.registros.add(registro);
    }

    public PersonaEnSituacionVulnerable buscarPersonaPorNombre(String nombre) {
        return (PersonaEnSituacionVulnerable) this.registros.stream()
                .map(RegistroDePersonaEnSituacionVulnerable::getPersonaEnSituacionVulnerable)
                .filter(persona -> persona.getNombre().equals(nombre))
                .findFirst()
                .orElseThrow().getRol();
    }

    public List<PersonaEnSituacionVulnerable> buscarPersonasPorColaborador(String nombreORazonSocial) {
        return this.registros.stream()
                .filter((r) -> r.getColaborador().getNombre().equals(nombreORazonSocial))
                .map((registro) ->
                        (PersonaEnSituacionVulnerable) registro.getPersonaEnSituacionVulnerable().getRol()
                ).toList();
    }
}
