package org.example.colaboraciones.contribuciones;

import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.Contribucion;
import org.example.colaboraciones.contribuciones.viandas.Vianda;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
public class DonacionDeViandas extends Contribucion {
    private List<Vianda> viandas;
    private Integer cantidadDeViandas;

    public DonacionDeViandas(){
        this.viandas= new ArrayList<>();
    }

    @Override
    public void ejecutarContribucion(){
        // TODO
    }
}
