package tests;

import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.colaboraciones.contribuciones.viandas.Vianda;
import org.example.incidentes.FallaTecnica;
import org.example.personas.PersonaHumana;
import org.example.reportes.GeneradorDeReportes;
import org.example.reportes.ReportesDeLaSemana;
import org.example.reportes.items_reportes.ItemReporteFallasPorHeladera;
import org.example.reportes.items_reportes.ItemReporteViandasColocadasPorHeladera;
import org.example.reportes.items_reportes.ItemReporteViandasDistribuidasPorColaborador;
import org.example.reportes.items_reportes.ItemReporteViandasRetiradasPorHeladera;
import org.example.repositorios.RepoIncidente;
import org.example.repositorios.RepoHeladeras;
import org.example.repositorios.RepoPersona;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GeneradorDeReportesTest {

    private GeneradorDeReportes generadorDeReportes;

    private RepoIncidente repoIncidente;

    private RepoHeladeras repoHeladeras;

    private RepoPersona repoPersona;

    @BeforeEach
    public void setUp() {
        generadorDeReportes = new GeneradorDeReportes();
    }



    @Test
    public void testGenerarReportesDeLaSemana() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime inicioSemana = now.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY)).toLocalDate().atStartOfDay();
        LocalDateTime finSemana = now.with(TemporalAdjusters.nextOrSame(java.time.DayOfWeek.SUNDAY)).toLocalDate().atTime(23, 59, 59);

        //creo mocks para los repos

        try(MockedStatic<RepoIncidente> mockedStaticRepoFallasTecnicas = Mockito.mockStatic(RepoIncidente.class)){
            try(MockedStatic<RepoHeladeras> mockedStaticRepoHeladeras = Mockito.mockStatic(RepoHeladeras.class)){
                try(MockedStatic<RepoPersona> mockedStaticRepoPersonas = Mockito.mockStatic(RepoPersona.class)){
                    repoIncidente = Mockito.mock(RepoIncidente.class);
                    repoHeladeras = Mockito.mock(RepoHeladeras.class);
                    repoPersona = Mockito.mock(RepoPersona.class);

                    when(RepoIncidente.getInstancia()).thenReturn(repoIncidente);
                    when(RepoHeladeras.getInstancia()).thenReturn(repoHeladeras);
                    when(RepoPersona.getInstancia()).thenReturn(repoPersona);
                    // indico que devolver cuando se llame a cada metodo

                    Heladera heladeraRandom = new Heladera();
                    FallaTecnica mockFallaTecnica = new FallaTecnica(heladeraRandom,"falla",LocalDateTime.now());
                    when(repoIncidente.obtenerCantidadDeFallasPorHeladeraDeLaSemana(inicioSemana, finSemana)).thenReturn(List.of(
                            new ItemReporteFallasPorHeladera( List.of(mockFallaTecnica, mockFallaTecnica), heladeraRandom),
                            new ItemReporteFallasPorHeladera(List.of(mockFallaTecnica),heladeraRandom)
                    ));

                    Vianda viandaMock = new Vianda();
                    when(repoHeladeras.obtenerCantidadDeViandasColocadasPorHeladeraDeLaSemana(inicioSemana, finSemana)).thenReturn(List.of(
                            new ItemReporteViandasColocadasPorHeladera(List.of(viandaMock,viandaMock,viandaMock,viandaMock,viandaMock), heladeraRandom),//5
                            new ItemReporteViandasColocadasPorHeladera(List.of(viandaMock,viandaMock,viandaMock), heladeraRandom)//3
                    ));

                    when(repoHeladeras.obtenerCantidadDeViandasRetiradasPorHeladeraDeLaSemana(inicioSemana, finSemana)).thenReturn(List.of(
                            new ItemReporteViandasRetiradasPorHeladera(List.of(viandaMock,viandaMock,viandaMock,viandaMock), heladeraRandom),
                            new ItemReporteViandasRetiradasPorHeladera(List.of(viandaMock,viandaMock), heladeraRandom)
                    ));

                    PersonaHumana colaboradorMock = new PersonaHumana();
                    when(repoPersona.obtenerCantidadDeViandasDistribuidasPorColaborador(inicioSemana, finSemana)).thenReturn(List.of(
                            new ItemReporteViandasDistribuidasPorColaborador(List.of(viandaMock,viandaMock,viandaMock,viandaMock), colaboradorMock),
                            new ItemReporteViandasDistribuidasPorColaborador(List.of(viandaMock,viandaMock), colaboradorMock)
                    ));

                    // Genero los reportes para testear
                    generadorDeReportes.generarReportesDeLaSemana();

                    // Verificar que los métodos del repositorio fueron llamados
                    Mockito.verify(repoIncidente, Mockito.times(1)).obtenerCantidadDeFallasPorHeladeraDeLaSemana(inicioSemana, finSemana);
                    Mockito.verify(repoHeladeras, Mockito.times(1)).obtenerCantidadDeViandasColocadasPorHeladeraDeLaSemana(inicioSemana, finSemana);
                    Mockito.verify(repoHeladeras, Mockito.times(1)).obtenerCantidadDeViandasRetiradasPorHeladeraDeLaSemana(inicioSemana, finSemana);
                    Mockito.verify(repoPersona, Mockito.times(1)).obtenerCantidadDeViandasDistribuidasPorColaborador(inicioSemana, finSemana);

                    ReportesDeLaSemana reportes = generadorDeReportes.getReportesSemanaActual();
                    // Verificar que los reportes fueron generados correctamente
                    Assertions.assertNotNull(reportes.getReporteCantidadDeFallasPorHeladera());
                    Assertions.assertNotNull(reportes.getReporteCantidadDeViandasColocadasPorHeladera());
                    Assertions.assertNotNull(reportes.getReporteCantidadDeViandasRetiradasPorHeladera());
                    Assertions.assertNotNull(reportes.getReporteCantidadDeviandasDistribuidasPorColaborador());


                    Assertions.assertEquals(2, reportes.getReporteCantidadDeFallasPorHeladera().size());
                    Assertions.assertEquals(2, reportes.getReporteCantidadDeViandasColocadasPorHeladera().size());
                    Assertions.assertEquals(2, reportes.getReporteCantidadDeViandasRetiradasPorHeladera().size());
                    Assertions.assertEquals(2, reportes.getReporteCantidadDeviandasDistribuidasPorColaborador().size());

                }
            }

        }
    }

    @Test
    void testGenerarReportesSemanalmente() throws InterruptedException {
        GeneradorDeReportes generadorDeReportesSpy = Mockito.spy(new GeneradorDeReportes());

        // Llamar al método que inicia el scheduler
        generadorDeReportesSpy.generarReportesSemanalmente();

        // Esperar un tiempo menor que una semana para simular el scheduler
        Thread.sleep(100);

        // Verificar que el método generarReportesDeLaSemana se haya llamado
        Mockito.verify(generadorDeReportesSpy, Mockito.times(1)).generarReportesDeLaSemana();
    }
}
