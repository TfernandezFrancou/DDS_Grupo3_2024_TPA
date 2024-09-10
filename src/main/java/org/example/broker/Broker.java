package org.example.broker;

import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.incidentes.Alerta;
import org.example.repositorios.RepoIncidente;
import org.example.repositorios.RepositorioSolicitudesApertura;
import org.example.tarjetas.SolicitudDeApertura;

public class Broker {

    public void gestionarSolicitudApertura(SolicitudDeApertura solicitudDeApertura){
        //TODO gestionarSolicitudApertura(SolicitudDeApertura solicitudDeApertura)
        RepositorioSolicitudesApertura.getInstancia().agregarSolicitudDeApertura(solicitudDeApertura);
    }

    public void mandarTemperaturasHeladeras(Heladera heladera, int temperatura){
        //todo mandarTemperaturasHeladeras(Heladera heladera, int temperatura)
    }
    public void gestionarAlerta(Alerta alerta){
        //TODO gestionarAlerta(Alerta alerta)
        RepoIncidente.getInstancia().agregarAlerta(alerta);
    }

}
