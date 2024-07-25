package org.example.incidentes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;

import java.time.LocalDateTime;

@Getter
@Setter
public abstract class Incidente {
    private Heladera heladera;
    private String tipoDeIncidente;
    private LocalDateTime fechaDeEmision;

    public Incidente(Heladera heladera, String tipoDeIncidente, LocalDateTime fechaDeEmision) {
        this.heladera = heladera;
        this.tipoDeIncidente = tipoDeIncidente;
        this.fechaDeEmision = fechaDeEmision;
    }

    public void avisarATecnico() {
        // TODO avisarATecnico
    }
}
