package org.example.repositorios;

import org.example.colaboraciones.contribuciones.ofertas.Oferta;
import org.example.excepciones.OfertaException;
import org.example.utils.BDUtils;

import javax.persistence.EntityManager;
import java.util.List;
import javax.persistence.NoResultException;

public class RepoOfertas {

    private static RepoOfertas instancia = null;

    private RepoOfertas() {
    }

    public static RepoOfertas getInstancia() {
        if (instancia == null) {
            instancia = new RepoOfertas();
        }
        return instancia;
    }

    public void agregarOferta(Oferta oferta) throws OfertaException {
        EntityManager em = BDUtils.getEntityManager();
        try {
            BDUtils.comenzarTransaccion(em);
            em.persist(oferta);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            BDUtils.rollback(em);
            throw new OfertaException("Error al agregar la oferta: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    public void agregarTodas(List<Oferta> ofertas) throws OfertaException {
        EntityManager em = BDUtils.getEntityManager();
        try {
            BDUtils.comenzarTransaccion(em);
            for (Oferta oferta : ofertas) {
                em.persist(oferta);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            BDUtils.rollback(em);
            throw new OfertaException("Error al agregar las ofertas: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    public void eliminarOferta(Oferta oferta) throws OfertaException {
        EntityManager em = BDUtils.getEntityManager();
        try {
            BDUtils.comenzarTransaccion(em);
            Oferta ofertaEncontrada = em.find(Oferta.class, oferta.getIdOferta());
            if (ofertaEncontrada != null) {
                em.remove(ofertaEncontrada);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            BDUtils.rollback(em);
            throw new OfertaException("Error al eliminar la oferta: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    public List<Oferta> obtenerTodas() {
        EntityManager em = BDUtils.getEntityManager();
        try {
            return em.createQuery("FROM Oferta", Oferta.class).getResultList();
        } finally {
            em.close();
        }
    }

    public Oferta buscarPorNombre(String nombreProducto) {
        EntityManager em = BDUtils.getEntityManager();
        try {
            return em.createQuery("FROM Oferta o WHERE o.nombre = :nombre", Oferta.class)
                    .setParameter("nombre", nombreProducto)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null; // Si no se encuentra ninguna oferta con ese nombre
        } finally {
            em.close();
        }
    }

    public void limpiar() throws OfertaException {
        EntityManager em = BDUtils.getEntityManager();
        try {
            BDUtils.comenzarTransaccion(em);
            em.createQuery("DELETE FROM Oferta").executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            BDUtils.rollback(em);
            throw new OfertaException("Error al limpiar el repositorio: " + e.getMessage());
        } finally {
            em.close();
        }
    }
}