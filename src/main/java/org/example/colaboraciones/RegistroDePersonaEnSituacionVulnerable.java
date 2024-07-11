package org.example.colaboraciones;

import lombok.Getter;
import lombok.Setter;
import org.example.personas.Persona;
import org.example.personas.roles.PersonaEnSituacionVulnerable;

import java.time.LocalDateTime;

@Getter
@Setter
public class RegistroDePersonaEnSituacionVulnerable {
    private Persona personaEnSituacionVulnerable;
    private Persona colaborador;
    private LocalDateTime fechaHoraRegistro;
}
