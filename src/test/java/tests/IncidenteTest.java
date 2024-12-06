package tests;

import org.example.colaboraciones.Ubicacion;
import org.example.colaboraciones.contribuciones.heladeras.*;
import org.example.excepciones.EmailNoRegistradoException;
import org.example.incidentes.Alerta;
import org.example.incidentes.Incidente;
import org.example.personas.PersonaHumana;
import org.example.personas.contacto.CorreoElectronico;
import org.example.colaboraciones.contribuciones.heladeras.Direccion;
import org.example.personas.roles.Colaborador;
import org.example.personas.roles.Tecnico;
import org.example.recomendacion.Zona;
import org.example.repositorios.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;

class IncidenteTest {

    private Heladera heladeraMock;

    private RepoHeladeras repoHeladeras;

    private RepoSensor repoSensor;

    private RepoUbicacion repoUbicacion;

    private CorreoElectronico correoElectronicoMock;

    private Ubicacion ubicacionMock;

    @BeforeEach
    public void setUp(){
        repoHeladeras = RepoHeladeras.getInstancia();
        repoHeladeras.clean();
        repoSensor = RepoSensor.getInstancia();
        repoSensor.clean();
        repoUbicacion = RepoUbicacion.getInstancia();
        repoUbicacion.clean();


        System.setProperty("env", "test");
        RepoIncidente.getInstancia().clean();
        RepoPersona.getInstancia().clean();
        correoElectronicoMock = new CorreoElectronico();

        ubicacionMock = new Ubicacion();

        heladeraMock = new Heladera();
        heladeraMock.setNombre("Heladera Medrano");

        Direccion direccionMock = new Direccion();
        direccionMock.setNombreCalle("Medrano");
        direccionMock.setAltura("321");

        heladeraMock.setDireccion(direccionMock);
        heladeraMock.setEstadoHeladeraActual(new EstadoHeladera(true));


        RepoHeladeras.getInstancia().agregar(heladeraMock);
    }

    //es lo mismo si fuera una falla tecnica ya que llaman al mismo método
    @Test
    void testSePuedeReportarUnaAlerta() throws MessagingException, EmailNoRegistradoException {
        Heladera heladeraMockSpy = Mockito.spy(heladeraMock);
        Ubicacion ubicacionMockSpy = Mockito.spy(ubicacionMock);

        SensorDeTemperatura sensorDeTemperatura = new SensorDeTemperatura();

        heladeraMockSpy.setUbicacion(ubicacionMockSpy);
        sensorDeTemperatura.setHeladera(heladeraMockSpy);

        when(ubicacionMockSpy.calcularDistanciaA(ubicacionMockSpy)).thenReturn(0D);

        //configuro Mocks para que el repo persona me encuentre el tecnico mockeado
        RepoPersona repoPersona = RepoPersona.getInstancia();
        PersonaHumana personaMock = new PersonaHumana();

        personaMock.addMedioDeContacto(correoElectronicoMock);

        repoUbicacion.agregar(ubicacionMockSpy);
        Tecnico rolTecnico = new Tecnico();
        Zona zonaMock = new Zona();
        zonaMock.setUbicacion(ubicacionMockSpy);
        zonaMock.setRadio(0);
        rolTecnico.agregarAreaDeCovertura(zonaMock);
        personaMock.setRol(rolTecnico);
        repoPersona.agregar(personaMock);

        //ejecuto
        sensorDeTemperatura.emitirAlerta("Tempratura");

        //se desactiva la heladera
        Mockito.verify(heladeraMockSpy, Mockito.times(1)).desactivarHeladera();

        //se guarda en el repoIncidentes

        List<Incidente> alertasRegistradas = RepoIncidente.getInstancia().obtenerTodasLasAlertas();

        Assertions.assertEquals(1, alertasRegistradas.size());
    }

    @Test
    void testUnColaboradorPuedeReportarFallaTecnica() throws MessagingException, EmailNoRegistradoException {
        Heladera heladeraMockSpy = Mockito.spy(heladeraMock);
        Ubicacion ubicacionMockSpy = Mockito.spy(ubicacionMock);

        heladeraMockSpy.setUbicacion(ubicacionMockSpy);
        Colaborador rolColaborador = new Colaborador();

        //configuro Mocks para que el repo persona me encuentre el tecnico mockeado
        RepoPersona repoPersona = RepoPersona.getInstancia();
        PersonaHumana tecnicoPersonaMock = new PersonaHumana();
        PersonaHumana colaboradorPersona = new PersonaHumana();
        tecnicoPersonaMock.addMedioDeContacto(correoElectronicoMock);

        Tecnico rolTecnico = new Tecnico();
        repoUbicacion.agregar(ubicacionMockSpy);
        Zona zonaMock = new Zona();
        zonaMock.setUbicacion(ubicacionMockSpy);
        zonaMock.setRadio(0);
        rolTecnico.agregarAreaDeCovertura(zonaMock);
        tecnicoPersonaMock.setRol(rolTecnico);
        colaboradorPersona.setRol(rolColaborador);

        repoPersona.agregar(tecnicoPersonaMock);
        repoPersona.agregar(colaboradorPersona);

        when(ubicacionMockSpy.calcularDistanciaA(ubicacionMockSpy)).thenReturn(0D);


        //reporto la falla técnica
        rolColaborador.reportarFallaTecnica(
                "no enfria",
                "C:/users/marcopolo/Desktop/heladera_pinchada.png",
                heladeraMockSpy, LocalDateTime.now()
        );

        //se desactiva la heladera
        Mockito.verify(heladeraMockSpy, Mockito.times(1)).desactivarHeladera();

        //se guarda en el repoIncidentes

        List<Incidente> fallasTecnicas = RepoIncidente.getInstancia().obtenerTodasLasFallasTecnicas();

        Assertions.assertEquals(1, fallasTecnicas.size());
    }

    @Test
    void testSiElSensorDetectaTemperaturaIncorrectaEmiteUnaAlerta() throws MessagingException, EmailNoRegistradoException {
        //creo el tecnico mas cercano
        PersonaHumana personaHumana = new PersonaHumana();
        Tecnico rolTecnico = new Tecnico();
        Zona zona = new Zona();
        zona.setRadio(12);
        Ubicacion ubicacion = new Ubicacion();
        ubicacion.setLongitud(11);
        ubicacion.setLatitud(11);
        repoUbicacion.agregar(ubicacion);
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
        TemperaturaHeladera temperaturaHeladera = new TemperaturaHeladera(-12, -1);
        heladera.setTemperaturasDeFuncionamiento(temperaturaHeladera);
        heladera.setNombre("Heladera Medrano UTN");

        Direccion direccionMock = new Direccion();
        direccionMock.setNombreCalle("Medrano");
        direccionMock.setAltura("321");
        heladera.setDireccion(direccionMock);

        repoHeladeras.agregar(heladera);

        sensorDeTemperatura.setHeladera(heladera);
        sensorDeTemperatura.setTemperatura(1); //temperatura fuera de rango

        repoSensor.agregarSensor(sensorDeTemperatura);

        //ejecuto
        sensorDeTemperatura.notificar();

        RepoIncidente repoIncidente = RepoIncidente.getInstancia();

        Assertions.assertEquals(0, repoIncidente.obtenerTodasLasFallasTecnicas().size());

        //debe emitir una alerta
        Assertions.assertEquals(1, repoIncidente.obtenerTodasLasAlertas().size());

        //debe ser una alerta del tipo temperatura
        Assertions.assertEquals("Temperatura", ((Alerta)repoIncidente.obtenerTodasLasAlertas().get(0)).getTipoDeAlerta());

        //se actualiza la temperatura actual de la heladera
        Assertions.assertEquals(1, heladera.getTemperaturaActualHeladera());

        //se debe desactivar la heladera
        Assertions.assertEquals(false, heladera.getEstadoHeladeraActual().getEstaActiva());
    }

    @Test
    void testSiElSensorDetectaMovimientoEmiteUnaAlerta() throws MessagingException, EmailNoRegistradoException {
        //creo el tecnico mas cercano
        PersonaHumana personaHumana = new PersonaHumana();
        Tecnico rolTecnico = new Tecnico();
        Zona zona = new Zona();
        zona.setRadio(12);
        Ubicacion ubicacion = new Ubicacion();
        ubicacion.setLongitud(11);
        ubicacion.setLatitud(11);
        repoUbicacion.agregar(ubicacion);
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


        Direccion direccionMock = new Direccion();
        direccionMock.setNombreCalle("Medrano");
        direccionMock.setAltura("321");
        heladera.setDireccion(direccionMock);

        repoHeladeras.agregar(heladera);

        sensorDeMovimiento.setHeladera(heladera);
        sensorDeMovimiento.setEstaActivado(true); //detecta movimiento

        repoSensor.agregarSensor(sensorDeMovimiento);

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

    }

    @Test
     void testSiNoHayTecnicoCercanoNoSeAvisaANadie() throws MessagingException, EmailNoRegistradoException {
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

        repoUbicacion.agregar(ubicacionCoverturaTecnico);
        repoUbicacion.agregar(ubicacionHeladera);

        RepoPersona.getInstancia().agregar(personaHumana);

        //creo la heladera
        Heladera heladera = new Heladera();
        heladera.setUbicacion(ubicacionHeladera);
        EstadoHeladera estadoHeladeraPrevio= new EstadoHeladera(true);
        heladera.setEstadoHeladeraActual(estadoHeladeraPrevio);

        heladera.setNombre("Heladera Medrano UTN");

        Direccion direccionMock = new Direccion();
        direccionMock.setNombreCalle("Medrano");
        direccionMock.setAltura("321");
        heladera.setDireccion(direccionMock);

        repoHeladeras.agregar(heladera);

        SensorDeMovimiento sensorDeMovimiento = new SensorDeMovimiento();
        sensorDeMovimiento.setHeladera(heladera);
        sensorDeMovimiento.setEstaActivado(true);

        repoSensor.agregarSensor(sensorDeMovimiento);

        //emito la alerta
        sensorDeMovimiento.notificar();
        Assertions.assertFalse(heladera.getEstadoHeladeraActual().getEstaActiva());
    }
}
