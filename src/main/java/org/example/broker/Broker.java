package org.example.broker;

import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.colaboraciones.contribuciones.heladeras.SensorDeTemperatura;
import org.example.incidentes.Alerta;
import org.example.personas.Persona;
import org.example.repositorios.RepoApertura;
import org.example.repositorios.RepoIncidente;
import org.example.repositorios.RepoSensor;
import org.example.tarjetas.Apertura;

import javax.mail.MessagingException;
import java.util.NoSuchElementException;

public class Broker {

    public void gestionarSolicitudApertura(Apertura solicitudDeApertura, Persona personaColaborador){
        solicitudDeApertura.getHeladera().autorizarColaborador(personaColaborador);
        RepoApertura.getInstancia().agregarApertura(solicitudDeApertura);
    }

    public void mandarTemperaturasHeladeras(Heladera heladera, int temperatura) throws MessagingException {
        RepoSensor repoSensor = RepoSensor.getInstancia();

        try { // si tiene sensor
            SensorDeTemperatura sensor = (SensorDeTemperatura) repoSensor.buscarSensorDeTemperaturaDeHeladera(heladera);
            sensor.setTemperatura(temperatura);
            sensor.notificar();
        } catch (NoSuchElementException ex){ // si no tiene sensor de tempratura
            SensorDeTemperatura sensor = new SensorDeTemperatura();
            sensor.setHeladera(heladera);
            sensor.setTemperatura(temperatura);

            repoSensor.agregarSensor(sensor);
            sensor.notificar(); // notifico a la heladera para que actualice su estado
        }
    }

    public void gestionarAlerta(Alerta alerta) throws MessagingException {
        //aviso al técnico mas cercano y desactivo a la heladera
        alerta.reportarIncidente();

        RepoIncidente.getInstancia().agregarAlerta(alerta);
    }

}
