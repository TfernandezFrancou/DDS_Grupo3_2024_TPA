package org.example.repositorios;

import org.example.tarjetas.Tarjeta;
import org.example.tarjetas.TarjetaColaborador;
import org.example.tarjetas.TarjetaHeladera;
import org.example.utils.BDUtils;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

public class RepoTarjetas { //TODO conectar con DB
    private List<Tarjeta> tarjetas;

    private static RepoTarjetas instancia = null;
    private RepoTarjetas() {
        this.tarjetas = new ArrayList<>();
    }

    public static RepoTarjetas getInstancia() {
        if (instancia == null) {
            RepoTarjetas.instancia = new RepoTarjetas();
        }
        return instancia;
    }

    public void agregarTodas(List<TarjetaHeladera> tarjeta) {
        this.tarjetas.addAll(tarjeta);
    }

    public void agregarTodasTarjetasColaboradores(List<TarjetaColaborador> tarjetaColaborador) {
        this.tarjetas.addAll(tarjetaColaborador);
    }

    public void agregar(Tarjeta tarjeta) {
        EntityManager em = BDUtils.getEntityManager();
        em.getTransaction().begin();
        em.persist(tarjeta);
        em.getTransaction().commit();
    }

    public void clean() {
        EntityManager em = BDUtils.getEntityManager();
        em.getTransaction().begin();
        em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();//deshabilito el check de FKs
        em.createNativeQuery("DELETE FROM Uso").executeUpdate();
        em.createNativeQuery("DELETE FROM Tarjeta").executeUpdate();
        em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();//habilito el check de FKs
        em.getTransaction().commit();
    }


    public Tarjeta buscarTarjetaPorId(String id) {
        return this.tarjetas.stream()
                .filter((t) -> t.getIdTarjeta().equals(id))
                .findFirst()
                .orElseThrow();
    }
}
