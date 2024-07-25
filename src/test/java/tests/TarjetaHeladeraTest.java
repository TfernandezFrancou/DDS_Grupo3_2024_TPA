package tests;

import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.personas.Persona;
import org.example.personas.roles.Rol;
import org.example.tarjetas.TarjetaHeladera;
import org.example.excepciones.LimiteDeUsosDiariosSuperados;
import org.example.personas.roles.PersonaEnSituacionVulnerable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.mockito.Mockito.when;

public class TarjetaHeladeraTest {

    private final TarjetaHeladera tarjetaHeladera = new TarjetaHeladera();

    @Mock
    private PersonaEnSituacionVulnerable duenioMock;

    @Mock
    private  Heladera heladeraMock;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        // when(duenioMock.getRol()).thenReturn(rolMock);
    }

    @Test
    public void testUsarTarjeta() throws LimiteDeUsosDiariosSuperados {
        tarjetaHeladera.usar(duenioMock, heladeraMock);

        Assertions.assertEquals(1, tarjetaHeladera.getUsos().size());
        Assertions.assertEquals(1, tarjetaHeladera.getCantidadDeUsosEnElDia());
    }

    @Test
    public void testCalcularLimiteTarjeta(){
        tarjetaHeladera.calcularLimiteTarjeta(10);

        Assertions.assertEquals(24, tarjetaHeladera.getLimiteDeUsuarios());
    }

    @Test
    public void testNoSePuedeSuperarElLimiteDeUsosEnElDia() throws LimiteDeUsosDiariosSuperados {
        tarjetaHeladera.calcularLimiteTarjeta(1);
        Assertions.assertEquals(6, tarjetaHeladera.getLimiteDeUsuarios());

        when(duenioMock.getCantMenores()).thenReturn(1);

        tarjetaHeladera.usar(duenioMock, heladeraMock);
        tarjetaHeladera.usar(duenioMock, heladeraMock);
        tarjetaHeladera.usar(duenioMock, heladeraMock);
        tarjetaHeladera.usar(duenioMock, heladeraMock);
        tarjetaHeladera.usar(duenioMock, heladeraMock);
        tarjetaHeladera.usar(duenioMock, heladeraMock);

        Assertions.assertEquals(6, tarjetaHeladera.getUsos().size());
        Assertions.assertEquals(6, tarjetaHeladera.getCantidadDeUsosEnElDia());

        Assertions.assertThrows(LimiteDeUsosDiariosSuperados.class, ()->{
            tarjetaHeladera.usar(duenioMock, heladeraMock);
        });
    }

    @Test
    public void testSiCambiaDeDiaSeReseteaLaCantidadDeUsosDiarios() throws LimiteDeUsosDiariosSuperados {
        tarjetaHeladera.calcularLimiteTarjeta(1);
        Assertions.assertEquals(6, tarjetaHeladera.getLimiteDeUsuarios());

        when(duenioMock.getCantMenores()).thenReturn(1);

        // Uso la tarjeta 6 veces
        tarjetaHeladera.usar(duenioMock, heladeraMock);
        tarjetaHeladera.usar(duenioMock, heladeraMock);
        tarjetaHeladera.usar(duenioMock, heladeraMock);
        tarjetaHeladera.usar(duenioMock, heladeraMock);
        tarjetaHeladera.usar(duenioMock, heladeraMock);
        tarjetaHeladera.usar(duenioMock, heladeraMock);

        Assertions.assertEquals(6, tarjetaHeladera.getUsos().size());
        Assertions.assertEquals(6, tarjetaHeladera.getCantidadDeUsosEnElDia());

        //cambio a una fecha vieja
        tarjetaHeladera.setDiaActual(LocalDate.of(2024,6,10)); // fecha vieja

        //como cambio de dia, debe resetearse el contador de usos diario
        tarjetaHeladera.usar(duenioMock, heladeraMock);
        Assertions.assertEquals(7, tarjetaHeladera.getUsos().size());
        Assertions.assertEquals(1, tarjetaHeladera.getCantidadDeUsosEnElDia());
    }
}
