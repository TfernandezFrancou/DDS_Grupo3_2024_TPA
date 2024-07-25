package org.example.colaboraciones.contribuciones;

import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.Contribucion;
import org.example.colaboraciones.TipoDePersona;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.colaboraciones.contribuciones.viandas.Vianda;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Getter
public class DonacionDeViandas extends Contribucion {
    private Heladera heladera;
    private List<Vianda> viandas;
    @Setter
    private Integer cantidadDeViandas = 0;

    public DonacionDeViandas(){
        this.viandas= new ArrayList<>();
    }

    public DonacionDeViandas(TipoDePersona tipo, LocalDate fecha, Integer cantidad) {
        this.setTiposDePersona(tipo);
        this.setFecha(fecha);
        this.cantidadDeViandas = cantidad;
    }

    public DonacionDeViandas(List<Vianda> viandas){
        this.viandas= viandas;
    }

    @Override
    public void ejecutarContribucion() {
        super.ejecutarContribucion();
        //TODO guarda en la DB las viandas
        try {
            colaborador.getTarjetaColaborador().usar(colaborador, heladera);
            // este 'registrarApertura' deberia ir dentro del metodo 'usar' de la tarjeta ??
            heladera.registrarApertura(colaborador.getTarjetaColaborador());
            heladera.actualizarCantidadViandas(cantidadDeViandas, 0);
        } catch (Exception e) {}
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
    public float getCoeficientePuntaje() {
        return 1.5f;
    }

    @Override
    public float obtenerPuntaje(){
        return cantidadDeViandas  * this.getCoeficientePuntaje();
    }
}
