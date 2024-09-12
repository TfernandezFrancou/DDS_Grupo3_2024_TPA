package org.example.colaboraciones.contribuciones.heladeras;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TemperaturaHeladera {
    private Integer temperaturaMinima;
    private Integer temperaturaMaxima;

    public TemperaturaHeladera(Integer temperaturaMinima, Integer temperaturaMaxima) {
        this.temperaturaMinima = temperaturaMinima;
        this.temperaturaMaxima = temperaturaMaxima;
    }
}
