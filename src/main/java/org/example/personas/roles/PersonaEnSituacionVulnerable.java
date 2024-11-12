package org.example.personas.roles;

import lombok.Getter;
import lombok.Setter;
import org.example.tarjetas.TarjetaHeladera;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.time.LocalDate;

@Getter
@Setter
@Entity
public class PersonaEnSituacionVulnerable extends Rol {
    private LocalDate fechaNac;
    private LocalDate fechaRegistro;
    @Column(columnDefinition = "INT")
    private Boolean tieneMenores;
    private Integer cantMenores;

    @OneToOne
    private TarjetaHeladera tarjetaHeladera;
}
