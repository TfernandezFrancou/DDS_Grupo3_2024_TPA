package org.example.repositorios;

import org.example.Tarjetas.SolicitudDeApertura;
import org.example.Tarjetas.Tarjeta;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;

import java.util.ArrayList;
import java.util.List;

public class RepositorioSolicitudesApertura {
    private List<SolicitudDeApertura> solicitudesDeApertura;

    public List<SolicitudDeApertura> buscarSolicitudDeAperturaDeTarjeta(Tarjeta tarjeta){
        //TODO CHEQUEAR! (no entendi bien el metodo en el diagrama. hice esto que es lo que se me ocurrio que puede ser)
        List<SolicitudDeApertura> listaADevolver = new ArrayList<>();
        for(SolicitudDeApertura solicitudDeApertura : solicitudesDeApertura){
            if(solicitudDeApertura.getColaborador().getTarjetaColaborador().equals(tarjeta)){
                listaADevolver.add(solicitudDeApertura);
            }
        }
        return listaADevolver;
    }

    public boolean existeSolicitudDeAperturaDeTarjetaParaHeladera(Tarjeta tarjeta, Heladera heladera){
        for(SolicitudDeApertura solicitudDeApertura : solicitudesDeApertura){
            if(solicitudDeApertura.getHeladera().equals(heladera)){
                if(solicitudDeApertura.getColaborador().getTarjetaColaborador().equals(tarjeta)){
                    return true;
                }
            }
        }
        return false;
    }
}
