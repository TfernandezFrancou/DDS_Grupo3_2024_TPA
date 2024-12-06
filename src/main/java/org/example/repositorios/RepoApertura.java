package org.example.repositorios;

import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.tarjetas.Apertura;
import org.example.tarjetas.Tarjeta;
import org.example.tarjetas.TipoDeApertura;
import org.example.utils.BDUtils;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class RepoApertura {

    private static RepoApertura instancia = null;

    public static RepoApertura getInstancia() {
        if (instancia == null) {
            instancia = new RepoApertura();
        }
        return instancia;
    }

    public void agregarApertura(Apertura apertura) {
        EntityManager em = BDUtils.getEntityManager();
        try{
            BDUtils.comenzarTransaccion(em);
            em.persist(apertura);
            em.getTransaction().commit();
        } catch (Exception e){
            e.printStackTrace();
            BDUtils.rollback(em);
            throw  e;
        }finally {
            em.close();
        }
    }

    public void quitarApertura(Apertura apertura) {
        EntityManager em = BDUtils.getEntityManager();
       try{
           BDUtils.comenzarTransaccion(em);
           Apertura resultado = em.find(Apertura.class, apertura.getIdApertura());
           if (resultado != null) {
               em.remove(resultado);
           }
           em.getTransaction().commit();
       } catch (Exception e){
           e.printStackTrace();
           BDUtils.rollback(em);
           throw  e;
       } finally {
           em.close();
       }
    }

    public void clean(){
        EntityManager em = BDUtils.getEntityManager();
        try{
            BDUtils.comenzarTransaccion(em);
            em.createQuery("DELETE FROM Apertura").executeUpdate();
            em.getTransaction().commit();
        } catch (Exception exception){
            exception.printStackTrace();
            BDUtils.rollback(em);
            throw  exception;
        }finally {
            em.close();
        }
    }


    public List<Apertura> obtenerAperturasFehacientes(){
        EntityManager em = BDUtils.getEntityManager();
        List<Apertura> result = null;
        try{
            result = em.createQuery(
                            "SELECT a FROM Apertura a " +
                                    "  WHERE a.tipoDeApertura=:tipoDeApertura ", Apertura.class)
                    .setParameter("tipoDeApertura", TipoDeApertura.APERTURA_FEHACIENTE)
                    .getResultList();

        }catch (Exception e){
            e.printStackTrace();
            throw e;
        } finally {
            em.close();
        }
        return result;
    }

    public List<Apertura> obtenerSolicitudesDeAperturas(){

        EntityManager em = BDUtils.getEntityManager();
        List<Apertura> result = null;
        try{
            result= em.createQuery(
                            "SELECT a FROM Apertura a " +
                                    "  WHERE a.tipoDeApertura=:tipoDeApertura ", Apertura.class)
                    .setParameter("tipoDeApertura", TipoDeApertura.SOLICITUD_APERTURA)
                    .getResultList();
        } catch (Exception e){
            e.printStackTrace();
            throw  e;
        } finally {
            em.close();
        }

        return result;
    }
    public Apertura buscarSolicitudDeApertura(Heladera heladera, Tarjeta tarjeta){
        Optional<Apertura> aperturaResultOP = this.obtenerSolicitudesDeAperturas().stream()
                .filter(solicitud -> solicitud.getHeladera().getIdHeladera() == (heladera.getIdHeladera())
                        && solicitud.getTarjeta().getIdTarjeta().equals(tarjeta.getIdTarjeta()))
                .findFirst();

        return aperturaResultOP.orElse(null);
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
