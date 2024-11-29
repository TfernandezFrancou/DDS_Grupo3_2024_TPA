package org.example.repositorios;

import lombok.Getter;
import org.example.reportes.ReportesDeLaSemana;

import org.example.utils.BDUtils;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@Getter
public class RepoReportes { //TODO conectar con DB
    private List<ReportesDeLaSemana> reportes;

    private static RepoReportes instancia = null;
    private RepoReportes() {
        this.reportes = new ArrayList<>();
    }

    public static RepoReportes getInstancia() {
        if (instancia == null) {
            RepoReportes.instancia = new RepoReportes();
        }
        return instancia;
    }
    public void agregarReporte(ReportesDeLaSemana nuevoReporte) {
        EntityManager em = BDUtils.getEntityManager();
        em.getTransaction().begin();
        em.persist(nuevoReporte);
        em.getTransaction().commit();
        reportes.add(nuevoReporte);
    }

    public void eliminarReporte(ReportesDeLaSemana reporte){
        EntityManager em = BDUtils.getEntityManager();
        em.getTransaction().begin();
        int idReporte = reporte.getIdReportesDeLaSemana();
        em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();//deshabilito el check de FKs

        ReportesDeLaSemana reporte1 = em.find(ReportesDeLaSemana.class, idReporte);
        if(reporte1 != null) {
            em.remove(reporte1);
            reportes.remove(reporte); // También remover de la lista local
        }

        em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();//habilito el check de FKs
        em.getTransaction().commit();
    }

    public void clean(){
        EntityManager em = BDUtils.getEntityManager();
        em.getTransaction().begin();
        em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();//deshabilito el check de FKs
        em.createNativeQuery("TRUNCATE TABLE reportesDeLaSemana").executeUpdate();
        em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();//habilito el check de FKs
        em.getTransaction().commit();
        reportes.clear();
    }
}
