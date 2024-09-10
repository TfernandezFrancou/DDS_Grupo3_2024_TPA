package org.example.broker;

import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.incidentes.Alerta;
import org.example.personas.Persona;
import org.example.repositorios.RepoIncidente;
import org.example.repositorios.RepositorioSolicitudesApertura;
import org.example.tarjetas.SolicitudDeApertura;

import javax.mail.MessagingException;

public class Broker {

    public void gestionarSolicitudApertura(SolicitudDeApertura solicitudDeApertura, Persona personaColaborador){
        solicitudDeApertura.getHeladera().autorizarColaborador(personaColaborador);
        RepositorioSolicitudesApertura.getInstancia().agregarSolicitudDeApertura(solicitudDeApertura);
    }

    public void mandarTemperaturasHeladeras(Heladera heladera, int temperatura){
        //TODO mandarTemperaturasHeladeras(Heladera heladera, int temperatura)
    }
    public void gestionarAlerta(Alerta alerta) throws MessagingException {

        //aviso al técnico mas cercano y desactivo a la heladera
        alerta.reportarIncidente();

        RepoIncidente.getInstancia().agregarAlerta(alerta);
    }

}
