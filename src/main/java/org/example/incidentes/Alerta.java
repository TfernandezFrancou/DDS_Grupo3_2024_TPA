package org.example.incidentes;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Alerta extends Incidente {
    private String tipoDeAlerta;

    public Alerta(String tipoDeAlerta, Heladera heladera, String tipoDeIncidente, LocalDateTime fechaDeEmision) {
        super(heladera, tipoDeIncidente, fechaDeEmision);
        this.tipoDeAlerta = tipoDeAlerta;
    }
}