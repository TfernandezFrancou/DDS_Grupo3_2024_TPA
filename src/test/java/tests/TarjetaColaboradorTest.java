package tests;

import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.excepciones.LimiteDeTiempoSuperado;
import org.example.personas.roles.Colaborador;
import org.example.repositorios.RepoApertura;
import org.example.tarjetas.Apertura;
import org.example.tarjetas.TarjetaColaborador;
import org.example.tarjetas.TipoDeApertura;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

public class TarjetaColaboradorTest {
    private final TarjetaColaborador tarjetaColaborador = new TarjetaColaborador();

    @Mock
    private Colaborador duenioMock;
    @Mock
    private Heladera heladeraMock;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        RepoApertura.getInstancia().clean();
    }

    @Test
    public void usarTarjeta() throws LimiteDeTiempoSuperado{
        RepoApertura repoApertura = RepoApertura.getInstancia();
        repoApertura.agregarApertura(new Apertura(tarjetaColaborador,heladeraMock, LocalDateTime.now(), TipoDeApertura.SOLICITUD_APERTURA));
        tarjetaColaborador.usar(duenioMock, heladeraMock);

        Assertions.assertEquals(1,tarjetaColaborador.getUsos().size());
    }

    @Test
    public void testNoSePuedeExcederElLimiteDeTiempo() throws LimiteDeTiempoSuperado{
        RepoApertura repoApertura = RepoApertura.getInstancia();
        repoApertura.agregarApertura(new Apertura(tarjetaColaborador, heladeraMock, LocalDateTime.now().minusHours(4), TipoDeApertura.SOLICITUD_APERTURA));

        Assertions.assertThrows(LimiteDeTiempoSuperado.class, ()->tarjetaColaborador.usar(duenioMock,heladeraMock));
    }
}
