package org.example.colaboraciones.contribuciones.viandas;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Entrega {
    private EstadoEntrega estadoEntrega;
    private LocalDate fechaEntrega;


}
