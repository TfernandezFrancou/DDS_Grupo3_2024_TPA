package org.example.repositorios;

import lombok.Getter;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.colaboraciones.contribuciones.heladeras.Sensor;
import org.example.colaboraciones.contribuciones.heladeras.SensorDeTemperatura;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import org.example.utils.BDUtils;

public class RepoSensor { //TODO conectar con DB

    private static RepoSensor instancia = null;

    private RepoSensor(){ }

    public static RepoSensor getInstancia(){
        if (instancia == null) {
            RepoSensor.instancia = new RepoSensor();
        }
        return instancia;
    }

    public Sensor buscarSensorDeTemperaturaDeHeladera(Heladera heladera){
        EntityManager em = BDUtils.getEntityManager();

        return em.createQuery("SELECT s FROM Sensor s WHERE s.heladera = :heladera AND TYPE(s) = SensorDeTemperatura", Sensor.class)
                .setParameter("heladera", heladera)
                .getSingleResult();
    }

    public List<Sensor> getSensoresDeTemperatura() {
        EntityManager em = BDUtils.getEntityManager();
        try {
            return em.createQuery("SELECT s FROM Sensor s WHERE TYPE(s) = SensorDeTemperatura", Sensor.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public void agregarSensor(Sensor sensor){
        EntityManager em = BDUtils.getEntityManager();
        em.getTransaction().begin();
        em.persist(sensor);
        em.getTransaction().commit();
    }

    public void eliminarSensor(Sensor sensor){
        EntityManager em = BDUtils.getEntityManager();
        em.getTransaction().begin();

        int idSensor = sensor.getIdSensor();
        em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate(); //deshabilito el check de FKs

        Sensor sensor1 = em.find(Sensor.class, idSensor);
        if(sensor1 != null) {
            em.remove(sensor1);
        }

        em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate(); //habilito el check de FKs
        em.getTransaction().commit();
    }

    public void clean(){
        EntityManager em = BDUtils.getEntityManager();
        em.getTransaction().begin();
        em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate(); //deshabilito el check de FKs
        em.createNativeQuery("DELETE FROM sensor").executeUpdate();
        em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate(); //habilito el check de FKs
        em.getTransaction().commit();
    }


}
