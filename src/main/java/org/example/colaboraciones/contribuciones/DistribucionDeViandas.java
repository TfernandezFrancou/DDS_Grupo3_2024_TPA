package org.example.colaboraciones.contribuciones;

import org.example.colaboraciones.Contribucion;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;

import java.time.LocalDate;

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
}
