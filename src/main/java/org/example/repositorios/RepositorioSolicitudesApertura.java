package org.example.repositorios;

import org.example.tarjetas.SolicitudDeApertura;
import org.example.tarjetas.Tarjeta;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RepositorioSolicitudesApertura {
    private List<SolicitudDeApertura> solicitudesDeApertura;

    private static RepositorioSolicitudesApertura instancia = null;

    private RepositorioSolicitudesApertura() {
        this.solicitudesDeApertura = new ArrayList<>();
    }

    public static RepositorioSolicitudesApertura getInstancia() {
        if (instancia == null) {
            RepositorioSolicitudesApertura.instancia = new RepositorioSolicitudesApertura();
        }
        return instancia;
    }

    public void agregarSolicitudDeApertura(SolicitudDeApertura solicitud) {
        this.solicitudesDeApertura.add(solicitud);
    }

    public void eliminarSolicitudDeApertura(SolicitudDeApertura solicitud) {
        this.solicitudesDeApertura.remove(solicitud);
    }

    public SolicitudDeApertura buscarSolicitudDeApertura(Heladera heladera, Tarjeta tarjeta) {
        return this.solicitudesDeApertura.stream()
                .filter(solicitud -> solicitud.getHeladera().equals(heladera) && solicitud.getTarjetaColaborador().equals(tarjeta))
                .findFirst().get();
    }

    public List<SolicitudDeApertura> buscarSolicitudesDeAperturaDeTarjeta(Tarjeta tarjeta){
        return this.solicitudesDeApertura.stream()
                .filter(solicitud -> solicitud.getTarjetaColaborador().equals(tarjeta))
                .toList();
    }

    public boolean existeSolicitudDeAperturaDeTarjetaParaHeladera(Tarjeta tarjeta, Heladera heladera){

       return this.solicitudesDeApertura.stream()
               .anyMatch(
                       (solicitudDeApertura ->
                               solicitudDeApertura.getHeladera().equals(heladera) &&
                                       solicitudDeApertura.getTarjetaColaborador().equals(tarjeta)
                       )
               );
    }
}
