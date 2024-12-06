package org.example.repositorios;

import lombok.Getter;
import org.example.reportes.ReportesDeLaSemana;

import org.example.reportes.itemsReportes.ItemReporteFallasPorHeladera;
import org.example.reportes.itemsReportes.ItemReporteViandasColocadasPorHeladera;
import org.example.reportes.itemsReportes.ItemReporteViandasDistribuidasPorColaborador;
import org.example.reportes.itemsReportes.ItemReporteViandasRetiradasPorHeladera;
import org.example.utils.BDUtils;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@Getter
public class RepoReportes {

    private static RepoReportes instancia = null;
    private RepoReportes() { }

    public static RepoReportes getInstancia() {
        if (instancia == null) {
            RepoReportes.instancia = new RepoReportes();
        }
        return instancia;
    }

    public List<ReportesDeLaSemana> obtenerReportes() {
        EntityManager em = BDUtils.getEntityManager();
        List<ReportesDeLaSemana> reportes = new ArrayList<>();
        try {
            reportes = em.createQuery("SELECT r FROM ReportesDeLaSemana r", ReportesDeLaSemana.class).getResultList();

            // Forzar la inicialización de las colecciones LAZY
            for (ReportesDeLaSemana reporte : reportes) {
                reporte.getReporteCantidadDeFallasPorHeladera().size();
                reporte.getReporteCantidadDeViandasColocadasPorHeladera().size();
                reporte.getReporteCantidadDeViandasRetiradasPorHeladera().size();
                reporte.getReporteCantidadDeviandasDistribuidasPorColaborador().size();
            }
        } finally {
            em.close();
        }
        return reportes;
    }

    public void agregarReporte(ReportesDeLaSemana nuevoReporte) {
        EntityManager em = BDUtils.getEntityManager();
        try{
            BDUtils.comenzarTransaccion(em);
            em.persist(nuevoReporte);
            em.getTransaction().commit();
        } catch (Exception e){
            e.printStackTrace();
            BDUtils.rollback(em);
            throw  e;
        } finally {
            em.close();
        }
    }

    public void eliminarReporte(ReportesDeLaSemana reporte){
        EntityManager em = BDUtils.getEntityManager();
        try{
            BDUtils.comenzarTransaccion(em);
            int idReporte = reporte.getIdReportesDeLaSemana();
            em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();//deshabilito el check de FKs

            ReportesDeLaSemana reporte1 = em.find(ReportesDeLaSemana.class, idReporte);
            if(reporte1 != null) {
                em.remove(reporte1);
            }

            em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();//habilito el check de FKs
            em.getTransaction().commit();
        } catch (Exception e){
            e.printStackTrace();
            BDUtils.rollback(em);
            throw e;
        } finally {
            em.close();
        }
    }

    public void clean(){
        EntityManager em = BDUtils.getEntityManager();
        try{
            BDUtils.comenzarTransaccion(em);
            em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();//deshabilito el check de FKs
            em.createNativeQuery("DELETE FROM reportesDeLaSemana").executeUpdate();
            em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();//habilito el check de FKs
            em.getTransaction().commit();
        }catch (Exception e){
            e.printStackTrace();
            BDUtils.rollback(em);
            throw e;
        }finally {
            em.close();
        }
    }

    public List<ItemReporteFallasPorHeladera> obtenerFallasPorHeladera() {
        EntityManager em = BDUtils.getEntityManager();
        List<ItemReporteFallasPorHeladera> reportes = new ArrayList<>();
        try {
            reportes = em.createQuery("SELECT r FROM ItemReporteFallasPorHeladera r", ItemReporteFallasPorHeladera.class).getResultList();
            //lazy initalizations
            reportes.forEach(reporte->{
                reporte.getFallas().size();
            });
            } finally {
            em.close();
        }
        return reportes;
    }
    public List<ItemReporteViandasColocadasPorHeladera> obtenerViandasColocadasPorHeladera() {
        EntityManager em = BDUtils.getEntityManager();
        List<ItemReporteViandasColocadasPorHeladera> reportes = new ArrayList<>();
        try {
            reportes = em.createQuery("SELECT r FROM ItemReporteViandasColocadasPorHeladera r", ItemReporteViandasColocadasPorHeladera.class).getResultList();
            //lazy initializations
            reportes.forEach(reporte ->{
                reporte.getViandasColocadas().size();
            });
        } finally {
            em.close();
        }
        return reportes;
    }
    public List<ItemReporteViandasDistribuidasPorColaborador> obtenerViandasDistribuidasPorColaborador() {
        EntityManager em = BDUtils.getEntityManager();
        List<ItemReporteViandasDistribuidasPorColaborador> reportes = new ArrayList<>();
        try {
            reportes = em.createQuery("SELECT r FROM ItemReporteViandasDistribuidasPorColaborador r", ItemReporteViandasDistribuidasPorColaborador.class).getResultList();
            //lazy initializations
            reportes.forEach(reporte->{
                reporte.getViandasDistribuidas().size();
            });
        } finally {
            em.close();
        }
        return reportes;
    }
    public List<ItemReporteViandasRetiradasPorHeladera> obtenerViandasRetiradasPorHeladera() {
        EntityManager em = BDUtils.getEntityManager();
        List<ItemReporteViandasRetiradasPorHeladera> reportes = new ArrayList<>();
        try {
            reportes = em.createQuery("SELECT r FROM ItemReporteViandasRetiradasPorHeladera r", ItemReporteViandasRetiradasPorHeladera.class).getResultList();
        } finally {
            em.close();
        }
        return reportes;
    }
}

