package org.example.repositorios;

import lombok.Getter;
import org.example.reportes.ReportesDeLaSemana;

import org.example.reportes.items_reportes.ItemReporteFallasPorHeladera;
import org.example.reportes.items_reportes.ItemReporteViandasColocadasPorHeladera;
import org.example.reportes.items_reportes.ItemReporteViandasDistribuidasPorColaborador;
import org.example.reportes.items_reportes.ItemReporteViandasRetiradasPorHeladera;
import org.example.utils.BDUtils;
import org.hibernate.Hibernate;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@Getter
public class RepoReportes {

    private static RepoReportes instancia = null;
    private RepoReportes() { }

    public static RepoReportes getInstancia() {
        if (instancia == null) {
            instancia = new RepoReportes();
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
               Hibernate.initialize(reporte.getReporteCantidadDeFallasPorHeladera());
                Hibernate.initialize(reporte.getReporteCantidadDeViandasColocadasPorHeladera());
                Hibernate.initialize(reporte.getReporteCantidadDeViandasRetiradasPorHeladera());
                Hibernate.initialize(reporte.getReporteCantidadDeviandasDistribuidasPorColaborador());
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
            reportes.forEach(reporte-> Hibernate.initialize(reporte.getFallas()));
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
            reportes.forEach(reporte -> Hibernate.initialize(reporte.getViandasColocadas()));
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
            reportes.forEach(reporte-> Hibernate.initialize(reporte.getViandasDistribuidas()));
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
            //lazy initializations
            reportes.forEach(r -> Hibernate.initialize(r.getViandasRetiradas()));
        } finally {
            em.close();
        }
        return reportes;
    }
}

