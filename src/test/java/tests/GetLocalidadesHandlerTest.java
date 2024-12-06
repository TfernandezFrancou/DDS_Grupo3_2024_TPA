package tests;

import io.javalin.http.Context;
import org.example.presentacion.GetLocalidadesHandler;
import org.example.presentacion.dtos.LocalidadDTO;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.colaboraciones.contribuciones.heladeras.Uso;
import org.example.personas.PersonaHumana;
import org.example.colaboraciones.contribuciones.heladeras.Direccion;
import org.example.personas.roles.PersonaEnSituacionVulnerable;
import org.example.repositorios.RepoHeladeras;
import org.example.repositorios.RepoPersona;
import org.example.repositorios.RepoTarjetas;
import org.example.tarjetas.TarjetaHeladera;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import static org.mockito.Mockito.*;

public class GetLocalidadesHandlerTest {

    private RepoPersona repoPersona;

    @Mock
    private Context contextMock;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        this.repoPersona  = RepoPersona.getInstancia();
        repoPersona.clean();
        RepoTarjetas.getInstancia().clean();
        RepoHeladeras.getInstancia().clean();
    }

    @Test
    public void testSolicitarLocalidades() throws Exception{
        PersonaHumana persona1 = new PersonaHumana();
        persona1.setNombre("Carlitos");
        persona1.setApellido("1.0");

        PersonaEnSituacionVulnerable rolPersonaEnSituacionVulnerable = new PersonaEnSituacionVulnerable();
        rolPersonaEnSituacionVulnerable.setFechaNac(LocalDate.now());

        TarjetaHeladera tarjetaHeladera = new TarjetaHeladera();

        Heladera heladeraMock = new Heladera();
        heladeraMock.setDireccion(new Direccion("Medrano", "123","Palermo"));
        Heladera heladeraMock2 = new Heladera();
        heladeraMock2.setDireccion(new Direccion("Medrano", "123","San Telmo"));

        RepoHeladeras.getInstancia().agregarTodas(List.of(heladeraMock, heladeraMock2));

        Uso uso1 = new Uso(LocalDateTime.now(), heladeraMock);
        Uso uso2 = new Uso(LocalDateTime.now().minusDays(1), heladeraMock2);
        Uso uso3 = new Uso(LocalDateTime.now(), heladeraMock);
        tarjetaHeladera.setUsos(List.of(uso1, uso2, uso3));
        RepoTarjetas.getInstancia().agregar(tarjetaHeladera);
        rolPersonaEnSituacionVulnerable.setTarjetaHeladera(tarjetaHeladera);

        persona1.setRol(rolPersonaEnSituacionVulnerable);

        repoPersona.agregar(persona1);

        GetLocalidadesHandler handlerLocalidades = new GetLocalidadesHandler();

        ArgumentCaptor<List<LocalidadDTO>> argumentCaptor = ArgumentCaptor.forClass(List.class);

        handlerLocalidades.handle(contextMock);

        verify(contextMock).json(argumentCaptor.capture());

        List<LocalidadDTO> respuestaCapturada = argumentCaptor.getValue();

        Assertions.assertEquals(2, respuestaCapturada.size());
        Assertions.assertEquals("Palermo", respuestaCapturada.get(0).getNombreLocalidad());
        Assertions.assertEquals(1, respuestaCapturada.get(0).getCantidadDePersonas());
        Assertions.assertEquals("Carlitos 1.0", respuestaCapturada.get(0).getNombresYApellidosDePersonas().get(0));

        Assertions.assertEquals("San Telmo", respuestaCapturada.get(1).getNombreLocalidad());
        Assertions.assertEquals(1, respuestaCapturada.get(1).getCantidadDePersonas());
        Assertions.assertEquals("Carlitos 1.0", respuestaCapturada.get(1).getNombresYApellidosDePersonas().get(0));
    }
}
