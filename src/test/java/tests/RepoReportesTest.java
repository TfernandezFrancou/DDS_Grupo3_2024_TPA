package tests;

import org.example.reportes.ReportesDeLaSemana;
import org.example.repositorios.RepoReportes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class RepoReportesTest {
    private RepoReportes repoReportes;

    private ReportesDeLaSemana reporte1;
    private ReportesDeLaSemana reporte2;

    @BeforeEach
    public void setUp(){
        this.repoReportes = RepoReportes.getInstancia();
        repoReportes.clean();

        this.reporte1 = new ReportesDeLaSemana();
        this.reporte2 = new ReportesDeLaSemana();

        repoReportes.agregarReporte(reporte1);
        repoReportes.agregarReporte(reporte2);
    }

    @Test
    public void testEliminarReporte(){
        repoReportes.eliminarReporte(reporte1);
        Assertions.assertEquals(1, repoReportes.obtenerReportes().size());
    }

    @Test
    public void testClean(){
        repoReportes.clean();
        Assertions.assertEquals(0, repoReportes.obtenerReportes().size());
    }

    @Test
    public void testAgregarReporte(){
        ReportesDeLaSemana reporte3 = new ReportesDeLaSemana();
        repoReportes.agregarReporte(reporte3);
        Assertions.assertEquals(3, repoReportes.obtenerReportes().size());
    }

    @Test
    public void testGetInstancia(){
        RepoReportes repoReportes2 = RepoReportes.getInstancia();
        Assertions.assertEquals(repoReportes, repoReportes2);
    }
}
