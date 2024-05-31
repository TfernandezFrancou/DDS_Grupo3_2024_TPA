package org.example.colaboraciones;

import lombok.Getter;
import lombok.Setter;
import org.example.personas.Colaborador;
import org.example.personas.PersonaEnSituacionVulnerable;

import java.time.LocalDateTime;

@Getter
@Setter
public class RegistroDePersonaEnSituacionVulnerable {
    private PersonaEnSituacionVulnerable personaEnSituacionVulnerable;
    private Colaborador colaborador;
    private LocalDateTime fechaHoraRegistro;
}
