package org.example.repositorios;

import org.example.colaboraciones.Contribucion;
import org.example.colaboraciones.contribuciones.*;
import org.example.utils.BDUtils;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

public class RepoContribucion {

    private static RepoContribucion instancia = null;

    public static RepoContribucion getInstancia() {
        if (instancia == null) {
            RepoContribucion.instancia = new RepoContribucion();
        }
        return instancia;
    }

    public void agregarContribucion(Contribucion contribucion) {
        EntityManager em = BDUtils.getEntityManager();
        em.getTransaction().begin();
        em.persist(contribucion);
        em.getTransaction().commit();
    }

    public void eliminarContribucion(Contribucion contribucion) {
        EntityManager em = BDUtils.getEntityManager();
        em.getTransaction().begin();
        int idContribucion =contribucion.getIdContribucion();
        em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();//deshabilito el check de FKs

        DonacionDeViandas ddv = em.find(DonacionDeViandas.class, idContribucion);
        if(ddv != null)
            em.remove(ddv);

        DonacionDeDinero ddd = em.find(DonacionDeDinero.class, idContribucion);
        if(ddd != null)
            em.remove(ddd);

        HacerseCargoDeUnaHeladera hcdh = em.find(HacerseCargoDeUnaHeladera.class, idContribucion);
        if(hcdh != null)
            em.remove(hcdh);

        OfrecerProductos op = em.find(OfrecerProductos.class, idContribucion);
        if(op != null)
            em.remove(op);

        RegistrarPersonasEnSituacionVulnerable rpsv = em.find(RegistrarPersonasEnSituacionVulnerable.class, idContribucion);
        if(rpsv != null)
            em.remove(rpsv);

        Contribucion c = em.find(Contribucion.class, idContribucion);
        if(c != null)
            em.remove(c);

        em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();//habilito el check de FKs
        em.getTransaction().commit();
    }

    public List<Contribucion> obtenerContribuciones(){
        EntityManager em = BDUtils.getEntityManager();
        return em.createQuery("SELECT c FROM Contribucion c", Contribucion.class)
                .getResultList();
    }

    public List<Contribucion> obtenerContribucionesPorPersona(int idPersona){
        // TODO: testear
        EntityManager em = BDUtils.getEntityManager();
        return em.createQuery("SELECT c FROM Contribucion c LEFT JOIN Persona p on c.colaborador = p.rol where p.idPersona = :idPersona", Contribucion.class)
                .setParameter("idPersona", idPersona)
                .getResultList();
    }

    public void clean(){
        EntityManager em = BDUtils.getEntityManager();
        em.getTransaction().begin();//deshabilito el check de FKs
        em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();
        em.createNativeQuery("DELETE FROM DistribucionDeViandas_Vianda").executeUpdate();
        em.createNativeQuery("DELETE FROM DistribucionDeViandas").executeUpdate();
        em.createNativeQuery("DELETE FROM DonacionDeViandas_Vianda").executeUpdate();
        em.createNativeQuery("DELETE FROM DonacionDeViandas").executeUpdate();
        em.createNativeQuery("DELETE FROM DonacionDeDinero").executeUpdate();
        em.createNativeQuery("DELETE FROM HacerseCargoDeUnaHeladera").executeUpdate();
        em.createNativeQuery("DELETE FROM OfrecerProductos").executeUpdate();
        em.createNativeQuery("DELETE FROM RegistrarPersonasEnSituacionVulnerable").executeUpdate();
        em.createNativeQuery("DELETE FROM Contribucion").executeUpdate();
        em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();//habilito el check de FKs
        em.getTransaction().commit();
    }
}
