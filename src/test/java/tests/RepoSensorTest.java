package tests;

import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.colaboraciones.contribuciones.heladeras.Sensor;
import org.example.colaboraciones.contribuciones.heladeras.SensorDeTemperatura;
import org.example.repositorios.RepoSensor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class RepoSensorTest {

    @Mock
    private Heladera heladeraMock;


    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        RepoSensor.getInstancia().clean();
    }

    @Test
    public void testSePuedeEncontrarElSensorAsociadoALaHeladera(){
        RepoSensor repoSensor = RepoSensor.getInstancia();
        SensorDeTemperatura sensorDeTemperatura = new SensorDeTemperatura();
        sensorDeTemperatura.setHeladera(heladeraMock);
        repoSensor.agregarSensor(sensorDeTemperatura);

        Assertions.assertEquals(1, repoSensor.getSensores().size());

        Sensor sensorEncontrado = repoSensor.buscarSensorDeTemperaturaDeHeladera(heladeraMock);

        Assertions.assertEquals(sensorDeTemperatura, sensorEncontrado);
    }

}
