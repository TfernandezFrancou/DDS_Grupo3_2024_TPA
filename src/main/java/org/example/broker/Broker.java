package org.example.broker;

import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.colaboraciones.contribuciones.heladeras.SensorDeTemperatura;
import org.example.excepciones.EmailNoRegistradoException;
import org.example.incidentes.Alerta;
import org.example.personas.Persona;
import org.example.repositorios.RepoApertura;
import org.example.repositorios.RepoIncidente;
import org.example.repositorios.RepoSensor;
import org.example.tarjetas.Apertura;

import javax.mail.MessagingException;

public class Broker {

    public void gestionarSolicitudApertura(Apertura solicitudDeApertura, Persona personaColaborador){
        solicitudDeApertura.getHeladera().autorizarColaborador(personaColaborador);
        RepoApertura.getInstancia().agregarApertura(solicitudDeApertura);
    }

    public void mandarTemperaturasHeladeras(Heladera heladera, int temperatura) throws MessagingException, EmailNoRegistradoException {
        RepoSensor repoSensor = RepoSensor.getInstancia();

        SensorDeTemperatura sensor = (SensorDeTemperatura) repoSensor.buscarSensorDeTemperaturaDeHeladera(heladera);
        if (sensor != null) {
            sensor.setTemperatura(temperatura);
            repoSensor.actualizarSensor(sensor);
            sensor.notificar();
        }
        else {
            sensor = new SensorDeTemperatura();
            sensor.setHeladera(heladera);
            sensor.setTemperatura(temperatura);
            repoSensor.agregarSensor(sensor);
            sensor.notificar();
        }
    }

    public void gestionarAlerta(Alerta alerta) throws MessagingException, EmailNoRegistradoException {
        //aviso al técnico mas cercano y desactivo a la heladera
        alerta.reportarIncidente();

        RepoIncidente.getInstancia().agregarAlerta(alerta);
    }

}
