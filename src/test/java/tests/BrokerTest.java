package tests;

import org.example.broker.Broker;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.colaboraciones.contribuciones.heladeras.SensorDeTemperatura;
import org.example.colaboraciones.contribuciones.heladeras.TemperaturaHeladera;
import org.example.repositorios.RepoHeladeras;
import org.example.repositorios.RepoSensor;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.MockHandler;

import javax.mail.MessagingException;

import static org.mockito.ArgumentMatchers.any;

public class BrokerTest {

    private RepoSensor repoSensor;
    private RepoHeladeras repoHeladeras;
    private Heladera heladera;

    @BeforeEach
    public void setUp(){
        repoSensor = RepoSensor.getInstancia();
        repoSensor.clean();

        repoHeladeras = RepoHeladeras.getInstancia();
        repoHeladeras.clean();

        heladera = new Heladera();
        repoHeladeras.agregar(heladera);

    }

    @Test
    public void testMandarTemperaturasHeladerasSinSensorAsociado() throws MessagingException {
        Broker broker = new Broker();
        int temperatura = 8;
        broker.mandarTemperaturasHeladeras(heladera, temperatura);

        SensorDeTemperatura sensorDeTemperatura = (SensorDeTemperatura) repoSensor.buscarSensorDeTemperaturaDeHeladera(heladera);

        Assertions.assertEquals(temperatura, sensorDeTemperatura.getTemperatura());
    }

    @Test
    public void testMandarTemperaturasHeladerasConSensorAsociado() throws MessagingException {
        Broker broker = new Broker();
        int temperatura = 8;

        SensorDeTemperatura sensorDeTemperatura = new SensorDeTemperatura();
        sensorDeTemperatura.setHeladera(heladera);
        repoSensor.agregarSensor(sensorDeTemperatura);

        broker.mandarTemperaturasHeladeras(heladera, temperatura);

        sensorDeTemperatura = (SensorDeTemperatura) repoSensor.buscarSensorDeTemperaturaDeHeladera(heladera);

        Assertions.assertEquals(temperatura, sensorDeTemperatura.getTemperatura());
    }

}
