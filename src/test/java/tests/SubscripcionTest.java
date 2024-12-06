package tests;

import org.example.broker.Broker;
import org.example.colaboraciones.Contribucion;
import org.example.colaboraciones.Ubicacion;
import org.example.colaboraciones.contribuciones.DonacionDeViandas;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.colaboraciones.contribuciones.heladeras.SensorDeTemperatura;
import org.example.colaboraciones.contribuciones.viandas.Vianda;
import org.example.excepciones.EmailNoRegistradoException;
import org.example.personas.PersonaHumana;
import org.example.personas.contacto.CorreoElectronico;
import org.example.personas.contacto.Mensaje;
import org.example.personas.roles.Colaborador;
import org.example.repositorios.*;
import org.example.subscripciones_heladeras.*;
import org.example.tarjetas.Apertura;
import org.example.tarjetas.Tarjeta;
import org.example.tarjetas.TarjetaColaborador;
import org.example.tarjetas.TipoDeApertura;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.mail.MessagingException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SubscripcionTest {

    private Heladera heladera;
    private RepoUbicacion repoUbicacion;
    private RepoApertura repoApertura;

    private RepoHeladeras repoHeladeras;
    @Mock
    private CorreoElectronico correoElectronicoMock;
    private CorreoElectronico correoElectronico;

    @BeforeEach
    public void setUp(){
        this.repoUbicacion = RepoUbicacion.getInstancia();
        this.repoUbicacion.clean();
        this.repoApertura = RepoApertura.getInstancia();
        this.repoApertura.clean();
        heladera = new Heladera();
        MockitoAnnotations.openMocks(this);
        Ubicacion ubicacion = new Ubicacion(0,  0);
        repoUbicacion.agregar(ubicacion);
        heladera.setUbicacion(ubicacion);
        heladera.setNombre("Heladera Medrano");
        heladera.setCapacidadEnViandas(5);
        heladera.actualizarEstadoHeladera(true);
        RepoHeladeras.getInstancia().clean();
        RepoHeladeras.getInstancia().agregar(heladera);
        correoElectronico = new CorreoElectronico("si@gmail.com");
        System.setProperty("env","test");
        repoHeladeras = RepoHeladeras.getInstancia();
        repoHeladeras.clean();
    }

    @Test
    void subscripcionAIncidentes() throws MessagingException, EmailNoRegistradoException {
        SensorDeTemperatura sensorDeTemperatura = new SensorDeTemperatura();
        sensorDeTemperatura.setHeladera(heladera);

        PersonaHumana personaMock = new PersonaHumana();
        personaMock.addMedioDeContacto(correoElectronico);
        RepoPersona.getInstancia().agregar(personaMock);
        SubscripcionDesperfecto sub = new SubscripcionDesperfecto(heladera,personaMock, correoElectronicoMock);


        try(MockedStatic<RepoHeladeras> mockedStaticRepoHeladeras = Mockito.mockStatic(RepoHeladeras.class)){
            repoHeladeras = Mockito.mock(RepoHeladeras.class);

            when(RepoHeladeras.getInstancia()).thenReturn(repoHeladeras);
            when(repoHeladeras.obtenerSubscripcionesDesperfecto(any(Integer.class)))
                    .thenReturn(List.of(sub));
            doNothing().when(repoHeladeras)
                    .agregarSubscripcion(any(SubscripcionHeladera.class));

            heladera.getPublisherDesperfecto().suscribir(sub);

            sensorDeTemperatura.emitirAlerta("hola");

            Mockito.verify(correoElectronicoMock, times(1))
                    .notificar(any(Mensaje.class));
        }

    }

    @Test
    void subscripcionAViandasFaltantes() throws Exception {
        Broker broker = new Broker();

        PersonaHumana persona = new PersonaHumana();
        persona.addMedioDeContacto(correoElectronico);
        RepoPersona.getInstancia().agregar(persona);
        SubscripcionViandasFaltantes sub = new SubscripcionViandasFaltantes(heladera,persona, correoElectronicoMock, 3);

        PersonaHumana persona2 = new PersonaHumana();
        Colaborador colaborador = new Colaborador();
        persona2.setRol(colaborador);
        colaborador.setTarjetaColaborador(new TarjetaColaborador());

        try(MockedStatic<RepoHeladeras> mockedStaticRepoHeladeras = Mockito.mockStatic(RepoHeladeras.class)){
            try(MockedStatic<RepoApertura> mockedStaticRepoApertura = Mockito.mockStatic(RepoApertura.class)){
                try(MockedStatic<RepoContribucion> mockedStaticRepoContribucion = Mockito.mockStatic(RepoContribucion.class)){
                    //mockeo repos para probar solo la funcionalidad
                    RepoContribucion repoContribucion = Mockito.mock(RepoContribucion.class);
                    when(RepoContribucion.getInstancia()).thenReturn(repoContribucion);
                    doNothing().when(repoContribucion).agregarContribucion(any(Contribucion.class));

                    RepoApertura repoApertura = Mockito.mock(RepoApertura.class);
                    when(RepoApertura.getInstancia()).thenReturn(repoApertura);
                    doNothing().when(repoApertura).agregarApertura(any(Apertura.class));
                    when(repoApertura
                            .existeSolicitudDeAperturaDeTarjetaParaHeladera(
                                    any(TarjetaColaborador.class),
                                    any(Heladera.class)
                            )).thenReturn(true);


                    repoHeladeras = Mockito.mock(RepoHeladeras.class);

                    when(RepoHeladeras.getInstancia()).thenReturn(repoHeladeras);
                    when(repoHeladeras.obtenerSubscripcionesViandasFaltantes(any(Integer.class)))
                            .thenReturn(List.of(sub));
                    doNothing().when(repoHeladeras)
                            .agregarSubscripcion(any(SubscripcionHeladera.class));

                    heladera.getPublisherViandasFaltantes().suscribir(sub);

                    RepoTarjetas.getInstancia().agregar(colaborador.getTarjetaColaborador());
                    RepoPersona.getInstancia().agregar(persona2);

                    Apertura solicitudApertura = new Apertura(colaborador.getTarjetaColaborador(),heladera, LocalDateTime.now(), TipoDeApertura.SOLICITUD_APERTURA);

                    when(repoApertura.buscarSolicitudDeApertura(any(Heladera.class),any(Tarjeta.class)))
                            .thenReturn(solicitudApertura);

                    //ejecuto para probar
                    broker.gestionarSolicitudApertura(solicitudApertura, persona2);

                    new DonacionDeViandas(heladera, colaborador, List.of(new Vianda()), LocalDate.now())
                            .ejecutarContribucion();

                    Mockito.verify(correoElectronicoMock,
                            times(0)).notificar(any(Mensaje.class));

                    //ejecuto para probar
                    broker.gestionarSolicitudApertura(
                            new Apertura( colaborador.getTarjetaColaborador(), heladera, LocalDateTime.now(), TipoDeApertura.SOLICITUD_APERTURA)
                            , persona2);

                    new DonacionDeViandas(heladera, colaborador, List.of(new Vianda()),  LocalDate.now())
                            .ejecutarContribucion();

                    Mockito.verify(correoElectronicoMock, times(1))
                            .notificar(any(Mensaje.class));
                }
            }
        }

    }
}
