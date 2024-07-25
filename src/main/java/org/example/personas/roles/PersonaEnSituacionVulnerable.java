package org.example.personas.roles;

import lombok.Getter;
import lombok.Setter;
import org.example.tarjetas.TarjetaHeladera;

import java.time.LocalDate;

@Getter
@Setter
public class PersonaEnSituacionVulnerable extends Rol {
    private LocalDate fechaNac;
    private LocalDate fechaRegistro;
    private Boolean tieneMenores;
    private Integer cantMenores;
    private TarjetaHeladera tarjetaHeladera;
}
