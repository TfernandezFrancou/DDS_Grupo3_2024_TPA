package org.example.colaboraciones.contribuciones;

import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.Contribucion;
import org.example.colaboraciones.TipoDePersona;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;

import java.time.LocalDate;

@Getter
@Setter
public class DistribucionDeViandas extends Contribucion {
    private Heladera origen;
    private Heladera destino;
    private Integer cantidad;
    private String motivo;
    private LocalDate fechaDistribucion;

    @Override
    public void ejecutarContribucion(){
        // TODO
    }

    @Override
    public boolean puedeRealizarContribucion() {
        return this.getTiposDePersona().equals(TipoDePersona.HUMANA);
    }
}
