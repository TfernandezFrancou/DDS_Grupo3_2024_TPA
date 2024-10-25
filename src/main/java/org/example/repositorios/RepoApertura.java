package org.example.repositorios;

import lombok.Getter;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.tarjetas.Apertura;
import org.example.tarjetas.Tarjeta;
import org.example.tarjetas.TipoDeApertura;

import java.util.ArrayList;
import java.util.List;

public class RepoApertura {

    @Getter
    private List<Apertura> aperturas;

    private static RepoApertura instancia = null;

    private RepoApertura() {
        this.aperturas = new ArrayList<>();
    }

    public static RepoApertura getInstancia() {
        if (instancia == null) {
            RepoApertura.instancia = new RepoApertura();
        }
        return instancia;
    }

    public void agregarApertura(Apertura apertura) {
        this.aperturas.add(apertura);
    }

    public void quitarApertura(Apertura apertura) {
        this.aperturas.remove(apertura);
    }

    public void clean(){
        this.aperturas.clear();
    }


    public List<Apertura> obtenerAperturasFehacientes(){
        return this.aperturas.stream()
                .filter(apertura -> apertura.getTipoDeApertura().equals(TipoDeApertura.APERTURA_FEHACIENTE))
                .toList();
    }

    public List<Apertura> obtenerSolicitudesDeAperturas(){
        return this.aperturas.stream()
                .filter(apertura -> apertura.getTipoDeApertura().equals(TipoDeApertura.SOLICITUD_APERTURA))
                .toList();
    }
    public Apertura buscarSolicitudDeApertura(Heladera heladera, Tarjeta tarjeta){
        return this.obtenerSolicitudesDeAperturas().stream()
                .filter(solicitud -> solicitud.getHeladera().equals(heladera) && solicitud.getTarjeta().equals(tarjeta))
                .findFirst().get();
    }

    public List<Apertura> buscarSolicitudesDeAperturaDeTarjeta(Tarjeta tarjeta){
        return this.obtenerSolicitudesDeAperturas().stream()
                .filter(solicitud -> solicitud.getTarjeta().equals(tarjeta))
                .toList();
    }

    public boolean existeSolicitudDeAperturaDeTarjetaParaHeladera(Tarjeta tarjeta, Heladera heladera){

        return this.obtenerSolicitudesDeAperturas().stream()
                .anyMatch(
                        (solicitudDeApertura ->
                                solicitudDeApertura.getHeladera().equals(heladera) &&
                                        solicitudDeApertura.getTarjeta().equals(tarjeta)
                        )
                );
    }

}
