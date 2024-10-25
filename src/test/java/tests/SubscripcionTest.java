package tests;

import org.example.broker.Broker;
import org.example.colaboraciones.Ubicacion;
import org.example.colaboraciones.contribuciones.DonacionDeViandas;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.colaboraciones.contribuciones.heladeras.SensorDeTemperatura;
import org.example.colaboraciones.contribuciones.viandas.Vianda;
import org.example.personas.PersonaHumana;
import org.example.personas.contacto.CorreoElectronico;
import org.example.personas.contacto.Mensaje;
import org.example.personas.roles.Colaborador;
import org.example.repositorios.*;
import org.example.subscripcionesHeladeras.*;
import org.example.tarjetas.Apertura;
import org.example.tarjetas.TarjetaColaborador;
import org.example.tarjetas.TipoDeApertura;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.mail.MessagingException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

public class SubscripcionTest {

    private Heladera heladera;

    @Mock
    private CorreoElectronico correoElectronicoMock;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        heladera = new Heladera();
        heladera.setUbicacion(new Ubicacion(0,  0));
        heladera.setNombre("Heladera Medrano");
        heladera.setCapacidadEnViandas(5);
        heladera.actualizarEstadoHeladera(true);
        RepoHeladeras.getInstancia().clean();
        RepoHeladeras.getInstancia().agregar(heladera);
    }

    @Test
    void subscripcionAIncidentes() throws MessagingException {
        SensorDeTemperatura sensorDeTemperatura = new SensorDeTemperatura();
        sensorDeTemperatura.setHeladera(heladera);

        PersonaHumana personaMock = new PersonaHumana();
        personaMock.addMedioDeContacto(correoElectronicoMock);

        heladera.getPublisherDesperfecto().suscribir(new SubscripcionDesperfecto(personaMock, correoElectronicoMock));

        sensorDeTemperatura.emitirAlerta("hola");

        Mockito.verify(correoElectronicoMock, times(1)).notificar(any(Mensaje.class));
    }

    @Test
    void subscripcionAViandasFaltantes() throws Exception {
        Broker broker = new Broker();

        PersonaHumana persona = new PersonaHumana();
        persona.addMedioDeContacto(correoElectronicoMock);

        SubscripcionViandasFaltantes sub = new SubscripcionViandasFaltantes(persona, correoElectronicoMock, 3);
        heladera.getPublisherViandasFaltantes().suscribir(sub);

        PersonaHumana persona2 = new PersonaHumana();
        Colaborador colaborador = new Colaborador();
        persona2.setRol(colaborador);
        colaborador.setTarjetaColaborador(new TarjetaColaborador());

        RepoTarjetas.getInstancia().agregar(colaborador.getTarjetaColaborador());
        RepoPersona.getInstancia().agregar(persona2);

        broker.gestionarSolicitudApertura(new Apertura(colaborador.getTarjetaColaborador(),heladera, LocalDateTime.now(), TipoDeApertura.SOLICITUD_APERTURA), persona2);

        new DonacionDeViandas(heladera, colaborador, List.of(new Vianda()), LocalDate.now()).ejecutarContribucion();

        Mockito.verify(correoElectronicoMock, times(0)).notificar(any(Mensaje.class));

        broker.gestionarSolicitudApertura(new Apertura( colaborador.getTarjetaColaborador(), heladera, LocalDateTime.now(), TipoDeApertura.SOLICITUD_APERTURA), persona2);

        new DonacionDeViandas(heladera, colaborador, List.of(new Vianda()),  LocalDate.now()).ejecutarContribucion();

        Mockito.verify(correoElectronicoMock, times(1)).notificar(any(Mensaje.class));
    }
}
