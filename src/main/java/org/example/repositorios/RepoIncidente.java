package org.example.repositorios;

import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.incidentes.Alerta;
import org.example.incidentes.FallaTecnica;
import org.example.incidentes.Incidente;
import org.example.reportes.items_reportes.ItemReporte;
import org.example.reportes.items_reportes.ItemReporteFallasPorHeladera;
import org.example.utils.BDUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RepoIncidente {

    private static RepoIncidente instancia = null;


    public static RepoIncidente getInstancia() {
        if (instancia == null) {
            instancia = new RepoIncidente();
        }
        return instancia;
    }

    public void agregarFalla(FallaTecnica fallaTecnica) {
        EntityManager em = BDUtils.getEntityManager();
        try{
            BDUtils.comenzarTransaccion(em);
            List<Heladera> heladeras = em.createQuery("from Heladera where idHeladera = :idHeladera", Heladera.class)
                    .setParameter("idHeladera",fallaTecnica.getHeladera().getIdHeladera()).getResultList();
            if(!heladeras.isEmpty()){
                fallaTecnica.setHeladera(heladeras.get(0));
            }
            em.merge(fallaTecnica);
            em.getTransaction().commit();
        } catch (Exception e){
            e.printStackTrace();
            BDUtils.rollback(em);
        }finally {
            em.close();
        }
    }

    public void agregarAlerta(Alerta alerta) {
        EntityManager em = BDUtils.getEntityManager();
        try{
            BDUtils.comenzarTransaccion(em);
            em.persist(alerta);
            em.getTransaction().commit();
        }catch (Exception e){
            e.printStackTrace();
            BDUtils.rollback(em);
        } finally {
            em.close();
        }
    }

    private List<Incidente> obtenerTodas(){
        EntityManager em = BDUtils.getEntityManager();
        List<Incidente> result = null;
        try{
            result = em.createQuery("FROM Incidente", Incidente.class).getResultList();
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            em.close();
        }
        return result;
    }

    public List<Incidente> obtenerTodasLasFallasTecnicas(){
        List<Incidente> incidentes = this.obtenerTodas();
        if(incidentes == null){
            return List.of();
        }
        return incidentes.stream()
                .filter(FallaTecnica.class::isInstance)
                .toList();
    }

    public List<Incidente> obtenerTodasLasAlertas(){
        List<Incidente> incidentes = this.obtenerTodas();
        if(incidentes == null){
            return List.of();
        }
        return incidentes.stream()
                .filter(Alerta.class::isInstance)
                .toList();
    }

    public List<Incidente> buscarPorHeladera(Integer idHeladera) {
        EntityManager em = BDUtils.getEntityManager();
        List<Incidente> result = null;
        try{
            result = em
                    .createQuery("from Incidente where heladera.idHeladera = :heladera", Incidente.class)
                    .setParameter("heladera", idHeladera)
                    .getResultList();
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            em.close();
        }
        return result;
    }

    private List<FallaTecnica> obtenerTodasLasFallasTecnicasDeHeladera(Heladera heladera, LocalDateTime inicioSemanaActual, LocalDateTime finSemanaActual){
        List<Incidente> fallas = this.obtenerTodasLasFallasTecnicas();

        return fallas.stream()
                .filter(falla -> (falla.getHeladera().getIdHeladera() == heladera.getIdHeladera()) &&
                        ((falla.getFechaDeEmision().isAfter(inicioSemanaActual) && falla.getFechaDeEmision().isBefore(finSemanaActual)
                        ) || (falla.getFechaDeEmision().equals(inicioSemanaActual) || falla.getFechaDeEmision().equals(finSemanaActual)))
                )
                .map(FallaTecnica.class::cast)           // Hago el cast a la clase FallaTecnica
                .toList();
    }

    private List<Heladera> obtenerHeladerasConFallasTecnicasEnLaSemana(LocalDateTime inicioSemanaActual, LocalDateTime finSemanaActual){
        return this.obtenerTodasLasFallasTecnicas().stream()
                .filter(falla ->
                        (falla.getFechaDeEmision().isAfter(inicioSemanaActual) && falla.getFechaDeEmision().isBefore(finSemanaActual)
                        ) || (falla.getFechaDeEmision().equals(inicioSemanaActual) || falla.getFechaDeEmision().equals(finSemanaActual))
                )
                .map(Incidente::getHeladera)
                .distinct() //elimino duplicados
                .toList();
    }

    public List<ItemReporte> obtenerCantidadDeFallasPorHeladeraDeLaSemana(LocalDateTime inicioSemanaActual, LocalDateTime finSemanaActual){

        List<Heladera> heladerasConFallas = this.obtenerHeladerasConFallasTecnicasEnLaSemana(inicioSemanaActual, finSemanaActual);


        List<ItemReporte> reporte = new ArrayList<>();
        for (Heladera heladera : heladerasConFallas) {

            ItemReporteFallasPorHeladera itemReporte = new ItemReporteFallasPorHeladera();
            itemReporte.setHeladera(heladera);
            itemReporte.setFallas(obtenerTodasLasFallasTecnicasDeHeladera(heladera, inicioSemanaActual, finSemanaActual));
            reporte.add(itemReporte);
        }

        return reporte;
    }

    public void clean() {
        EntityManager em = BDUtils.getEntityManager();
        try{
            BDUtils.comenzarTransaccion(em);
            em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();//deshabilito el check de FKs
            em.createQuery("delete from Incidente").executeUpdate();
            em.createQuery("delete from Heladera").executeUpdate();
            em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();//deshabilito el check de FKs
            em.getTransaction().commit();
        }catch (Exception e){
            e.printStackTrace();
            BDUtils.rollback(em);
        }finally {
            em.close();
        }
    }

    public FallaTecnica obtenerFallaPorId(int idFalla) {
        EntityManager em = BDUtils.getEntityManager();
        FallaTecnica falla = null;
        try {
            falla = em.find(FallaTecnica.class, idFalla);
        } finally {
            em.close();
        }
        return falla;
    }

    public Incidente obtenerIncidentePorId(int idIncidente) {
        EntityManager em = BDUtils.getEntityManager();
        Incidente incidente = null;
        try {
            incidente = em.find(Incidente.class, idIncidente);
        } finally {
            em.close();
        }
        return incidente;
    }
}
