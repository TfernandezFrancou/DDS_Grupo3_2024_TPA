package org.example.colaboraciones.contribuciones;

import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.Contribucion;
import org.example.colaboraciones.TipoDePersona;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.colaboraciones.contribuciones.viandas.Vianda;
import org.example.config.Configuracion;
import org.example.excepciones.SolicitudInexistente;
import org.example.personas.roles.Colaborador;
import org.example.validadores.VerificadorAperturaHeladera;

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
    public void ejecutarContribucion() throws Exception{
        super.ejecutarContribucion();

        if(VerificadorAperturaHeladera.getInstancia().puedeAbrirHeladera(heladera, colaborador)){
            colaborador.getTarjetaColaborador().usar(colaborador, heladera);
            heladera.notificarCambioViandas(cantidadDeViandas, 0);
        }else {
           throw new SolicitudInexistente(Configuracion.obtenerProperties("mensaje.apertura-heladera.solicitud-heladera-inexistente"));
        }
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
