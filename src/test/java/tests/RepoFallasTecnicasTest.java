package tests;

import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.incidentes.FallaTecnica;
import org.example.personas.Persona;
import org.example.reportes.ItemReporteHeladera;
import org.example.repositorios.RepoIncidente;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.List;

public class RepoFallasTecnicasTest {


    private RepoIncidente repoFallasTecnicas;
    @Mock
    private Persona colaborador;

    private Heladera heladera1;

    private Heladera heladera2;

    @BeforeEach
    public void setUp(){
        this.repoFallasTecnicas  = RepoIncidente.getInstancia();

        this.heladera1 = new Heladera();
        this.heladera2 = new Heladera();
        repoFallasTecnicas.clean();
    }

    @Test
    public void testObtenerCantidadDeFallasPorHeladeraDeLaSemana() throws MessagingException {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime inicioSemana = now.with(java.time.temporal.TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY)).toLocalDate().atStartOfDay();
        LocalDateTime finSemana = now.with(java.time.temporal.TemporalAdjusters.nextOrSame(java.time.DayOfWeek.SUNDAY)).toLocalDate().atTime(23, 59, 59);

        // Agregar fallas dentro de la semana actual
        repoFallasTecnicas.agregarFalla(new FallaTecnica(colaborador,"esta todo caliente","url", heladera1,"no enfria",inicioSemana.plusDays(1))); // Martes
        repoFallasTecnicas.agregarFalla(new FallaTecnica(colaborador,"esta todo caliente","url", heladera1,"no enfria",inicioSemana.plusDays(2))); // Miércoles
        repoFallasTecnicas.agregarFalla(new FallaTecnica(colaborador,"esta todo caliente","url", heladera2,"no enfria",finSemana.minusDays(1))); // Sábado

        // Agregar fallas fuera de la semana actual
        repoFallasTecnicas.agregarFalla(new FallaTecnica(colaborador,"esta todo caliente","url", heladera1,"no enfria",inicioSemana.minusDays(1))); // Domingo anterior
        repoFallasTecnicas.agregarFalla(new FallaTecnica(colaborador,"esta todo caliente","url", heladera2,"no enfria", finSemana.plusDays(1))); // Lunes siguiente

        List<ItemReporteHeladera> reporte = repoFallasTecnicas.obtenerCantidadDeFallasPorHeladeraDeLaSemana(inicioSemana, finSemana);

        Assertions.assertEquals(2, reporte.size(), "Debe haber reportes para dos heladeras");

        for (ItemReporteHeladera item : reporte) {
            if (item.getHeladera().equals(heladera1)) {
                Assertions.assertEquals(2, item.getCantidad(), "Heladera 1 debe tener 2 fallas");
            } else if (item.getHeladera().equals(heladera2)) {
                Assertions.assertEquals(1, item.getCantidad(), "Heladera 2 debe tener 1 falla");
            } else {
                Assertions.fail("Heladera no esperada en el reporte");
            }
        }
    }
}
