package tests;

import org.example.colaboraciones.Contribucion;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.colaboraciones.contribuciones.ofertas.Oferta;
import org.example.excepciones.NoRegistroDireccionException;
import org.example.excepciones.PuntosInsuficienteParaCanjearOferta;
import org.example.personas.Persona;
import org.example.personas.PersonaHumana;
import org.example.personas.roles.Colaborador;
import org.example.repositorios.RepoContribucion;
import org.example.repositorios.RepoHeladeras;
import org.example.repositorios.RepoPersona;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ColaboradorTest {


    @InjectMocks
    private Colaborador rolColaboradorMock ;

    @Mock
    private Contribucion contribucionMock;

    @Mock
    private Oferta ofertaMock;

    @Mock
    private Heladera heladeraMock;

    @Mock
    private PersonaHumana persona;

    @Mock
    private RepoContribucion repoContribucionMock;
    private MockedStatic<RepoContribucion> repoContribucionStatic;

    @Mock
    private RepoPersona repoPersonaMock;
    private MockedStatic<RepoPersona> repoPersonaStatic;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        repoContribucionStatic = mockStatic(RepoContribucion.class);
        repoContribucionStatic.when(RepoContribucion::getInstancia).thenReturn(repoContribucionMock);
        repoPersonaStatic = mockStatic(RepoPersona.class);
        repoPersonaStatic.when(RepoPersona::getInstancia).thenReturn(repoPersonaMock);
        when(persona.getIdPersona()).thenReturn(1);
        when(rolColaboradorMock.getPersona()).thenReturn(persona);
    }

    @AfterEach
    public void tearDown() {
        repoContribucionStatic.close();
        repoPersonaStatic.close();
    }

    @Test
    public void testCalcularPuntaje(){
        List<Contribucion> contribuciones = List.of(contribucionMock, contribucionMock, contribucionMock, contribucionMock);
        when(repoContribucionMock.obtenerContribucionesPorPersona(persona.getIdPersona())).thenReturn(contribuciones);
        when(repoPersonaMock.actualizarPersona(any())).thenReturn(persona);
        when(contribucionMock.obtenerPuntaje()).thenReturn(25F);
        rolColaboradorMock.setFormasContribucion(contribuciones);
        rolColaboradorMock.calcularPuntuaje();
        Assertions.assertEquals(rolColaboradorMock.getPuntuaje(),100F);
    }

    @Test
    public void testCanjearOferta() throws PuntosInsuficienteParaCanjearOferta {
        rolColaboradorMock.setPuntuaje(100F);
        when(ofertaMock.getPuntosNecesarios()).thenReturn( 10);

        rolColaboradorMock.canjearOferta(ofertaMock);

        Assertions.assertEquals(90F, rolColaboradorMock.getPuntuaje());
    }

    @Test
    public void testNoPuedeCanjearOfertaSiNoTieneLosPuntos() {
        rolColaboradorMock.setPuntuaje(100F);
        when(ofertaMock.getPuntosNecesarios()).thenReturn( 200);



        Assertions.assertThrows(PuntosInsuficienteParaCanjearOferta.class, ()->{
            rolColaboradorMock.canjearOferta(ofertaMock);
        } );

        Assertions.assertEquals(100F, rolColaboradorMock.getPuntuaje());
    }

    @Test
    public void testNoPuedeSolicitarAperturaAHeladeraSiNoTieneDireccionRegistrada(){
        rolColaboradorMock.setTarjetaColaborador(null);

        //registro una persona humana con el rol colaborador para verificar si tiene dirección
        PersonaHumana personaHumana = new PersonaHumana();
        personaHumana.setDireccion(null);
        personaHumana.setRol(rolColaboradorMock);
        RepoPersona.getInstancia().agregar(personaHumana);

        Assertions.assertThrows(NoRegistroDireccionException.class, ()->rolColaboradorMock.emitirAvisoHeladera(heladeraMock));
    }

}
