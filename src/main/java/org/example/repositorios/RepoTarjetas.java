package org.example.repositorios;

import org.example.tarjetas.Tarjeta;
import org.example.tarjetas.TarjetaColaborador;
import org.example.tarjetas.TarjetaHeladera;
import org.example.utils.BDUtils;
import org.hibernate.Hibernate;

import javax.persistence.EntityManager;
import java.util.List;

public class RepoTarjetas {

    private static RepoTarjetas instancia = null;
    private RepoTarjetas() { }

    public static RepoTarjetas getInstancia() {
        if (instancia == null) {
            instancia = new RepoTarjetas();
        }
        return instancia;
    }

    public void agregarTodas(List<TarjetaHeladera> tarjeta) {
        EntityManager em = BDUtils.getEntityManager();
        try{
            BDUtils.comenzarTransaccion(em);
            tarjeta.forEach(em::persist);
            em.getTransaction().commit();
        }catch (Exception e){
            e.printStackTrace();
            BDUtils.rollback(em);
            throw e;
        }finally {
            em.close();
        }
    }

    public void agregarTodasTarjetasColaboradores(List<TarjetaColaborador> tarjetaColaborador) {
        EntityManager em = BDUtils.getEntityManager();
        try {
            BDUtils.comenzarTransaccion(em);//
            tarjetaColaborador.forEach(em::persist);
            em.getTransaction().commit();
        } catch (Exception e){
            e.printStackTrace();
            BDUtils.rollback(em);
            throw e;
        }finally {
            em.close();
        }
    }

    public void agregar(Tarjeta tarjeta) {
        EntityManager em = BDUtils.getEntityManager();
        try{
            BDUtils.comenzarTransaccion(em);
            em.persist(tarjeta);
            em.getTransaction().commit();
        } catch (Exception e){
            e.printStackTrace();
            BDUtils.rollback(em);
            throw e;
        }
    }

    public void clean() {
        EntityManager em = BDUtils.getEntityManager();
        try{
            BDUtils.comenzarTransaccion(em);
            em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();//deshabilito el check de FKs
            em.createNativeQuery("DELETE FROM Uso").executeUpdate();
            em.createNativeQuery("DELETE FROM Tarjeta").executeUpdate();
            em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();//habilito el check de FKs
            em.getTransaction().commit();
        } catch (Exception e){
            e.printStackTrace();
            BDUtils.rollback(em);
            throw e;
        } finally {
            em.close();
        }
    }

    public List<Tarjeta> getTarjetas() {
        EntityManager em = BDUtils.getEntityManager();
        List<Tarjeta> tarjetas  = null;
        try{
            tarjetas = em.createQuery("SELECT t FROM Tarjeta t", Tarjeta.class).getResultList();
        //lazy initializations
        tarjetas.forEach(t -> Hibernate.initialize(t.getUsos().size()));
        } finally {
            em.close();
        }
        return tarjetas;
    }

    public Tarjeta buscarTarjetaPorId(String id) {
        EntityManager em = BDUtils.getEntityManager();
        Tarjeta tarjeta = null;
        try{
           tarjeta =  em.find(Tarjeta.class, id);
            //lazy initializations
            Hibernate.initialize(tarjeta.getUsos());
        } finally {
            em.close();
        }
        return tarjeta;
    }
}
