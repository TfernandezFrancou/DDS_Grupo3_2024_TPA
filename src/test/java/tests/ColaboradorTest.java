package tests;

import org.example.colaboraciones.Contribucion;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.colaboraciones.contribuciones.ofertas.Oferta;
import org.example.excepciones.NoRegistroDireccionException;
import org.example.excepciones.PuntosInsuficienteParaCanjearOferta;
import org.example.personas.Persona;
import org.example.personas.PersonaHumana;
import org.example.personas.roles.Colaborador;
import org.example.repositorios.RepoPersona;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.Mockito.when;

public class ColaboradorTest {


    @InjectMocks
    private Colaborador rolColaboradorMock ;

    @Mock
    private Contribucion contribucionMock;

    @Mock
    private Oferta ofertaMock;

    @Mock
    private Heladera heladeraMock;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCalcularPuntaje(){


        rolColaboradorMock.setFormasContribucion(List.of(contribucionMock, contribucionMock,contribucionMock, contribucionMock));
        when(contribucionMock.obtenerPuntaje()).thenReturn(25F);

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
