package org.example.personas;

import lombok.Getter;

import java.time.LocalDateTime;

public class RegistroDePersonaEnSituacionVulnerable {
    @Getter
    private PersonaEnSituacionVulnerable personaEnSituacionVulnerable;
    @Getter
    private Colaborador colaborador;
    private LocalDateTime fechaHoraRegistro;
}
