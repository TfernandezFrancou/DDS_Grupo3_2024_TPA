package tests.contribuciones;

import org.example.colaboraciones.contribuciones.DistribucionDeViandas;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;

public class DistribucionDeViandasTest {


    private Vianda vianda2;

    private Colaborador colaboradorMock;


    private Heladera heladeraMockOrigen;

    private Heladera heladeraMockDestino;

    private Vianda viandaMock;


    private TarjetaColaborador tarjetaColaboradorMock;


    private PersonaHumana personaHumanaMock;


    private Heladera heladeraMockOrigenSpy;
    private Heladera heladeraMockDestinoSpy ;

    @BeforeEach
    public void setUp(){
        RepoApertura.getInstancia().clean();
        RepoContribucion.getInstancia().clean();
        RepoPersona repoPersona = RepoPersona.getInstancia();
        RepoTarjetas repoTarjetas = RepoTarjetas.getInstancia();
        RepoHeladeras repoHeladeras = RepoHeladeras.getInstancia();
        repoPersona.clean();
        repoTarjetas.clean();
        repoHeladeras.clean();
        tarjetaColaboradorMock = new TarjetaColaborador();

        colaboradorMock = new Colaborador();
        colaboradorMock.setTarjetaColaborador(tarjetaColaboradorMock);

        personaHumanaMock = new PersonaHumana();
        personaHumanaMock.setNombre("Franco");
        personaHumanaMock.setApellido("Callero");
        personaHumanaMock.setRol(colaboradorMock);
        repoTarjetas.agregar(tarjetaColaboradorMock);
        repoPersona.agregar(personaHumanaMock);

        heladeraMockOrigen = new Heladera();
        heladeraMockOrigen.setNombre("heladera medrano buffet");
        heladeraMockOrigen.autorizarColaborador(personaHumanaMock);
        heladeraMockOrigen.setEstadoHeladeraActual(new EstadoHeladera(true));

        heladeraMockDestino = new Heladera();
        heladeraMockDestino.setNombre("heladera lugano buffet");
        heladeraMockDestino.autorizarColaborador(personaHumanaMock);
        heladeraMockDestino.setEstadoHeladeraActual(new EstadoHeladera(true));
        repoHeladeras.agregarTodas(List.of(heladeraMockOrigen, heladeraMockDestino));

        heladeraMockOrigenSpy = Mockito.spy(heladeraMockOrigen);
         heladeraMockDestinoSpy = Mockito.spy(heladeraMockDestino);

        viandaMock = new Vianda();
        viandaMock.setPeso(15);
        viandaMock.setCalorias(10);
        viandaMock.setDescripcion("ensalada cesar");

        vianda2 = new Vianda();
        vianda2.setPeso(20);
        vianda2.setCalorias(300);
        vianda2.setDescripcion("Pancho");
    }

    private DistribucionDeViandas crearDristribucionDeViandas(){
        DistribucionDeViandas distribucionDeViandasMock = new DistribucionDeViandas(
                LocalDate.now(),
                List.of(viandaMock, vianda2)
        );
        distribucionDeViandasMock.setColaborador(colaboradorMock);
        distribucionDeViandasMock.setOrigen(heladeraMockOrigenSpy);
        distribucionDeViandasMock.setDestino(heladeraMockDestinoSpy);

        return distribucionDeViandasMock;
    }

    @Test
    public void testPuedeDistribuirViandas() throws Exception {

        DistribucionDeViandas distribucionDeViandasMock = this.crearDristribucionDeViandas();

        RepoApertura repo = RepoApertura.getInstancia();

        repo.agregarApertura(new Apertura(tarjetaColaboradorMock, heladeraMockOrigenSpy, LocalDateTime.now(), TipoDeApertura.SOLICITUD_APERTURA));
        repo.agregarApertura(new Apertura(tarjetaColaboradorMock, heladeraMockDestinoSpy, LocalDateTime.now(),TipoDeApertura.SOLICITUD_APERTURA));

        // no debe tirar error al ejecutar la donacion
        Assertions.assertDoesNotThrow(distribucionDeViandasMock::ejecutarContribucion);

        //verifico que se notifiquen el cambio de viandas a las heladeras correspondientes
        Mockito.verify(heladeraMockOrigenSpy, Mockito.times(1)).notificarCambioViandas(List.of(), List.of(viandaMock, vianda2));
        Mockito.verify(heladeraMockDestinoSpy, Mockito.times(1)).notificarCambioViandas(List.of(viandaMock, vianda2),List.of());

        // debe registrarse la apertura fehaciente de la heladera
        RepoApertura repoApretura = RepoApertura.getInstancia();

        Assertions.assertEquals(2,repoApretura.obtenerAperturasFehacientes().size());

        Assertions.assertEquals(heladeraMockOrigen.getIdHeladera(),repoApretura.obtenerAperturasFehacientes().get(0).getHeladera().getIdHeladera());
        Assertions.assertEquals(tarjetaColaboradorMock.getIdTarjeta(),repoApretura.obtenerAperturasFehacientes().get(0).getTarjeta().getIdTarjeta());

        Assertions.assertEquals(heladeraMockDestino.getIdHeladera(),repoApretura.obtenerAperturasFehacientes().get(1).getHeladera().getIdHeladera());
        Assertions.assertEquals(tarjetaColaboradorMock.getIdTarjeta(),repoApretura.obtenerAperturasFehacientes().get(1).getTarjeta().getIdTarjeta());
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
