package org.example.repositorios;

import org.example.personas.contacto.Mensaje;


import java.util.List;
import org.example.personas.Persona;
;
import org.example.utils.BDUtils;

import javax.persistence.EntityManager;


public class RepoMensajes {

    private static RepoMensajes instancia = null;

    private RepoMensajes() { }

    public static RepoMensajes getInstancia() {
        if (instancia == null) {
            instancia = new RepoMensajes();
        }
        return instancia;
    }

    public void agregarMensaje(Mensaje mensaje) {
        EntityManager em = BDUtils.getEntityManager();
        try{
            BDUtils.comenzarTransaccion(em);
            em.persist(mensaje);
            em.getTransaction().commit();
        } catch (Exception e){
            e.printStackTrace();
            BDUtils.rollback(em);
        } finally {
            em.close();
        }
    }

    public void quitarMensaje(Mensaje mensaje) {
        EntityManager em = BDUtils.getEntityManager();
        try {
            BDUtils.comenzarTransaccion(em);
            Mensaje mensajeEncontrado = em.find(Mensaje.class, mensaje.getIdMensaje());
            if (mensajeEncontrado != null) {
                em.remove(mensajeEncontrado);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            BDUtils.rollback(em);
            throw new RuntimeException("Error al quitar el mensaje", e);
        } finally {
            em.close();
        }
    }

    public List<Mensaje> obtenerMensajes() {
        EntityManager em = BDUtils.getEntityManager();
        List<Mensaje> result = null;
        try {
            result =  em.createQuery("SELECT m FROM Mensaje m", Mensaje.class)
                    .getResultList();
        } catch (Exception e){
            e.printStackTrace();
        }finally {
            em.close();
        }
        return result;
    }

    public void clean(){
        EntityManager em = BDUtils.getEntityManager();
        try{
            BDUtils.comenzarTransaccion(em);
            em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate(); //deshabilito el check de FKs
            em.createNativeQuery("DELETE FROM mensaje").executeUpdate();
            em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate(); //habilito el check de FKs
            em.getTransaction().commit();
        } catch (Exception e){
            e.printStackTrace();
            BDUtils.rollback(em);
        } finally {
            em.close();
        }
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