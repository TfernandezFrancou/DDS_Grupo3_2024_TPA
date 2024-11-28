package org.example.repositorios;

import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.tarjetas.Apertura;
import org.example.tarjetas.Tarjeta;
import org.example.tarjetas.TipoDeApertura;
import org.example.utils.BDUtils;

import javax.persistence.EntityManager;
import java.util.List;

public class RepoApertura {

    private static RepoApertura instancia = null;

    public static RepoApertura getInstancia() {
        if (instancia == null) {
            RepoApertura.instancia = new RepoApertura();
        }
        return instancia;
    }

    public void agregarApertura(Apertura apertura) {
        EntityManager em = BDUtils.getEntityManager();
        em.getTransaction().begin();
        em.persist(apertura);
        em.getTransaction().commit();
    }

    public void quitarApertura(Apertura apertura) {
        EntityManager em = BDUtils.getEntityManager();
        em.getTransaction().begin();
        em.createQuery("DELETE FROM Apertura a WHERE a.idApertura=:idApertura")
                        .setParameter("idApertura", apertura.getIdApertura())
                                .executeUpdate();
        em.getTransaction().commit();
    }

    public void clean(){
        EntityManager em = BDUtils.getEntityManager();
        em.getTransaction().begin();
        em.createQuery("DELETE FROM Apertura").executeUpdate();
        em.getTransaction().commit();
    }


    public List<Apertura> obtenerAperturasFehacientes(){
        EntityManager em = BDUtils.getEntityManager();

        return em.createQuery(
                        "SELECT a FROM Apertura a " +
                                "  WHERE a.tipoDeApertura=:tipoDeApertura ", Apertura.class)
                .setParameter("tipoDeApertura", TipoDeApertura.APERTURA_FEHACIENTE)
                .getResultList();
    }

    public List<Apertura> obtenerSolicitudesDeAperturas(){

        EntityManager em = BDUtils.getEntityManager();

        return em.createQuery(
                        "SELECT a FROM Apertura a " +
                                "  WHERE a.tipoDeApertura=:tipoDeApertura ", Apertura.class)
                .setParameter("tipoDeApertura", TipoDeApertura.SOLICITUD_APERTURA)
                .getResultList();
    }
    public Apertura buscarSolicitudDeApertura(Heladera heladera, Tarjeta tarjeta){
        return this.obtenerSolicitudesDeAperturas().stream()
                .filter(solicitud -> solicitud.getHeladera().getIdHeladera() == (heladera.getIdHeladera())
                        && solicitud.getTarjeta().getIdTarjeta().equals(tarjeta.getIdTarjeta()))
                .findFirst().get();
    }

    public List<Apertura> buscarSolicitudesDeAperturaDeTarjeta(Tarjeta tarjeta){
        return this.obtenerSolicitudesDeAperturas().stream()
                .filter(solicitud -> solicitud.getTarjeta() !=null &&
                            solicitud.getTarjeta().getIdTarjeta().equals(tarjeta.getIdTarjeta()))
                .toList();
    }

    public boolean existeSolicitudDeAperturaDeTarjetaParaHeladera(Tarjeta tarjeta, Heladera heladera){

        return this.obtenerSolicitudesDeAperturas().stream()
                .anyMatch(
                        (solicitudDeApertura -> solicitudDeApertura.getHeladera() !=null &&
                                solicitudDeApertura.getTarjeta() != null &&
                                solicitudDeApertura.getHeladera().getIdHeladera() == (heladera.getIdHeladera()) &&
                                        solicitudDeApertura.getTarjeta().getIdTarjeta().equals(tarjeta.getIdTarjeta())
                        )
                );
    }

}
