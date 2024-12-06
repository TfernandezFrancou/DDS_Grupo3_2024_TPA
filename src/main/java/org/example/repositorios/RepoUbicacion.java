package org.example.repositorios;

import org.example.colaboraciones.Ubicacion;
import org.example.utils.BDUtils;
import java.util.List;

import javax.persistence.EntityManager;

public class RepoUbicacion {

    private static RepoUbicacion instancia = null;

    private RepoUbicacion() { }

    public static RepoUbicacion getInstancia() {
        if (instancia == null) {
            instancia = new RepoUbicacion();
        }
        return instancia;
    }

    public void agregar(Ubicacion ubicacion) {
        EntityManager em = BDUtils.getEntityManager();
        try{
            BDUtils.comenzarTransaccion(em);
            em.persist(ubicacion);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            BDUtils.rollback(em);
            throw  e;
        } finally {
            em.close();
        }
    }

    public void eliminar(Ubicacion ubicacion) {
        EntityManager em = BDUtils.getEntityManager();
        try{
            BDUtils.comenzarTransaccion(em);
            em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();//deshabilito el check de FK
            Ubicacion ubicacion1 = em.find(Ubicacion.class, ubicacion.getIdUbicacion());
            if (ubicacion1 != null) {
                em.remove(ubicacion1);
            }
            em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();//habilito el check de FK
            em.getTransaction().commit();
        } catch (Exception e){
            e.printStackTrace();
            BDUtils.rollback(em);
            throw e;
        } finally {
            em.close();
        }
    }

    public void clean() {
        EntityManager em = BDUtils.getEntityManager();
        try{
            BDUtils.comenzarTransaccion(em);
            em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();//deshabilito el check de FK
            em.createNativeQuery("DELETE FROM ubicacion").executeUpdate();
            em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();//habilito el check de FK
            em.getTransaction().commit();
        } catch (Exception e){
            e.printStackTrace();
            BDUtils.rollback(em);
            throw e;
        } finally {
            em.close();
        }
    }

    public List<Ubicacion> getUbicaciones() {
        EntityManager em = BDUtils.getEntityManager();
        try {
            return em.createQuery("SELECT u FROM Ubicacion u", Ubicacion.class).getResultList();
        } finally {
            em.close();
        }
    }

    public void agregarTodas(List<Ubicacion> ubicaciones) {
        EntityManager em = BDUtils.getEntityManager();
        try {
            BDUtils.comenzarTransaccion(em);
            for (Ubicacion ubicacion : ubicaciones) {
                em.persist(ubicacion);
            }
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
