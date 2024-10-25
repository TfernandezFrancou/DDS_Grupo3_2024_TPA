package tests.contribuciones;

import org.example.colaboraciones.contribuciones.DistribucionDeViandas;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.colaboraciones.contribuciones.viandas.Vianda;
import org.example.excepciones.SolicitudInexistente;
import org.example.personas.PersonaHumana;
import org.example.personas.roles.Colaborador;
import org.example.repositorios.RepoApertura;
import org.example.tarjetas.Apertura;
import org.example.tarjetas.TarjetaColaborador;
import org.example.tarjetas.TipoDeApertura;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;

public class DistribucionDeViandasTest {


    @Mock
    private Vianda vianda1;

    @Mock
    private Vianda vianda2;

    @Mock
    private Colaborador colaboradorMock;

    @Mock
    private Heladera heladeraMockOrigen;
    @Mock
    private Heladera heladeraMockDestino;

    @Mock
    private Vianda viandaMock;

    @Mock
    private TarjetaColaborador tarjetaColaboradorMock;

    @Mock
    private PersonaHumana personaHumanaMock;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);//crea los mocks
        RepoApertura.getInstancia().clean();
    }

    private DistribucionDeViandas crearDristribucionDeViandas(){
        DistribucionDeViandas distribucionDeViandasMock = new DistribucionDeViandas(
                LocalDate.now(),
                List.of(viandaMock, viandaMock)
        );
        distribucionDeViandasMock.setColaborador(colaboradorMock);
        distribucionDeViandasMock.setOrigen(heladeraMockOrigen);
        distribucionDeViandasMock.setDestino(heladeraMockDestino);

        when(heladeraMockOrigen.estaActiva()).thenReturn(true);
        when(heladeraMockOrigen.puedeAbrirHeladera(personaHumanaMock)).thenReturn(true);

        when(heladeraMockDestino.estaActiva()).thenReturn(true);
        when(heladeraMockDestino.puedeAbrirHeladera(personaHumanaMock)).thenReturn(true);

        when(colaboradorMock.getTarjetaColaborador()).thenReturn(tarjetaColaboradorMock);
        when(colaboradorMock.getPersona()).thenReturn(personaHumanaMock);

        return distribucionDeViandasMock;
    }

    @Test
    public void testPuedeDistribuirViandas() throws Exception {
        DistribucionDeViandas distribucionDeViandasMock = this.crearDristribucionDeViandas();

        RepoApertura repo = RepoApertura.getInstancia();

        repo.agregarApertura(new Apertura(tarjetaColaboradorMock, heladeraMockOrigen, LocalDateTime.now(), TipoDeApertura.SOLICITUD_APERTURA));
        repo.agregarApertura(new Apertura(tarjetaColaboradorMock, heladeraMockDestino, LocalDateTime.now(),TipoDeApertura.SOLICITUD_APERTURA));

        // no debe tirar error al ejecutar la donacion
        Assertions.assertDoesNotThrow(distribucionDeViandasMock::ejecutarContribucion);

        //verifico que se notifiquen el cambio de viandas a las heladeras correspondientes
        Mockito.verify(heladeraMockOrigen, Mockito.times(1)).notificarCambioViandas(List.of(), List.of(viandaMock, viandaMock));
        Mockito.verify(heladeraMockDestino, Mockito.times(1)).notificarCambioViandas(List.of(viandaMock, viandaMock),List.of());

        // debe registrarse la apertura fehaciente de la heladera
        RepoApertura repoApretura = RepoApertura.getInstancia();

        Assertions.assertEquals(2,repoApretura.obtenerAperturasFehacientes().size());

        Assertions.assertEquals(heladeraMockOrigen,repoApretura.obtenerAperturasFehacientes().get(0).getHeladera());
        Assertions.assertEquals(tarjetaColaboradorMock,repoApretura.obtenerAperturasFehacientes().get(0).getTarjeta());

        Assertions.assertEquals(heladeraMockDestino,repoApretura.obtenerAperturasFehacientes().get(1).getHeladera());
        Assertions.assertEquals(tarjetaColaboradorMock,repoApretura.obtenerAperturasFehacientes().get(1).getTarjeta());
    }

    @Test
    public void testNoPuedeAbrirHeladerasSinPermiso(){
        DistribucionDeViandas distribucionDeViandasMock = this.crearDristribucionDeViandas();


        Assertions.assertThrows(SolicitudInexistente.class, distribucionDeViandasMock::ejecutarContribucion);

        // No debe registrarse la apertura fehaciente de la heladera
        RepoApertura repoApreturaFehaciente = RepoApertura.getInstancia();
        Assertions.assertEquals(0,repoApreturaFehaciente.obtenerAperturasFehacientes().size());
    }
}
