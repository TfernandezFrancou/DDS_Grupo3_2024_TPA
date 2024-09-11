package tests;

import org.example.colaboraciones.Ubicacion;
import org.example.colaboraciones.contribuciones.heladeras.*;
import org.example.incidentes.Alerta;
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
    public void testSePuedeReportarUnaAlerta() throws MessagingException {
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
                "C:/users/marcopolo/Desktop/heladera_pinchada.png",
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

    @Test
    public void testSiElSensorDetectaTemperaturaIncorrectaEmiteUnaAlerta() throws MessagingException {
        //creo el tecnico mas cercano
        PersonaHumana personaHumana = new PersonaHumana();
        Tecnico rolTecnico = new Tecnico();
        Zona zona = new Zona();
        zona.setRadio(12);
        Ubicacion ubicacion = new Ubicacion();
        ubicacion.setLongitud(11);
        ubicacion.setLatitud(11);
        zona.setUbicacion(ubicacion);
        rolTecnico.agregarAreaDeCovertura(zona);
        personaHumana.setRol(rolTecnico);
        personaHumana.addMedioDeContacto(correoElectronicoMock);

        RepoPersona.getInstancia().agregar(personaHumana);

        //creo el sensor
        SensorDeTemperatura sensorDeTemperatura = new SensorDeTemperatura();

        Heladera heladera = new Heladera();
        heladera.setUbicacion(ubicacion); //misma ubicacion que el técnico
        EstadoHeladera estadoHeladeraPrevio= new EstadoHeladera(true);
        heladera.setEstadoHeladeraActual(estadoHeladeraPrevio);
        TemperaturaHeladera temperaturaHeladera = new TemperaturaHeladera();
        temperaturaHeladera.setTemperaturaMaxima(-1);
        temperaturaHeladera.setTemperaturaMinima(-12);
        heladera.setTemperaturasDeFuncionamiento(temperaturaHeladera);
        heladera.setNombre("Heladera Medrano UTN");
        heladera.setDireccion("Medrano 1234");


        sensorDeTemperatura.setHeladera(heladera);
        sensorDeTemperatura.setTemperatura(1); //temperatura fuera de rango

        //ejecuto
        sensorDeTemperatura.notificar();

        RepoIncidente repoIncidente = RepoIncidente.getInstancia();

        Assertions.assertEquals(0, repoIncidente.obtenerTodasLasFallasTecnicas().size());

        //debe emitir una alerta
        Assertions.assertEquals(1, repoIncidente.obtenerTodasLasAlertas().size());

        //debe ser una alerta del tipo temperatura
        Assertions.assertEquals("Temperatura", ((Alerta)repoIncidente.obtenerTodasLasAlertas().get(0)).getTipoDeAlerta());


        //se debe desactivar la heladera
        Assertions.assertEquals(false, heladera.getEstadoHeladeraActual().getEstaActiva());

        //debe avisar al técnico mas cernano
        Mockito.verify(correoElectronicoMock, times(1)).notificar(any(Mensaje.class));

    }

    @Test
    public void testSiElSensorDetectaMovimientoEmiteUnaAlerta() throws MessagingException {
        //creo el tecnico mas cercano
        PersonaHumana personaHumana = new PersonaHumana();
        Tecnico rolTecnico = new Tecnico();
        Zona zona = new Zona();
        zona.setRadio(12);
        Ubicacion ubicacion = new Ubicacion();
        ubicacion.setLongitud(11);
        ubicacion.setLatitud(11);
        zona.setUbicacion(ubicacion);
        rolTecnico.agregarAreaDeCovertura(zona);
        personaHumana.setRol(rolTecnico);
        personaHumana.addMedioDeContacto(correoElectronicoMock);

        RepoPersona.getInstancia().agregar(personaHumana);

        //creo el sensor
        SensorDeMovimiento sensorDeMovimiento = new SensorDeMovimiento();

        Heladera heladera = new Heladera();
        heladera.setUbicacion(ubicacion); //misma ubicacion que el técnico
        EstadoHeladera estadoHeladeraPrevio= new EstadoHeladera(true);
        heladera.setEstadoHeladeraActual(estadoHeladeraPrevio);
        heladera.setNombre("Heladera Medrano UTN");
        heladera.setDireccion("Medrano 1234");


        sensorDeMovimiento.setHeladera(heladera);
        sensorDeMovimiento.setEstaActivado(true); //detecta movimiento

        //ejecuto
        sensorDeMovimiento.notificar();

        RepoIncidente repoIncidente = RepoIncidente.getInstancia();

        Assertions.assertEquals(0, repoIncidente.obtenerTodasLasFallasTecnicas().size());

        //debe emitir una alerta
        Assertions.assertEquals(1, repoIncidente.obtenerTodasLasAlertas().size());

        //debe ser una alerta del tipo fraude
        Assertions.assertEquals("Fraude", ((Alerta)repoIncidente.obtenerTodasLasAlertas().get(0)).getTipoDeAlerta());

        //se debe desactivar la heladera
        Assertions.assertEquals(false, heladera.getEstadoHeladeraActual().getEstaActiva());

        //debe avisar al técnico mas cernano
        Mockito.verify(correoElectronicoMock, times(1)).notificar(any(Mensaje.class));

    }

    @Test
    public void testSiNoHayTecnicoCercanoNoSeAvisaANadie() throws MessagingException {
        Ubicacion ubicacionCoverturaTecnico = new Ubicacion(-34.609722F, -58.382592F);// Cerca de Buenos Aires

        Zona zonaCovertura = new Zona();
        zonaCovertura.setUbicacion(ubicacionCoverturaTecnico);
        zonaCovertura.setRadio(2);//radio chico para que la heladera no entre en este radio

        Ubicacion ubicacionHeladera = new Ubicacion(-34.705722F, -58.501592F);// Más lejos de Buenos Aires

        //creo el tecnico mas cercano
        PersonaHumana personaHumana = new PersonaHumana();
        Tecnico rolTecnico = new Tecnico();

        rolTecnico.agregarAreaDeCovertura(zonaCovertura);

        personaHumana.setRol(rolTecnico);
        personaHumana.addMedioDeContacto(correoElectronicoMock);

        RepoPersona.getInstancia().agregar(personaHumana);

        //creo la heladera
        Heladera heladera = new Heladera();
        heladera.setUbicacion(ubicacionHeladera);
        EstadoHeladera estadoHeladeraPrevio= new EstadoHeladera(true);
        heladera.setEstadoHeladeraActual(estadoHeladeraPrevio);

        heladera.setNombre("Heladera Medrano UTN");
        heladera.setDireccion("Medrano 1234");

        SensorDeMovimiento sensorDeMovimiento = new SensorDeMovimiento();
        sensorDeMovimiento.setHeladera(heladera);
        sensorDeMovimiento.setEstaActivado(true);

        //emito la alerta
        sensorDeMovimiento.notificar();

        //verifico que no se le aviso a nadie
        Mockito.verify(correoElectronicoMock, times(0)).notificar(any(Mensaje.class));
    }
}
