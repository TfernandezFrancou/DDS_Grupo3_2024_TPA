package tests;

import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.colaboraciones.contribuciones.heladeras.MovimientoViandas;
import org.example.incidentes.FallaTecnica;
import org.example.personas.Persona;
import org.example.reportes.ItemReporteHeladera;
import org.example.repositorios.RepoFallasTecnicas;
import org.example.repositorios.RepoHeladeras;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.util.List;

public class RepoHeladerasTest {

    private RepoHeladeras repoHeladeras;

    private Heladera heladera1;

    private Heladera heladera2;

    LocalDateTime inicioSemana ;
    LocalDateTime finSemana;


    @BeforeEach
    public void setUp(){
        this.repoHeladeras  = RepoHeladeras.getInstancia();

        repoHeladeras.clean();

        this.heladera1 = new Heladera();
        this.heladera2 = new Heladera();

        LocalDateTime now = LocalDateTime.now();
        this.inicioSemana = now.with(java.time.temporal.TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY)).toLocalDate().atStartOfDay();
        this.finSemana = now.with(java.time.temporal.TemporalAdjusters.nextOrSame(java.time.DayOfWeek.SUNDAY)).toLocalDate().atTime(23, 59, 59);
    }

    @Test
    public void testObtenerCantidadDeViandasColocadasPorHeladeraDeLaSemana(){

        // crear historial de movimientos de cada heladera

        heladera1.setHistorialMovimientos(List.of(
                new MovimientoViandas(10,0,inicioSemana.plusDays(1)),// Martes
                new MovimientoViandas(20,10,inicioSemana.plusDays(2)),// Miércoles
                new MovimientoViandas(20,10,inicioSemana.minusDays(1))// Domingo anterior
        ));

        heladera2.setHistorialMovimientos(List.of(
                new MovimientoViandas(10,0,finSemana.minusDays(1)),// Sábado
                new MovimientoViandas(100,20,finSemana.plusDays(1)) // Lunes siguiente
        ));

        // Agregar heladeras al repo
        repoHeladeras.agregar(heladera1);
        repoHeladeras.agregar(heladera2);

        List<ItemReporteHeladera> reporte = repoHeladeras.obtenerCantidadDeViandasColocadasPorHeladeraDeLaSemana(inicioSemana, finSemana);

        Assertions.assertEquals(2, reporte.size(), "Debe haber reportes para dos heladeras");

        for (ItemReporteHeladera item : reporte) {
            if (item.getHeladera().equals(heladera1)) {
                Assertions.assertEquals(30, item.getCantidad(), "Heladera 1 debe tener 40 viandas colocadas en la semana");
            } else if (item.getHeladera().equals(heladera2)) {
                Assertions.assertEquals(10, item.getCantidad(), "Heladera 2 debe tener 10 viandas colocadas en la semana");
            } else {
                Assertions.fail("Heladera no esperada en el reporte");
            }
        }
    }

    @Test
    public void testObtenerCantidadDeViandasRetiradasPorHeladeraDeLaSemana(){
        // crear historial de movimientos de cada heladera

        heladera1.setHistorialMovimientos(List.of(
                new MovimientoViandas(10,0,inicioSemana.plusDays(1)),// Martes
                new MovimientoViandas(20,10,inicioSemana.plusDays(2)),// Miércoles
                new MovimientoViandas(20,10,inicioSemana.minusDays(1))// Domingo anterior
        ));

        heladera2.setHistorialMovimientos(List.of(
                new MovimientoViandas(10,0,finSemana.minusDays(1)),// Sábado
                new MovimientoViandas(100,20,finSemana.plusDays(1)) // Lunes siguiente
        ));

        // Agregar heladeras al repo
        repoHeladeras.agregar(heladera1);
        repoHeladeras.agregar(heladera2);

        List<ItemReporteHeladera> reporte = repoHeladeras.obtenerCantidadDeViandasRetiradasPorHeladeraDeLaSemana(inicioSemana, finSemana);

        Assertions.assertEquals(2, reporte.size(), "Debe haber reportes para dos heladeras");

        for (ItemReporteHeladera item : reporte) {
            if (item.getHeladera().equals(heladera1)) {
                Assertions.assertEquals(10, item.getCantidad(), "Heladera 1 debe tener 10 viandas retiradas en la semana");
            } else if (item.getHeladera().equals(heladera2)) {
                Assertions.assertEquals(0, item.getCantidad(), "Heladera 2 debe tener 0 viandas retiradas en la semana");
            } else {
                Assertions.fail("Heladera no esperada en el reporte");
            }
        }
    }
}
