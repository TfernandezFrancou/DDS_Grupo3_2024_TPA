package tests;

import org.example.colaboraciones.Ubicacion;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.colaboraciones.contribuciones.heladeras.MovimientoViandas;
import org.example.colaboraciones.contribuciones.viandas.Vianda;
import org.example.reportes.items_reportes.ItemReporte;
import org.example.reportes.items_reportes.ItemReporteViandasColocadasPorHeladera;
import org.example.reportes.items_reportes.ItemReporteViandasRetiradasPorHeladera;
import org.example.repositorios.RepoHeladeras;
import org.example.repositorios.RepoUbicacion;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

public class RepoHeladerasTest {

    private RepoHeladeras repoHeladeras;

    private Heladera heladera1;

    private Heladera heladera2;

    private RepoUbicacion repoUbicacion;

    LocalDateTime inicioSemana ;
    LocalDateTime finSemana;


    @BeforeEach
    public void setUp(){
        this.repoHeladeras  = RepoHeladeras.getInstancia();
        repoHeladeras.clean();
        this.repoUbicacion = RepoUbicacion.getInstancia();
        repoUbicacion.clean();


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
                new MovimientoViandas(List.of(new Vianda(), new Vianda(), new Vianda()),List.of(),inicioSemana.plusDays(1)),// Martes
                new MovimientoViandas(List.of(new Vianda(), new Vianda()),List.of(new Vianda(), new Vianda(), new Vianda()),inicioSemana.plusDays(2)),// Miércoles
                new MovimientoViandas(List.of(new Vianda()),List.of(new Vianda()),inicioSemana.minusDays(1))// Domingo anterior
        ));

        heladera2.setHistorialMovimientos(List.of(
                new MovimientoViandas(List.of(new Vianda(), new Vianda(), new Vianda()),List.of(),finSemana.minusDays(1)),// Sábado
                new MovimientoViandas(List.of(new Vianda()),List.of(new Vianda(), new Vianda(), new Vianda()),finSemana.plusDays(1)) // Lunes siguiente
        ));

        // Agregar heladeras al repo
        repoHeladeras.agregar(heladera1);
        repoHeladeras.agregar(heladera2);

        List<ItemReporte> reporte = repoHeladeras.obtenerCantidadDeViandasColocadasPorHeladeraDeLaSemana(inicioSemana, finSemana);

        Assertions.assertEquals(2, reporte.size(), "Debe haber reportes para dos heladeras");

        for (ItemReporte item : reporte) {
            ItemReporteViandasColocadasPorHeladera itemReporteViandasColocadasPorHeladera = (ItemReporteViandasColocadasPorHeladera) item;
            if (itemReporteViandasColocadasPorHeladera.getHeladera().getIdHeladera() == heladera1.getIdHeladera()) {

                Assertions.assertEquals(5, itemReporteViandasColocadasPorHeladera.getViandasColocadas().size(), "Heladera 1 debe tener 5 viandas colocadas en la semana");
            } else if (itemReporteViandasColocadasPorHeladera.getHeladera().getIdHeladera() == heladera2.getIdHeladera()) {
                Assertions.assertEquals(3, itemReporteViandasColocadasPorHeladera.getViandasColocadas().size(), "Heladera 2 debe tener 3 viandas colocadas en la semana");
            } else {
                Assertions.fail("Heladera no esperada en el reporte");
            }
        }
    }

    @Test
    public void testObtenerCantidadDeViandasRetiradasPorHeladeraDeLaSemana(){
        //repoHeladeras.clean();
        // crear historial de movimientos de cada heladera

        heladera1.setHistorialMovimientos(List.of(
                new MovimientoViandas(List.of(new Vianda(), new Vianda(), new Vianda()),List.of(),inicioSemana.plusDays(1)),// Martes
                new MovimientoViandas(List.of(new Vianda(), new Vianda()),List.of(new Vianda("vianda 1 retirada",12,33),
                        new Vianda("vianda 2 retirada",21,66), new Vianda("vianda 3 retirada",41,55)),inicioSemana.plusDays(2)),// Miércoles
                new MovimientoViandas(List.of(new Vianda()),List.of(new Vianda("vianda x retirada", 55, 100)),inicioSemana.minusDays(1))// Domingo anterior
        ));

        heladera2.setHistorialMovimientos(List.of(
                new MovimientoViandas(List.of(new Vianda(), new Vianda(), new Vianda()),List.of(),finSemana.minusDays(1)),// Sábado
                new MovimientoViandas(List.of(new Vianda()),List.of(new Vianda(), new Vianda(), new Vianda()),finSemana.plusDays(1)) // Lunes siguiente
        ));

        // Agregar heladeras al repo
        repoHeladeras.agregar(heladera1);
        repoHeladeras.agregar(heladera2);

        List<ItemReporte> reporte = repoHeladeras.obtenerCantidadDeViandasRetiradasPorHeladeraDeLaSemana(inicioSemana, finSemana);

        Assertions.assertEquals(2, reporte.size(), "Debe haber reportes para dos heladeras");

        for (ItemReporte item : reporte) {
            ItemReporteViandasRetiradasPorHeladera itemReporteViandasRetiradasPorHeladera =(ItemReporteViandasRetiradasPorHeladera) item;
            if (itemReporteViandasRetiradasPorHeladera.getHeladera().getIdHeladera() == heladera1.getIdHeladera()) {
                Assertions.assertEquals(3, itemReporteViandasRetiradasPorHeladera.getViandasRetiradas().size(), "Heladera 1 debe tener 3 viandas retiradas en la semana");
            } else if (itemReporteViandasRetiradasPorHeladera.getHeladera().getIdHeladera() == heladera2.getIdHeladera()) {
                Assertions.assertEquals(0, itemReporteViandasRetiradasPorHeladera.getViandasRetiradas().size(), "Heladera 2 debe tener 0 viandas retiradas en la semana");
            } else {
                Assertions.fail("Heladera no esperada en el reporte");
            }
        }
    }

    @Test
    public void testBuscarHeladerasCercanasA(){
        Ubicacion ubicacion1 = new Ubicacion(-34.609722F, -58.382592F);
        Ubicacion ubicacion2 = new Ubicacion(-34.705722F, -58.501592F);
        Ubicacion ubicacion3 = new Ubicacion(-34.603722F, -60.381592F);
        Ubicacion ubicacion4 = new Ubicacion(-34.600706F, -58.379833F);
        repoUbicacion.agregarTodas(List.of(ubicacion1, ubicacion2, ubicacion3, ubicacion4));

       // repoHeladeras.clean();

        //creo heladeras cernacas a 10 km y heladeras no cercanas para el test
        Heladera heladera1 = new Heladera();
        heladera1.setUbicacion(ubicacion1);// Cerca de Buenos Aires

        Heladera heladera2 = new Heladera();
        heladera2.setUbicacion(ubicacion2); // Más lejos de Buenos Aires

        Heladera heladera3 = new Heladera();
        heladera3.setUbicacion(ubicacion3); // Lejos de Buenos Aires

        Heladera heladera4 = new Heladera();
        heladera4.setUbicacion(ubicacion4);// Cerca de Buenos Aires

        //heladera central
        Heladera heladeraEnBuenosAires = new Heladera(); // Ejemplo en Buenos Aires
        Ubicacion ubicacionEnBuenosAires = new Ubicacion(-34.603722F, -58.381592F);
        repoUbicacion.agregar(ubicacionEnBuenosAires);
        heladeraEnBuenosAires.setUbicacion(ubicacionEnBuenosAires);

        // las agrego al repo
        repoHeladeras.agregarTodas(List.of(heladera1, heladera2, heladera3, heladera4, heladeraEnBuenosAires));

        //llamo al metodo para probarlo
        List<Heladera> heladerasCercanas = repoHeladeras.buscarHeladerasCercanasA(heladeraEnBuenosAires, 10.0);

        Assertions.assertEquals(2, heladerasCercanas.size(), "Deben ser 2 heladeras cercanas en un radio de 10 km");

        List<Integer> idsHeladerasCercanas = heladerasCercanas.stream().map(Heladera::getIdHeladera).toList();
        Assertions.assertTrue(idsHeladerasCercanas.contains(heladera1.getIdHeladera()));
        Assertions.assertTrue(idsHeladerasCercanas.contains(heladera4.getIdHeladera()));

        Assertions.assertFalse(heladerasCercanas.contains(heladeraEnBuenosAires), "la heladera en buenos aires no debe estar en el resultado");
    }

    @Test
    public void testSiLaDistanciaMaxiaEntreHeladerasEsCeroNoVaAVerNingunaHeladeraCercana(){
        Ubicacion ubicacion1 = new Ubicacion(-34.609722F, -58.382592F);
        Ubicacion ubicacion2 = new Ubicacion(-34.705722F, -58.501592F);
        Ubicacion ubicacion3 = new Ubicacion(-34.603722F, -60.381592F);
        Ubicacion ubicacion4 = new Ubicacion(-34.600706F, -58.379833F);
        List<Ubicacion> ubicaciones = List.of(ubicacion1, ubicacion2, ubicacion3, ubicacion4);
        repoUbicacion.agregarTodas(ubicaciones);
       // repoHeladeras.clean();
        //creo heladeras cernacas a 10 km y heladeras no cercanas para el test
        Heladera heladera1 = new Heladera();
        heladera1.setUbicacion(ubicacion1);// Cerca de Buenos Aires

        Heladera heladera2 = new Heladera();
        heladera2.setUbicacion(ubicacion2); // Más lejos de Buenos Aires

        Heladera heladera3 = new Heladera();
        heladera3.setUbicacion(ubicacion3); // Lejos de Buenos Aires

        Heladera heladera4 = new Heladera();
        heladera4.setUbicacion(ubicacion4);// Cerca de Buenos Aires

        //heladera central
        Heladera heladeraEnBuenosAires = new Heladera(); // Ejemplo en Buenos Aires
        Ubicacion ubicacionEnBuenosAires = new Ubicacion(-34.603722F, -58.381592F);
        repoUbicacion.agregar(ubicacionEnBuenosAires);
        heladeraEnBuenosAires.setUbicacion(ubicacionEnBuenosAires);

        // las agrego al repo
        repoHeladeras.agregarTodas(List.of(heladera1, heladera2, heladera3, heladera4, heladeraEnBuenosAires));

        //llamo al metodo para probarlo
        List<Heladera> heladerasCercanas = repoHeladeras.buscarHeladerasCercanasA(heladeraEnBuenosAires, 0);

        Assertions.assertEquals(0, heladerasCercanas.size(), "Ninguna heladera es cercana a otra heladera con un radio de 0 km");
 }
}
