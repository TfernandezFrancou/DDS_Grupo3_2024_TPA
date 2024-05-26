package org.example.contribuciones.heladeras;

import lombok.Getter;
import lombok.Setter;
import org.example.contribuciones.heladeras.Heladera;

import java.time.LocalDateTime;

@Getter
@Setter
public class Uso {
    private LocalDateTime fechaHoraDeUso;
    private Heladera heladera;

    public Uso(LocalDateTime fechaHoraDeUso, Heladera heladera) {
        this.fechaHoraDeUso = fechaHoraDeUso;
        this.heladera = heladera;
    }

}
