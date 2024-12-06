package org.example.repositorios;

import org.example.colaboraciones.contribuciones.heladeras.VisitaHeladera;
import org.example.utils.BDUtils;

import javax.persistence.EntityManager;
import java.util.List;

public class RepositorioVisitasTecnicos {

    private static RepositorioVisitasTecnicos instancia = null;
    private RepositorioVisitasTecnicos() {}

    public static RepositorioVisitasTecnicos getInstancia() {
        if (instancia == null) {
            instancia = new RepositorioVisitasTecnicos();
        }
        return instancia;
    }

    public void agregarVisita(VisitaHeladera visita) {
        EntityManager em = BDUtils.getEntityManager();
        try{
            BDUtils.comenzarTransaccion(em);
            em.persist(visita);
            em.getTransaction().commit();
        } catch (Exception e){
            e.printStackTrace();
            BDUtils.rollback(em);
            throw e;
        } finally {
            em.close();
        }
    }

    public void quitarVisita(VisitaHeladera visita) {
        EntityManager em = BDUtils.getEntityManager();
        try{
            BDUtils.comenzarTransaccion(em);
            em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();//deshabilito el check de FKs
            VisitaHeladera resultado = em.find(VisitaHeladera.class, visita.getIdVisitaHeladera());
            if (resultado != null) {
                em.remove(resultado);
            }
            em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();//habilito el check de FKs
            em.getTransaction().commit();
        } catch (Exception e){
            e.printStackTrace();
            BDUtils.rollback(em);
            throw e;
        }finally {
            em.close();
        }

    }

    public void clean(){
        EntityManager em = BDUtils.getEntityManager();
        try {
            BDUtils.comenzarTransaccion(em);
            em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();//deshabilito el check de FKs
            em.createNativeQuery("DELETE FROM VisitaHeladera").executeUpdate();
            em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();//habilito el check de FKs
            em.getTransaction().commit();
        }catch (Exception e){
            e.printStackTrace();
            BDUtils.rollback(em);
            throw e;
        }finally {
            em.close();
        }
    }

    public List<VisitaHeladera> getVisitas() {
        EntityManager em = BDUtils.getEntityManager();
        try {
            return em.createQuery("FROM VisitaHeladera", VisitaHeladera.class).getResultList();
        } finally {
            em.close();
        }
    }

}