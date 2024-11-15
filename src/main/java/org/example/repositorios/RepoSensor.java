package org.example.repositorios;

import lombok.Getter;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.colaboraciones.contribuciones.heladeras.Sensor;
import org.example.colaboraciones.contribuciones.heladeras.SensorDeTemperatura;

import java.util.ArrayList;
import java.util.List;

public class RepoSensor { //TODO conectar con DB

    @Getter
    private List<Sensor> sensores;

    private static RepoSensor instancia = null;

    private RepoSensor(){
        this.sensores = new ArrayList<>();
    }

    public static RepoSensor getInstancia(){
        if (instancia == null) {
            RepoSensor.instancia = new RepoSensor();
        }
        return instancia;
    }

    public Sensor buscarSensorDeTemperaturaDeHeladera(Heladera heladera){
        return this.getSensoresDeTemperatura()
                .stream().filter(sensor -> sensor.getHeladera().equals(heladera))
                .findFirst()
                .orElseThrow();
    }
    private List<Sensor> getSensoresDeTemperatura(){
        return this.sensores.stream().filter(sensor -> sensor instanceof SensorDeTemperatura)
                .toList();
    }

    public void agregarSensor(Sensor sensor){
        this.sensores.add(sensor);
    }

    public void quitarSensor(Sensor sensor){
        this.sensores.remove(sensor);
    }

    public void clean(){
        this.sensores.clear();
    }


}
