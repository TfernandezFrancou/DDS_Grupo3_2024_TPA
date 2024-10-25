package tests.contribuciones;

import org.example.colaboraciones.TipoDePersona;
import org.example.colaboraciones.contribuciones.DonacionDeViandas;
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

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;

public class DonacionDeViandasTest {

    @Mock
    private Vianda vianda1;

    @Mock
    private Vianda vianda2;

    @Mock
    private Colaborador colaboradorMock;

    @Mock
    private PersonaHumana personaMock;

    @Mock
    private Heladera heladeraMock;

    @Mock
    private TarjetaColaborador tarjetaColaboradorMock;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);//crea los mocks
        RepoApertura.getInstancia().clean();
    }

    private DonacionDeViandas crearDonacionDeViandas(){
        DonacionDeViandas donacionDeViandasMock = new DonacionDeViandas();
        donacionDeViandasMock.agregarVianda(vianda1);
        donacionDeViandasMock.agregarVianda(vianda2);
        donacionDeViandasMock.setCantidadDeViandas(2);
        donacionDeViandasMock.setFecha(LocalDate.now());
        donacionDeViandasMock.setColaborador(colaboradorMock);
        donacionDeViandasMock.setHeladera(heladeraMock);

        when(heladeraMock.estaActiva()).thenReturn(true);
        when(heladeraMock.puedeAbrirHeladera(personaMock)).thenReturn(true);

        when(colaboradorMock.getTarjetaColaborador()).thenReturn(tarjetaColaboradorMock);
        when(colaboradorMock.getPersona()).thenReturn(personaMock);

        return donacionDeViandasMock;
    }

    @Test
    public void testPuedeDonarViandas() throws Exception {
        DonacionDeViandas donacionDeViandasMock = this.crearDonacionDeViandas();

        RepoApertura repo = RepoApertura.getInstancia();
        repo.agregarApertura(new Apertura(tarjetaColaboradorMock, heladeraMock, LocalDateTime.now(), TipoDeApertura.SOLICITUD_APERTURA));

        // no debe tirar error al ejecutar la donacion
        Assertions.assertDoesNotThrow(donacionDeViandasMock::ejecutarContribucion);

        //Verifico si se notifica a la heladera del cambio de viandas
        Mockito.verify(heladeraMock, Mockito.times(1)).notificarCambioViandas(List.of(vianda1, vianda2),List.of());

        // debe registrarse la apertura fehaciente de la heladera
        Assertions.assertEquals(1,repo.obtenerAperturasFehacientes().size());
        Assertions.assertEquals(heladeraMock,repo.obtenerAperturasFehacientes().get(0).getHeladera());
        Assertions.assertEquals(tarjetaColaboradorMock,repo.obtenerAperturasFehacientes().get(0).getTarjeta());
    }

    @Test
    public void testNoPuedeAbrirHeladeraSinPermiso() throws MessagingException {
        DonacionDeViandas donacionDeViandasMock = this.crearDonacionDeViandas();


        Assertions.assertThrows(SolicitudInexistente.class, donacionDeViandasMock::ejecutarContribucion);

        //no se debe notificar a la heladera ya que no hay cambios
        Mockito.verify(heladeraMock, Mockito.times(0)).notificarCambioViandas(List.of(new Vianda(), new Vianda()),List.of());


        // No debe registrarse la apertura fehaciente de la heladera
        RepoApertura repoApreturaFehaciente = RepoApertura.getInstancia();
        Assertions.assertEquals(0,repoApreturaFehaciente.obtenerAperturasFehacientes().size());
    }

}
