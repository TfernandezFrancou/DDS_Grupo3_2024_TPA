package org.example.repositorios;

import org.example.personas.PersonaEnSituacionVulnerable;
import org.example.personas.RegistroDePersonaEnSituacionVulnerable;

import java.util.List;

public class RepoPersonaEnSituacionVulnerable {
    private List<RegistroDePersonaEnSituacionVulnerable> registros;

    public void registrarPersona(RegistroDePersonaEnSituacionVulnerable registro) {
        this.registros.add(registro);
    }

    public PersonaEnSituacionVulnerable buscarPersonaPorNombre(String nombre) {
        return this.registros.stream()
                .map(RegistroDePersonaEnSituacionVulnerable::getPersonaEnSituacionVulnerable)
                .filter(persona -> persona.getNombre().equals(nombre))
                .findFirst()
                .orElseThrow();
    }

    public List<PersonaEnSituacionVulnerable> buscarPersonasPorColaborador(String nombreORazonSocial) {
        return this.registros.stream()
                .filter((r) -> r.getColaborador().getNombre().equals(nombreORazonSocial))
                .map(RegistroDePersonaEnSituacionVulnerable::getPersonaEnSituacionVulnerable)
                .toList();
    }
}
