package org.example.repositorios;

import org.example.tarjetas.SolicitudDeApertura;
import org.example.tarjetas.Tarjeta;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RepoSolicitudApertura {
    private List<SolicitudDeApertura> solicitudesDeApertura;

    private static RepoSolicitudApertura instancia = null;

    private RepoSolicitudApertura() {
        this.solicitudesDeApertura = new ArrayList<>();
    }

    public static RepoSolicitudApertura getInstancia() {
        if (instancia == null) {
            RepoSolicitudApertura.instancia = new RepoSolicitudApertura();
        }
        return instancia;
    }

    public void agregar(SolicitudDeApertura solicitud) {
        this.solicitudesDeApertura.add(solicitud);
    }

    public void eliminar(SolicitudDeApertura solicitud) {
        this.solicitudesDeApertura.remove(solicitud);
    }

    public Optional<SolicitudDeApertura> buscarSolicitud(Heladera heladera, Tarjeta tarjeta) {
        return this.solicitudesDeApertura.stream()
                .filter(solicitud -> solicitud.getHeladera().equals(heladera) && solicitud.getTarjetaColaborador().equals(tarjeta))
                .findFirst();
    }

//    public List<SolicitudDeApertura> buscarSolicitudDeAperturaDeTarjeta(Tarjeta tarjeta){
//        //TODO CHEQUEAR! (no entendi bien el metodo en el diagrama. hice esto que es lo que se me ocurrio que puede ser)
//        List<SolicitudDeApertura> listaADevolver = new ArrayList<>();
//        for(SolicitudDeApertura solicitudDeApertura : solicitudesDeApertura){
//            if(solicitudDeApertura.getColaborador().getTarjetaColaborador().equals(tarjeta)){
//                listaADevolver.add(solicitudDeApertura);
//            }
//        }
//        return listaADevolver;
//    }

//    public boolean existeSolicitudDeAperturaDeTarjetaParaHeladera(Tarjeta tarjeta, Heladera heladera){
//        for(SolicitudDeApertura solicitudDeApertura : solicitudesDeApertura){
//            if(solicitudDeApertura.getHeladera().equals(heladera)){
//                if(solicitudDeApertura.getColaborador().getTarjetaColaborador().equals(tarjeta)){
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
}
