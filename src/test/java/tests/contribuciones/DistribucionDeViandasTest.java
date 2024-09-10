package tests.contribuciones;

import org.example.colaboraciones.TipoDePersona;
import org.example.colaboraciones.contribuciones.DistribucionDeViandas;
import org.example.colaboraciones.contribuciones.DonacionDeViandas;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.colaboraciones.contribuciones.viandas.Vianda;
import org.example.excepciones.SolicitudInexistente;
import org.example.personas.Persona;
import org.example.personas.PersonaHumana;
import org.example.personas.roles.Colaborador;
import org.example.repositorios.RepositorioAperturasHeladera;
import org.example.repositorios.RepositorioSolicitudesApertura;
import org.example.tarjetas.SolicitudDeApertura;
import org.example.tarjetas.TarjetaColaborador;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
    private TarjetaColaborador tarjetaColaboradorMock;

    @Mock
    private PersonaHumana personaHumanaMock;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);//crea los mocks
        RepositorioSolicitudesApertura.getInstancia().clean();
        RepositorioAperturasHeladera.getInstancia().clean();
    }

    private DistribucionDeViandas crearDristribucionDeViandas(){
        DistribucionDeViandas distribucionDeViandasMock = new DistribucionDeViandas(
                TipoDePersona.HUMANA,
                LocalDate.now(),
                2
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

        RepositorioSolicitudesApertura repo = RepositorioSolicitudesApertura.getInstancia();

        repo.agregarSolicitudDeApertura(new SolicitudDeApertura(heladeraMockOrigen, LocalDateTime.now(),tarjetaColaboradorMock));
        repo.agregarSolicitudDeApertura(new SolicitudDeApertura(heladeraMockDestino, LocalDateTime.now(),tarjetaColaboradorMock));

        // no debe tirar error al ejecutar la donacion
        Assertions.assertDoesNotThrow(distribucionDeViandasMock::ejecutarContribucion);

        //verifico que se notifiquen el cambio de viandas a las heladeras correspondientes
        Mockito.verify(heladeraMockOrigen, Mockito.times(1)).notificarCambioViandas(0,2);
        Mockito.verify(heladeraMockDestino, Mockito.times(1)).notificarCambioViandas(2,0);

        // debe registrarse la apertura fehaciente de la heladera
        RepositorioAperturasHeladera repoApreturaFehaciente = RepositorioAperturasHeladera.getInstancia();

        Assertions.assertEquals(2,repoApreturaFehaciente.getAperturas().size());

        Assertions.assertEquals(heladeraMockOrigen,repoApreturaFehaciente.getAperturas().get(0).getHeladera());
        Assertions.assertEquals(tarjetaColaboradorMock,repoApreturaFehaciente.getAperturas().get(0).getTarjeta());

        Assertions.assertEquals(heladeraMockDestino,repoApreturaFehaciente.getAperturas().get(1).getHeladera());
        Assertions.assertEquals(tarjetaColaboradorMock,repoApreturaFehaciente.getAperturas().get(1).getTarjeta());
    }

    @Test
    public void testNoPuedeAbrirHeladerasSinPermiso(){
        DistribucionDeViandas distribucionDeViandasMock = this.crearDristribucionDeViandas();


        Assertions.assertThrows(SolicitudInexistente.class, distribucionDeViandasMock::ejecutarContribucion);

        // No debe registrarse la apertura fehaciente de la heladera
        RepositorioAperturasHeladera repoApreturaFehaciente = RepositorioAperturasHeladera.getInstancia();
        Assertions.assertEquals(0,repoApreturaFehaciente.getAperturas().size());
    }
}
