package tests;

import org.example.colaboraciones.Ubicacion;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.colaboraciones.contribuciones.heladeras.MovimientoViandas;
import org.example.reportes.ItemReporteHeladera;
import org.example.repositorios.RepoHeladeras;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

    @Test
    public void testBuscarHeladerasCercanasA(){

        //creo heladeras cernacas a 10 km y heladeras no cercanas para el test
        Heladera heladera1 = new Heladera();
        heladera1.setUbicacion(new Ubicacion(-34.609722F, -58.382592F) );// Cerca de Buenos Aires

        Heladera heladera2 = new Heladera();
        heladera2.setUbicacion(new Ubicacion(-34.705722F, -58.501592F)); // Más lejos de Buenos Aires

        Heladera heladera3 = new Heladera();
        heladera3.setUbicacion(new Ubicacion(-34.603722F, -60.381592F)); // Lejos de Buenos Aires

        Heladera heladera4 = new Heladera();
        heladera4.setUbicacion(new Ubicacion(-34.600706F, -58.379833F));// Cerca de Buenos Aires

        //heladera central
        Heladera heladeraEnBuenosAires = new Heladera(); // Ejemplo en Buenos Aires
        heladeraEnBuenosAires.setUbicacion(new Ubicacion(-34.603722F, -58.381592F));

        // las agrego al repo
        repoHeladeras.agregarTodas(List.of(heladera1, heladera2, heladera3, heladera4, heladeraEnBuenosAires));

        //llamo al metodo para probarlo
        List<Heladera> heladerasCercanas = repoHeladeras.buscarHeladerasCercanasA(heladeraEnBuenosAires, 10.0);

        Assertions.assertEquals(2, heladerasCercanas.size(), "Deben ser 2 heladeras cercanas en un radio de 10 km");

        Assertions.assertTrue(heladerasCercanas.contains(heladera1));
        Assertions.assertTrue(heladerasCercanas.contains(heladera4));

        Assertions.assertFalse(heladerasCercanas.contains(heladeraEnBuenosAires), "la heladera en buenos aires no debe estar en el resultado");
    }

    @Test
    public void testSiLaDistanciaMaxiaEntreHeladerasEsCeroNoVaAVerNingunaHeladeraCercana(){

        //creo heladeras cernacas a 10 km y heladeras no cercanas para el test
        Heladera heladera1 = new Heladera();
        heladera1.setUbicacion(new Ubicacion(-34.609722F, -58.382592F) );// Cerca de Buenos Aires

        Heladera heladera2 = new Heladera();
        heladera2.setUbicacion(new Ubicacion(-34.705722F, -58.501592F)); // Más lejos de Buenos Aires

        Heladera heladera3 = new Heladera();
        heladera3.setUbicacion(new Ubicacion(-34.603722F, -60.381592F)); // Lejos de Buenos Aires

        Heladera heladera4 = new Heladera();
        heladera4.setUbicacion(new Ubicacion(-34.600706F, -58.379833F));// Cerca de Buenos Aires

        //heladera central
        Heladera heladeraEnBuenosAires = new Heladera(); // Ejemplo en Buenos Aires
        heladeraEnBuenosAires.setUbicacion(new Ubicacion(-34.603722F, -58.381592F));

        // las agrego al repo
        repoHeladeras.agregarTodas(List.of(heladera1, heladera2, heladera3, heladera4, heladeraEnBuenosAires));

        //llamo al metodo para probarlo
        List<Heladera> heladerasCercanas = repoHeladeras.buscarHeladerasCercanasA(heladeraEnBuenosAires, 0);

        Assertions.assertEquals(0, heladerasCercanas.size(), "Ninguna heladera es cercana a otra heladera con un radio de 0 km");
 }
}
