package tests;

import io.javalin.http.Context;
import org.example.Presentacion.GetLocalidadesHandler;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.colaboraciones.contribuciones.heladeras.Uso;
import org.example.personas.PersonaHumana;
import org.example.personas.contacto.Direccion;
import org.example.personas.roles.Colaborador;
import org.example.personas.roles.PersonaEnSituacionVulnerable;
import org.example.repositorios.RepoPersona;
import org.example.tarjetas.TarjetaHeladera;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import static org.mockito.Mockito.when;

public class GetLocalidadesHandlerTest {

    private RepoPersona repoPersona;

    @Mock
    private Context contextMock;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        this.repoPersona  = RepoPersona.getInstancia();
        repoPersona.clean();
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

        Uso uso1 = new Uso(LocalDateTime.now(), heladeraMock);
        Uso uso2 = new Uso(LocalDateTime.now().minusDays(1), heladeraMock);

        tarjetaHeladera.setUsos(List.of(uso1, uso2));
        rolPersonaEnSituacionVulnerable.setTarjetaHeladera(tarjetaHeladera);

        persona1.setRol(rolPersonaEnSituacionVulnerable);

        repoPersona.agregar(persona1);

        GetLocalidadesHandler handlerLocalidades = new GetLocalidadesHandler();

        handlerLocalidades.handle(contextMock);
        when(contextMock.json(List));

    }
}
