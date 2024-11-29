package org.example.repositorios;

import lombok.Getter;
import org.example.personas.contacto.Mensaje;

import java.util.ArrayList;
import java.util.List;
import org.example.personas.Persona;

import org.example.personas.contacto.Mensaje;
import org.example.utils.BDUtils;

import javax.persistence.EntityManager;
import java.util.List;

public class RepoMensajes {

    private static RepoMensajes instancia = null;

    @Getter
    private List<Mensaje> mensajes;

    private RepoMensajes() {
        this.mensajes = new ArrayList<>();
    }

    public static RepoMensajes getInstancia() {
        if (instancia == null) {
            instancia = new RepoMensajes();
        }
        return instancia;
    }

    public void agregarMensaje(Mensaje mensaje) {
        EntityManager em = BDUtils.getEntityManager();
        em.getTransaction().begin();
        em.persist(mensaje);
        em.getTransaction().commit();
        this.mensajes.add(mensaje);
    }

    public void quitarMensaje(Mensaje mensaje) {
        EntityManager em = BDUtils.getEntityManager();
        try {
            em.getTransaction().begin();
            Mensaje mensajeEncontrado = em.find(Mensaje.class, mensaje.getIdMensaje());
            if (mensajeEncontrado != null) {
                em.remove(mensajeEncontrado);
                this.mensajes.remove(mensaje);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Error al quitar el mensaje", e);
        } finally {
            em.close();
        }
    }

    public List<Mensaje> obtenerMensajes() {
        EntityManager em = BDUtils.getEntityManager();
        try {
            return em.createQuery("SELECT m FROM Mensaje m", Mensaje.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public void clean(){
        EntityManager em = BDUtils.getEntityManager();
        em.getTransaction().begin();
        em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate(); //deshabilito el check de FKs
        em.createNativeQuery("TRUNCATE TABLE mensaje").executeUpdate();
        em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate(); //habilito el check de FKs
        em.getTransaction().commit();
        this.mensajes.clear();
    }

    public List<Mensaje> obtenerMensajesPorDestinatario(Persona destinatario) {
        EntityManager em = BDUtils.getEntityManager();
        try {
            return em.createQuery("SELECT m FROM Mensaje m WHERE m.destinatario = :destinatario", Mensaje.class)
                    .setParameter("destinatario", destinatario)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}