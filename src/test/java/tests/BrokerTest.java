package tests;

import org.example.broker.Broker;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.colaboraciones.contribuciones.heladeras.SensorDeTemperatura;
import org.example.repositorios.RepoSensor;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.mail.MessagingException;

import static org.mockito.ArgumentMatchers.any;

public class BrokerTest {


    @Mock
    private Heladera heladeraMock;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        RepoSensor.getInstancia().clean();
    }

    @Test
    public void testElSistemaPuedeRecibirLasTemperaturaDeHeladeras() throws MessagingException {

        RepoSensor repoSensor = RepoSensor.getInstancia();
        SensorDeTemperatura sensorDeTemperatura = new SensorDeTemperatura();
        sensorDeTemperatura.setHeladera(heladeraMock);
        repoSensor.agregarSensor(sensorDeTemperatura);

        Broker broker = new Broker();
        int temperaturaHeladera = 12;
        broker.mandarTemperaturasHeladeras(heladeraMock, temperaturaHeladera);

        //debe actualizarse el estado de la heladera
        Mockito.verify(heladeraMock, Mockito.times(1))
                    .actualizarEstadoHeladera(sensorDeTemperatura);

        //el sensor  deben tener la temperatura actualizada de la heladera
        Assert.assertEquals(temperaturaHeladera, sensorDeTemperatura.getTemperatura());
    }

    @Test
    public void testAlRecibirLaTemperaturaDeHeladeraCreaElSensorSiNoLoTiene() throws MessagingException {
        Broker broker = new Broker();
        int temperaturaHeladera = 12;
        broker.mandarTemperaturasHeladeras(heladeraMock, temperaturaHeladera);

        //debe actualizarse el estado de la heladera
        Mockito.verify(heladeraMock, Mockito.times(1))
                .actualizarEstadoHeladera(any(SensorDeTemperatura.class));

        RepoSensor repoSensor = RepoSensor.getInstancia();

        //debe haber un sensor
        Assertions.assertEquals(1, repoSensor.getSensores().size());

        //busco el sensor
        SensorDeTemperatura sensorDeTemperatura = (SensorDeTemperatura) repoSensor
                    .buscarSensorDeTemperaturaDeHeladera(heladeraMock);

        //el sensor  deben tener la temperatura actualizada de la heladera
        Assertions.assertEquals(temperaturaHeladera, sensorDeTemperatura.getTemperatura());
    }

}
