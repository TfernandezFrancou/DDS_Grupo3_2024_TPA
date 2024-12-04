package org.example.repositorios;

import org.example.tarjetas.Tarjeta;
import org.example.tarjetas.TarjetaColaborador;
import org.example.tarjetas.TarjetaHeladera;
import org.example.utils.BDUtils;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

public class RepoTarjetas {

    private static RepoTarjetas instancia = null;
    private RepoTarjetas() { }

    public static RepoTarjetas getInstancia() {
        if (instancia == null) {
            RepoTarjetas.instancia = new RepoTarjetas();
        }
        return instancia;
    }

    public void agregarTodas(List<TarjetaHeladera> tarjeta) {
        EntityManager em = BDUtils.getEntityManager();
        BDUtils.comenzarTransaccion(em);
        tarjeta.forEach(em::persist);
        em.getTransaction().commit();
    }

    public void agregarTodasTarjetasColaboradores(List<TarjetaColaborador> tarjetaColaborador) {
        EntityManager em = BDUtils.getEntityManager();
        BDUtils.comenzarTransaccion(em);//
        tarjetaColaborador.forEach(em::persist);
        em.getTransaction().commit();
    }

    public void agregar(Tarjeta tarjeta) {
        EntityManager em = BDUtils.getEntityManager();
        BDUtils.comenzarTransaccion(em);
        em.persist(tarjeta);
        em.getTransaction().commit();
    }

    public void clean() {
        EntityManager em = BDUtils.getEntityManager();
        BDUtils.comenzarTransaccion(em);
        em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();//deshabilito el check de FKs
        em.createNativeQuery("DELETE FROM Uso").executeUpdate();
        em.createNativeQuery("DELETE FROM Tarjeta").executeUpdate();
        em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();//habilito el check de FKs
        em.getTransaction().commit();
    }

    public List<Tarjeta> getTarjetas() {
        EntityManager em = BDUtils.getEntityManager();
        return em.createQuery("SELECT t FROM Tarjeta t", Tarjeta.class).getResultList();
    }

    public Tarjeta buscarTarjetaPorId(String id) {
        EntityManager em = BDUtils.getEntityManager();
        return em.find(Tarjeta.class, id);
    }
}
