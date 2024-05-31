package org.example.personas;

import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.contribuciones.heladeras.TarjetaHeladera;
import org.example.personas.documentos.Documento;

import java.time.LocalDate;

@Getter
@Setter
public class PersonaEnSituacionVulnerable {
    @Getter
    private String nombre;
    private LocalDate fechaNac;
    private LocalDate fechaRegistro;

    private String domicilio;
    private Documento documento;
    private Boolean tieneMenores;
    private Integer cantMenores;

    private TarjetaHeladera tarjetaHeladera;
}
