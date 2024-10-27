package tests;

import org.example.colaboraciones.Ubicacion;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.incidentes.FallaTecnica;
import org.example.personas.Persona;
import org.example.personas.PersonaHumana;
import org.example.personas.contacto.CorreoElectronico;
import org.example.personas.contacto.Direccion;
import org.example.recomendacion.Zona;
import org.example.reportes.itemsReportes.ItemReporte;
import org.example.reportes.itemsReportes.ItemReporteFallasPorHeladera;
import org.example.repositorios.RepoIncidente;
import org.example.repositorios.RepoPersona;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;

public class RepoFallasTecnicasTest {


    private RepoIncidente repoFallasTecnicas;

    private RepoPersona repoPersona;
    @Mock
    private Persona colaborador;

    @Mock
    private Heladera heladera1;

    @Mock
    private Heladera heladera2;

    @Mock
    private PersonaHumana personaMock;

    @Mock
    private CorreoElectronico correoElectronicoMock;

    @Mock
    private Zona zonaMock;

    @Mock
    private Ubicacion ubicacionMock;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        this.repoFallasTecnicas  = RepoIncidente.getInstancia();
        this.repoPersona = RepoPersona.getInstancia();

        repoPersona.clean();
        repoFallasTecnicas.clean();
    }

    @Test
    public void testObtenerCantidadDeFallasPorHeladeraDeLaSemana() throws MessagingException {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime inicioSemana = now.with(java.time.temporal.TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY)).toLocalDate().atStartOfDay();
        LocalDateTime finSemana = now.with(java.time.temporal.TemporalAdjusters.nextOrSame(java.time.DayOfWeek.SUNDAY)).toLocalDate().atTime(23, 59, 59);

        //mockeo llamadas a las heladeras
        when(heladera1.getNombre()).thenReturn("Heladera1");

        Direccion direccionMock = new Direccion();
        direccionMock.setNombreCalle("Medrano");
        direccionMock.setAltura("321");
        when(heladera1.getDireccion()).thenReturn(direccionMock);
        when(heladera1.getUbicacion()).thenReturn(ubicacionMock);

        when(heladera2.getNombre()).thenReturn("Heladera2");
        when(heladera2.getDireccion()).thenReturn(direccionMock);
        when(heladera2.getUbicacion()).thenReturn(ubicacionMock);

        // Agregar fallas dentro de la semana actual
        repoFallasTecnicas.agregarFalla(new FallaTecnica(colaborador,"esta todo caliente","url", heladera1,"no enfria",inicioSemana.plusDays(1))); // Martes
        repoFallasTecnicas.agregarFalla(new FallaTecnica(colaborador,"esta todo caliente","url", heladera1,"no enfria",inicioSemana.plusDays(2))); // Miércoles
        repoFallasTecnicas.agregarFalla(new FallaTecnica(colaborador,"esta todo caliente","url", heladera2,"no enfria",finSemana.minusDays(1))); // Sábado

        // Agregar fallas fuera de la semana actual
        repoFallasTecnicas.agregarFalla(new FallaTecnica(colaborador,"esta todo caliente","url", heladera1,"no enfria",inicioSemana.minusDays(1))); // Domingo anterior
        repoFallasTecnicas.agregarFalla(new FallaTecnica(colaborador,"esta todo caliente","url", heladera2,"no enfria", finSemana.plusDays(1))); // Lunes siguiente

        List<ItemReporte> reporte = repoFallasTecnicas.obtenerCantidadDeFallasPorHeladeraDeLaSemana(inicioSemana, finSemana);

        Assertions.assertEquals(2, reporte.size(), "Debe haber reportes para dos heladeras");

        for (ItemReporte item : reporte) {
            ItemReporteFallasPorHeladera itemReporteFallasPorHeladera = (ItemReporteFallasPorHeladera) item;
            if (itemReporteFallasPorHeladera.getHeladera().equals(heladera1)) {
                Assertions.assertEquals(2, itemReporteFallasPorHeladera.getFallas().size(), "Heladera 1 debe tener 2 fallas");
            } else if (itemReporteFallasPorHeladera.getHeladera().equals(heladera2)) {
                Assertions.assertEquals(1, itemReporteFallasPorHeladera.getFallas().size(), "Heladera 2 debe tener 1 falla");
            } else {
                Assertions.fail("Heladera no esperada en el reporte");
            }
        }
    }
}
