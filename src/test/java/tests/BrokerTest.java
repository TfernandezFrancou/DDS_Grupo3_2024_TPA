package tests;

import org.example.broker.Broker;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.colaboraciones.contribuciones.heladeras.SensorDeTemperatura;
import org.example.excepciones.EmailNoRegistradoException;
import org.example.repositorios.RepoHeladeras;
import org.example.repositorios.RepoSensor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.mail.MessagingException;

 class BrokerTest {

    private RepoSensor repoSensor;
     private Heladera heladera;

    @BeforeEach
    public void setUp(){
        repoSensor = RepoSensor.getInstancia();
        repoSensor.clean();

        RepoHeladeras repoHeladeras = RepoHeladeras.getInstancia();
        repoHeladeras.clean();

        heladera = new Heladera();
        repoHeladeras.agregar(heladera);

    }

    @Test
    void testMandarTemperaturasHeladerasSinSensorAsociado() throws MessagingException, EmailNoRegistradoException {
        Broker broker = new Broker();
        int temperatura = 8;
        broker.mandarTemperaturasHeladeras(heladera, temperatura);

        SensorDeTemperatura sensorDeTemperatura = (SensorDeTemperatura) repoSensor.buscarSensorDeTemperaturaDeHeladera(heladera);

        Assertions.assertEquals(temperatura, sensorDeTemperatura.getTemperatura());
    }

    @Test
    void testMandarTemperaturasHeladerasConSensorAsociado() throws MessagingException, EmailNoRegistradoException {
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
