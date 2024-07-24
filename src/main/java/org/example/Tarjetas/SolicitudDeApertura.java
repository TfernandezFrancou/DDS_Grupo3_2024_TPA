package org.example.Tarjetas;

import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.Contribucion;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.personas.roles.Colaborador;

import java.time.LocalDateTime;

@Getter
@Setter
public class SolicitudDeApertura {
    private Heladera heladera;
    private Colaborador colaborador;
    private LocalDateTime fechaCreacion;
    private Contribucion contribucion;
}
