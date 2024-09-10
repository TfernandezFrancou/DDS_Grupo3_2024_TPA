package tests.contribuciones;

import org.example.colaboraciones.TipoDePersona;
import org.example.colaboraciones.contribuciones.DonacionDeViandas;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.colaboraciones.contribuciones.viandas.Vianda;
import org.example.excepciones.SolicitudInexistente;
import org.example.personas.PersonaHumana;
import org.example.personas.roles.Colaborador;
import org.example.repositorios.RepoPersona;
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

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
        RepositorioSolicitudesApertura.getInstancia().clean();
        RepositorioAperturasHeladera.getInstancia().clean();
    }

    private DonacionDeViandas crearDonacionDeViandas(){
        DonacionDeViandas donacionDeViandasMock = new DonacionDeViandas();
        donacionDeViandasMock.agregarVianda(vianda1);
        donacionDeViandasMock.agregarVianda(vianda2);
        donacionDeViandasMock.setCantidadDeViandas(2);
        donacionDeViandasMock.setFecha(LocalDate.now());
        donacionDeViandasMock.setColaborador(colaboradorMock);
        donacionDeViandasMock.setTiposDePersona(TipoDePersona.HUMANA);
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

        RepositorioSolicitudesApertura repo = RepositorioSolicitudesApertura.getInstancia();
        repo.agregarSolicitudDeApertura(new SolicitudDeApertura(heladeraMock, LocalDateTime.now(),tarjetaColaboradorMock));

        // no debe tirar error al ejecutar la donacion
        Assertions.assertDoesNotThrow(donacionDeViandasMock::ejecutarContribucion);

        //Verifico si se notifica a la heladera del cambio de viandas
        Mockito.verify(heladeraMock, Mockito.times(1)).notificarCambioViandas(2,0);

        // debe registrarse la apertura fehaciente de la heladera
        RepositorioAperturasHeladera repoApreturaFehaciente = RepositorioAperturasHeladera.getInstancia();

        Assertions.assertEquals(1,repoApreturaFehaciente.getAperturas().size());
        Assertions.assertEquals(heladeraMock,repoApreturaFehaciente.getAperturas().get(0).getHeladera());
        Assertions.assertEquals(tarjetaColaboradorMock,repoApreturaFehaciente.getAperturas().get(0).getTarjeta());
    }

    @Test
    public void testNoPuedeAbrirHeladeraSinPermiso() throws MessagingException {
        DonacionDeViandas donacionDeViandasMock = this.crearDonacionDeViandas();


        Assertions.assertThrows(SolicitudInexistente.class, donacionDeViandasMock::ejecutarContribucion);

        //no se debe notificar a la heladera ya que no hay cambios
        Mockito.verify(heladeraMock, Mockito.times(0)).notificarCambioViandas(2,0);


        // No debe registrarse la apertura fehaciente de la heladera
        RepositorioAperturasHeladera repoApreturaFehaciente = RepositorioAperturasHeladera.getInstancia();
        Assertions.assertEquals(0,repoApreturaFehaciente.getAperturas().size());
    }

}
