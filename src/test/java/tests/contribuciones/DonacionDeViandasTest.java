package tests.contribuciones;

import org.example.colaboraciones.TipoDePersona;
import org.example.colaboraciones.contribuciones.DonacionDeViandas;
import org.example.colaboraciones.contribuciones.heladeras.EstadoHeladera;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.colaboraciones.contribuciones.viandas.Vianda;
import org.example.excepciones.SolicitudInexistente;
import org.example.personas.PersonaHumana;
import org.example.personas.roles.Colaborador;
import org.example.repositorios.*;
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
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

public class DonacionDeViandasTest {


    private Vianda vianda1;


    private Vianda vianda2;


    private Colaborador colaboradorMock;


    private PersonaHumana personaMock;

    private Heladera heladeraMock;

    private Heladera heladeraMockSpy;

    private TarjetaColaborador tarjetaColaboradorMock;

    @BeforeEach
    public void setUp(){
        RepoApertura.getInstancia().clean();
        RepoPersona.getInstancia().clean();
        RepoTarjetas.getInstancia().clean();
        RepoHeladeras.getInstancia().clean();
        RepoContribucion.getInstancia().clean();
        tarjetaColaboradorMock = new TarjetaColaborador();
        tarjetaColaboradorMock.setLimiteDeTiempoDeUsoEnHoras(24);

        RepoTarjetas.getInstancia().agregar(tarjetaColaboradorMock);

        colaboradorMock = new Colaborador();
        colaboradorMock.setPuntuaje(100);
        colaboradorMock.setTarjetaColaborador(tarjetaColaboradorMock);

        personaMock = new PersonaHumana();
        personaMock.setNombre("Franco");
        personaMock.setApellido("Callero");
        personaMock.setRol(colaboradorMock);
        RepoPersona.getInstancia().agregar(personaMock);

        heladeraMock = new Heladera();
        heladeraMock.autorizarColaborador(personaMock);
        heladeraMock.setEstadoHeladeraActual(new EstadoHeladera(true));
        RepoHeladeras.getInstancia().agregar(heladeraMock);

        colaboradorMock.setTarjetaColaborador(tarjetaColaboradorMock);

        vianda1 = new Vianda();
        vianda1.setPeso(100);
        vianda2 = new Vianda();
        vianda2.setPeso(250);
    }

    private DonacionDeViandas crearDonacionDeViandas(){
        DonacionDeViandas donacionDeViandasMock = new DonacionDeViandas();
        donacionDeViandasMock.agregarVianda(vianda1);
        donacionDeViandasMock.agregarVianda(vianda2);
        donacionDeViandasMock.setCantidadDeViandas(2);
        donacionDeViandasMock.setFecha(LocalDate.now());
        donacionDeViandasMock.setColaborador(colaboradorMock);

        heladeraMockSpy = Mockito.spy(heladeraMock);
        donacionDeViandasMock.setHeladera(heladeraMockSpy);

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
        Mockito.verify(heladeraMockSpy, Mockito.times(1)).notificarCambioViandas(List.of(vianda1, vianda2),List.of());


        // debe registrarse la apertura fehaciente de la heladera
        Assertions.assertEquals(1,repo.obtenerAperturasFehacientes().size());
        Assertions.assertEquals(heladeraMock.getIdHeladera(),repo.obtenerAperturasFehacientes().get(0).getHeladera().getIdHeladera());
        Assertions.assertEquals(tarjetaColaboradorMock.getIdTarjeta(),repo.obtenerAperturasFehacientes().get(0).getTarjeta().getIdTarjeta());
    }

    @Test
    public void testNoPuedeAbrirHeladeraSinPermiso() throws MessagingException {
        DonacionDeViandas donacionDeViandasMock = this.crearDonacionDeViandas();


        Assertions.assertThrows(SolicitudInexistente.class, donacionDeViandasMock::ejecutarContribucion);

        //no se debe notificar a la heladera ya que no hay cambios
        Mockito.verify(heladeraMockSpy, Mockito.times(0)).notificarCambioViandas(List.of(vianda1, vianda2),List.of());


        // No debe registrarse la apertura fehaciente de la heladera
        RepoApertura repoApreturaFehaciente = RepoApertura.getInstancia();
        Assertions.assertEquals(0,repoApreturaFehaciente.obtenerAperturasFehacientes().size());
    }

}
