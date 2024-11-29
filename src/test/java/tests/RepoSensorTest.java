package tests;

import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.colaboraciones.contribuciones.heladeras.Sensor;
import org.example.colaboraciones.contribuciones.heladeras.SensorDeMovimiento;
import org.example.colaboraciones.contribuciones.heladeras.SensorDeTemperatura;
import org.example.repositorios.RepoSensor;
import org.example.repositorios.RepoHeladeras;
import org.example.utils.BDUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;

public class RepoSensorTest {

    private RepoSensor repoSensor;
    private Sensor sensorDeTemperatura;

    @BeforeEach
    public void setUp(){
        this.repoSensor = RepoSensor.getInstancia();
        this.repoSensor.clean();

        sensorDeTemperatura = new SensorDeTemperatura();
        this.repoSensor.agregarSensor(sensorDeTemperatura);
    }

    @Test
    public void testAgregarSensor() {
        Sensor sensorDeTemperatura2 = new SensorDeTemperatura();
        this.repoSensor.agregarSensor(sensorDeTemperatura2);

        Assertions.assertEquals(2, repoSensor.getSensores().size());
    }

    @Test
    public void testEliminarSensor(){
        this.repoSensor.eliminarSensor(sensorDeTemperatura);

        Assertions.assertEquals(0, repoSensor.getSensores().size());
    }

    @Test
    public void testGetInstancia(){
        RepoSensor repoSensor2 = RepoSensor.getInstancia();
        Assertions.assertEquals(repoSensor, repoSensor2);
    }

    @Test
    public void testGetSensoresDeTemperatura(){
        Sensor sensorDeTemperatura2 = new SensorDeTemperatura();
        this.repoSensor.agregarSensor(sensorDeTemperatura2);
        Assertions.assertEquals(2, repoSensor.getSensoresDeTemperatura().size());
    }

    @Test
    public void testBuscarSensorDeTemperaturaDeHeladera(){

        Heladera heladera = new Heladera();
        Sensor sensorDeTemperatura1 = new SensorDeTemperatura();
        sensorDeTemperatura1.setHeladera(heladera);
        this.repoSensor.agregarSensor(sensorDeTemperatura1);
        int idSensor = sensorDeTemperatura1.getIdSensor();

        Sensor sensorEncontrado = repoSensor.buscarSensorDeTemperaturaDeHeladera(heladera);

        Assertions.assertEquals(idSensor, sensorEncontrado.getIdSensor());
    }

}
