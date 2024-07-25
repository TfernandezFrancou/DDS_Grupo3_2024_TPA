package tests;

import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.personas.PersonaHumana;
import org.example.personas.roles.Colaborador;
import org.example.reportes.GeneradorDeReportes;
import org.example.reportes.ItemReporteColaborador;
import org.example.reportes.ItemReporteHeladera;
import org.example.repositorios.RepoFallasTecnicas;
import org.example.repositorios.RepoHeladeras;
import org.example.repositorios.RepoPersona;
import org.example.repositorios.RepoUsuario;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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

    private RepoFallasTecnicas repoFallasTecnicas;

    private RepoHeladeras repoHeladeras;

    private RepoPersona repoPersona;

    @BeforeEach
    public void setUp() {
        generadorDeReportes = new GeneradorDeReportes();
    }

    @Test
    public void testGenerarReportesDeLaSemana() {
        LocalDateTime now = LocalDateTime.of(2024, 7,22,13,0,0);
        LocalDateTime inicioSemana = now.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY)).toLocalDate().atStartOfDay();
        LocalDateTime finSemana = now.with(TemporalAdjusters.nextOrSame(java.time.DayOfWeek.SUNDAY)).toLocalDate().atTime(23, 59, 59);

        //creo mocks para los repos

        try(MockedStatic<RepoFallasTecnicas> mockedStaticRepoFallasTecnicas = Mockito.mockStatic(RepoFallasTecnicas.class)){
            try(MockedStatic<RepoHeladeras> mockedStaticRepoHeladeras = Mockito.mockStatic(RepoHeladeras.class)){
                try(MockedStatic<RepoPersona> mockedStaticRepoPersonas = Mockito.mockStatic(RepoPersona.class)){
                    repoFallasTecnicas = Mockito.mock(RepoFallasTecnicas.class);
                    repoHeladeras = Mockito.mock(RepoHeladeras.class);
                    repoPersona = Mockito.mock(RepoPersona.class);

                    when(RepoFallasTecnicas.getInstancia()).thenReturn(repoFallasTecnicas);
                    when(RepoHeladeras.getInstancia()).thenReturn(repoHeladeras);
                    when(RepoPersona.getInstancia()).thenReturn(repoPersona);
                    // indico que devolver cuando se llame a cada metodo

                    when(repoFallasTecnicas.obtenerCantidadDeFallasPorHeladeraDeLaSemana(inicioSemana, finSemana)).thenReturn(List.of(
                            new ItemReporteHeladera( 2, new Heladera()),
                            new ItemReporteHeladera(1,new Heladera())
                    ));


                    when(repoHeladeras.obtenerCantidadDeViandasColocadasPorHeladeraDeLaSemana(inicioSemana, finSemana)).thenReturn(List.of(
                            new ItemReporteHeladera(5, new Heladera()),
                            new ItemReporteHeladera(3, new Heladera())
                    ));

                    when(repoHeladeras.obtenerCantidadDeViandasRetiradasPorHeladeraDeLaSemana(inicioSemana, finSemana)).thenReturn(List.of(
                            new ItemReporteHeladera(4, new Heladera()),
                            new ItemReporteHeladera(2, new Heladera())
                    ));

                    when(repoPersona.obtenerCantidadDeViandasDistribuidasPorColaborador(inicioSemana, finSemana)).thenReturn(List.of(
                            new ItemReporteColaborador(4, new PersonaHumana()),
                            new ItemReporteColaborador(2, new PersonaHumana())
                    ));

                    // Genero los reportes para testear
                    generadorDeReportes.generarReportesDeLaSemana();

                    // Verificar que los métodos del repositorio fueron llamados
                    Mockito.verify(repoFallasTecnicas, Mockito.times(1)).obtenerCantidadDeFallasPorHeladeraDeLaSemana(inicioSemana, finSemana);
                    Mockito.verify(repoHeladeras, Mockito.times(1)).obtenerCantidadDeViandasColocadasPorHeladeraDeLaSemana(inicioSemana, finSemana);
                    Mockito.verify(repoHeladeras, Mockito.times(1)).obtenerCantidadDeViandasRetiradasPorHeladeraDeLaSemana(inicioSemana, finSemana);
                    Mockito.verify(repoPersona, Mockito.times(1)).obtenerCantidadDeViandasDistribuidasPorColaborador(inicioSemana, finSemana);

                    // Verificar que los reportes fueron generados correctamente
                    Assertions.assertNotNull(generadorDeReportes.getReporteCantidadDeFallasPorHeladera());
                    Assertions.assertNotNull(generadorDeReportes.getReporteCantidadDeViandasColocadasPorHeladera());
                    Assertions.assertNotNull(generadorDeReportes.getReporteCantidadDeViandasRetiradasPorHeladera());
                    Assertions.assertNotNull(generadorDeReportes.getReporteCantidadDeviandasDistribuidasPorColaborador());


                    Assertions.assertEquals(2, generadorDeReportes.getReporteCantidadDeFallasPorHeladera().size());
                    Assertions.assertEquals(2, generadorDeReportes.getReporteCantidadDeViandasColocadasPorHeladera().size());
                    Assertions.assertEquals(2, generadorDeReportes.getReporteCantidadDeViandasRetiradasPorHeladera().size());
                    Assertions.assertEquals(2, generadorDeReportes.getReporteCantidadDeviandasDistribuidasPorColaborador().size());

                }
            }

        }
    }

    @Test
    public void testGenerarReportesSemanalmente() throws InterruptedException {
        GeneradorDeReportes generadorDeReportesSpy = Mockito.spy(new GeneradorDeReportes());

        // Llamar al método que inicia el scheduler
        generadorDeReportesSpy.generarReportesSemanalmente();

        // Esperar un tiempo menor que una semana para simular el scheduler
        Thread.sleep(100);

        // Verificar que el método generarReportesDeLaSemana se haya llamado
        Mockito.verify(generadorDeReportesSpy, Mockito.times(1)).generarReportesDeLaSemana();
    }
}
