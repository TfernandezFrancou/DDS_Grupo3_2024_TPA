package org.example.colaboraciones.contribuciones.heladeras;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.personas.Persona;
import org.example.tarjetas.TarjetaColaborador;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class AperturaHeladera {
    private Heladera heladera;
    private TarjetaColaborador tarjeta;
    private LocalDateTime horarioApertura;

}