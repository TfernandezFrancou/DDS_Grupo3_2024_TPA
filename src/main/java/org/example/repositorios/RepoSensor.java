package org.example.repositorios;

import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.colaboraciones.contribuciones.heladeras.Sensor;

import javax.persistence.EntityManager;
import java.util.List;
import org.example.utils.BDUtils;
import org.hibernate.Hibernate;

public class RepoSensor {

    private static RepoSensor instancia = null;

    private RepoSensor(){ }

    public static RepoSensor getInstancia(){
        if (instancia == null) {
            instancia = new RepoSensor();
        }
        return instancia;
    }

    public Sensor buscarSensorDeTemperaturaDeHeladera(Heladera heladera){
        EntityManager em = BDUtils.getEntityManager();
        Sensor sensor = null;
        try {
            sensor =  em.createQuery("SELECT s FROM Sensor s WHERE TYPE(s) = SensorDeTemperatura AND s.heladera = :heladera", Sensor.class)
                    .setParameter("heladera", heladera)
                    .getSingleResult();
            Hibernate.initialize(sensor.getHeladera().getHistorialEstadoHeladera());//lazy initialization

        } catch (Exception e){
           e.printStackTrace();
           BDUtils.rollback(em);
        } finally {
            em.close();
        }
        return sensor;
    }

    public List<Sensor> getSensoresDeTemperatura() {
        EntityManager em = BDUtils.getEntityManager();
        List<Sensor> sensores = null;
        try {
            sensores= em.createQuery("SELECT s FROM Sensor s WHERE TYPE(s) = SensorDeTemperatura", Sensor.class)
                    .getResultList();
        } finally {
            em.close();
        }
        return sensores;
    }

    public Sensor buscarSensorPorId(int idSensor){
        EntityManager em = BDUtils.getEntityManager();
        try {
            return em.find(Sensor.class, idSensor);
        } finally {
            em.close();
        }
    }

    public void actualizarSensor(Sensor sensor){
        EntityManager em = BDUtils.getEntityManager();
        try{
            BDUtils.comenzarTransaccion(em);
            em.merge(sensor);
            em.getTransaction().commit();
        } catch (Exception e){
            e.printStackTrace();
            BDUtils.rollback(em);
            throw e;
        } finally {
            em.close();
        }
    }

    public void agregarSensor(Sensor sensor){
        EntityManager em = BDUtils.getEntityManager();
        try{
            BDUtils.comenzarTransaccion(em);
            em.persist(sensor);
            em.getTransaction().commit();
        } catch (Exception e){
            e.printStackTrace();
            BDUtils.rollback(em);
            throw e;
        } finally {
            em.close();
        }
    }

    public void eliminarSensor(Sensor sensor){
        EntityManager em = BDUtils.getEntityManager();
        try{
            BDUtils.comenzarTransaccion(em);

            int idSensor = sensor.getIdSensor();
            em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate(); //deshabilito el check de FKs

            Sensor sensor1 = em.find(Sensor.class, idSensor);
            if(sensor1 != null) {
                em.remove(sensor1);
            }

            em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate(); //habilito el check de FKs
            em.getTransaction().commit();
        } catch (Exception e){
            e.printStackTrace();
            BDUtils.rollback(em);
            throw e;
        } finally {
            em.close();
        }
    }

    public void clean(){
        EntityManager em = BDUtils.getEntityManager();
        try{
            BDUtils.comenzarTransaccion(em);
            em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate(); //deshabilito el check de FKs
            em.createNativeQuery("DELETE FROM sensor").executeUpdate();
            em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate(); //habilito el check de FKs
            em.getTransaction().commit();
        } catch (Exception e){
            e.printStackTrace();
            BDUtils.rollback(em);
            throw e;
        } finally {
            em.close();
        }
    }


}
