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
public class RepoReportes { //TODO conectar con DB

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
        } finally {
            em.close();
        }
        return reportes;
    }

    public void agregarReporte(ReportesDeLaSemana nuevoReporte) {
        EntityManager em = BDUtils.getEntityManager();
        em.getTransaction().begin();
        em.persist(nuevoReporte);
        em.getTransaction().commit();
    }

    public void eliminarReporte(ReportesDeLaSemana reporte){
        EntityManager em = BDUtils.getEntityManager();
        em.getTransaction().begin();
        int idReporte = reporte.getIdReportesDeLaSemana();
        em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();//deshabilito el check de FKs

        ReportesDeLaSemana reporte1 = em.find(ReportesDeLaSemana.class, idReporte);
        if(reporte1 != null) {
            em.remove(reporte1);
        }

        em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();//habilito el check de FKs
        em.getTransaction().commit();
    }

    public void clean(){
        EntityManager em = BDUtils.getEntityManager();
        em.getTransaction().begin();
        em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();//deshabilito el check de FKs
        em.createNativeQuery("DELETE FROM reportesDeLaSemana").executeUpdate();
        em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();//habilito el check de FKs
        em.getTransaction().commit();
    }

    public List<ItemReporteFallasPorHeladera> obtenerReporteFallas() {
        EntityManager em = BDUtils.getEntityManager();
        List<ItemReporteFallasPorHeladera> reportes = new ArrayList<>();
        try {
            reportes = em.createQuery("SELECT r FROM ItemReporteFallasPorHeladera r", ItemReporteFallasPorHeladera.class).getResultList();
        } finally {
            em.close();
        }
        return reportes;
    }

    public List<ItemReporteViandasColocadasPorHeladera> obtenerReporteViandasColocadas() {
        EntityManager em = BDUtils.getEntityManager();
        List<ItemReporteViandasColocadasPorHeladera> reportes = new ArrayList<>();
        try {
            reportes = em.createQuery("SELECT r FROM ItemReporteViandasColocadasPorHeladera r", ItemReporteViandasColocadasPorHeladera.class).getResultList();
        } finally {
            em.close();
        }
        return reportes;
    }

    public List<ItemReporteViandasDistribuidasPorColaborador> obtenerReporteViandasDistribuidas() {
        EntityManager em = BDUtils.getEntityManager();
        List<ItemReporteViandasDistribuidasPorColaborador> reportes = new ArrayList<>();
        try {
            reportes = em.createQuery("SELECT r FROM ItemReporteViandasDistribuidasPorColaborador r", ItemReporteViandasDistribuidasPorColaborador.class).getResultList();
        } finally {
            em.close();
        }
        return reportes;
    }

    public List<ItemReporteViandasRetiradasPorHeladera> obtenerReporteViandasRetiradas() {
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

