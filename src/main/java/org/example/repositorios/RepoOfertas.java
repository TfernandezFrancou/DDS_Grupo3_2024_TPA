package org.example.repositorios;

import org.example.colaboraciones.contribuciones.ofertas.Oferta;
import org.example.utils.BDUtils;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

public class RepoOfertas { //TODO conectar con DB
    private List<Oferta> ofertas;

    private static RepoOfertas instancia = null;

    private RepoOfertas() {
        this.ofertas = new ArrayList<>();
    }

    public static RepoOfertas getInstancia() {
        if (instancia == null) {
            RepoOfertas.instancia = new RepoOfertas();
        }
        return instancia;
    }

    public void agregarOferta(Oferta oferta) {
        EntityManager em = BDUtils.getEntityManager();
        em.getTransaction().begin();
        em.persist(oferta);
        em.getTransaction().commit();
    }

    public void agregarTodas(List<Oferta> ofertas) {
        this.ofertas.addAll(ofertas);
    }

    public void eliminarOferta(Oferta oferta) {
        this.ofertas.remove(oferta);
    }

    public List<Oferta> obtenerTodas() {
        return BDUtils
                .getEntityManager()
                .createQuery("from Oferta", Oferta.class)
                .getResultList();
    }
}
