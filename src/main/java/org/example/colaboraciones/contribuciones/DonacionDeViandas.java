package org.example.colaboraciones.contribuciones;

import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.Contribucion;
import org.example.colaboraciones.TipoDePersona;
import org.example.colaboraciones.contribuciones.viandas.Vianda;

import java.util.ArrayList;
import java.util.List;
@Getter
public class DonacionDeViandas extends Contribucion {
    private List<Vianda> viandas;
    @Setter
    private Integer cantidadDeViandas = 0;

    public DonacionDeViandas(){
        this.viandas= new ArrayList<>();
    }

    public DonacionDeViandas(Integer cantidad) {
        this.cantidadDeViandas = cantidad;
    }

    public DonacionDeViandas(List<Vianda> viandas){
        this.viandas= viandas;
    }

    @Override
    public void ejecutarContribucion(){
        //TODO guarda en la DB las viandas
    }

    @Override
    public boolean puedeRealizarContribucion() {
        return this.getTiposDePersona().equals(TipoDePersona.HUMANA);
    }

    public void agregarVianda(Vianda vianda){
        this.viandas.add(vianda);
        cantidadDeViandas++;
    }

    @Override
    public float obtenerPuntaje(){
        return cantidadDeViandas  * this.getCoeficientePuntaje();
    }
}
