package tests;

import org.example.colaboraciones.Ubicacion;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.colaboraciones.contribuciones.heladeras.SensorDeTemperatura;
import org.example.incidentes.Incidente;
import org.example.personas.PersonaHumana;
import org.example.personas.contacto.CorreoElectronico;
import org.example.personas.contacto.Mensaje;
import org.example.personas.roles.Colaborador;
import org.example.personas.roles.Tecnico;
import org.example.recomendacion.Zona;
import org.example.repositorios.RepoIncidente;
import org.example.repositorios.RepoPersona;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.mail.MessagingException;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

public class IncidenteTest {

    @Mock
    private Heladera heladeraMock;

    @Mock
    private CorreoElectronico correoElectronicoMock;

    @Mock
    private Ubicacion ubicacionMock;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        RepoIncidente.getInstancia().clean();
        RepoPersona.getInstancia().clean();
    }

    //es lo mismo si fuera una falla tecnica ya que llaman al mismo método
    @Test
    public void testSeReportaUnaAlerta() throws MessagingException {
        SensorDeTemperatura sensorDeTemperatura = new SensorDeTemperatura();
        sensorDeTemperatura.setHeladera(heladeraMock);

        //configuro Mocks para que el repo persona me encuentre el tecnico mockeado
        RepoPersona repoPersona = RepoPersona.getInstancia();
        PersonaHumana personaMock = new PersonaHumana();
        personaMock.addMedioDeContacto(correoElectronicoMock);

        Tecnico rolTecnico = new Tecnico();
        Zona zonaMock = new Zona();
        zonaMock.setUbicacion(ubicacionMock);
        zonaMock.setRadio(0);
        rolTecnico.agregarAreaDeCovertura(zonaMock);
        personaMock.setRol(rolTecnico);
        repoPersona.agregar(personaMock);

        when(ubicacionMock.calcularDistanciaA(ubicacionMock)).thenReturn(0D);

        when(heladeraMock.getUbicacion()).thenReturn(ubicacionMock);
        when(heladeraMock.getNombre()).thenReturn("Heladera Medrano");
        when(heladeraMock.getDireccion()).thenReturn("Medrano 321");

        //ejecuto
        sensorDeTemperatura.emitirAlerta("Tempratura");

        //se desactiva la heladera
        Mockito.verify(heladeraMock, Mockito.times(1)).desactivarHeladera();

        //se avisa al tecnico mas cercano correspondiente
        Mockito.verify(correoElectronicoMock, times(1)).notificar(any(Mensaje.class));

        //se guarda en el repoIncidentes

        List<Incidente> alertasRegistradas = RepoIncidente.getInstancia().obtenerTodasLasAlertas();

        Assertions.assertEquals(1, alertasRegistradas.size());
    }

    @Test
    public void testUnColaboradorPuedeReportarFallaTecnica() throws MessagingException {
        Colaborador rolColaborador = new Colaborador();

        //configuro Mocks para que el repo persona me encuentre el tecnico mockeado
        RepoPersona repoPersona = RepoPersona.getInstancia();
        PersonaHumana tecnicoPersonaMock = new PersonaHumana();
        PersonaHumana colaboradorPersona = new PersonaHumana();
        tecnicoPersonaMock.addMedioDeContacto(correoElectronicoMock);

        Tecnico rolTecnico = new Tecnico();
        Zona zonaMock = new Zona();
        zonaMock.setUbicacion(ubicacionMock);
        zonaMock.setRadio(0);
        rolTecnico.agregarAreaDeCovertura(zonaMock);
        tecnicoPersonaMock.setRol(rolTecnico);
        colaboradorPersona.setRol(rolColaborador);

        repoPersona.agregar(tecnicoPersonaMock);
        repoPersona.agregar(colaboradorPersona);

        when(ubicacionMock.calcularDistanciaA(ubicacionMock)).thenReturn(0D);

        when(heladeraMock.getUbicacion()).thenReturn(ubicacionMock);
        when(heladeraMock.getNombre()).thenReturn("Heladera Medrano");
        when(heladeraMock.getDireccion()).thenReturn("Medrano 321");

        //reporto la falla técnica
        rolColaborador.reportarFallaTecnica(
                "no enfria",
                "C:/users/marcopolo/heladera_pinchada.png",
                heladeraMock
        );

        //se desactiva la heladera
        Mockito.verify(heladeraMock, Mockito.times(1)).desactivarHeladera();

        //se avisa al tecnico mas cercano correspondiente
        Mockito.verify(correoElectronicoMock, times(1)).notificar(any(Mensaje.class));

        //se guarda en el repoIncidentes

        List<Incidente> fallasTecnicas = RepoIncidente.getInstancia().obtenerTodasLasFallasTecnicas();

        Assertions.assertEquals(1, fallasTecnicas.size());
    }
}
