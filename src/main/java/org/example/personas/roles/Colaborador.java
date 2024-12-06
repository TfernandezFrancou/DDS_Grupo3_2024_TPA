package org.example.personas.roles;

import lombok.Getter;
import lombok.Setter;
import org.example.broker.Broker;
import org.example.colaboraciones.contribuciones.DonacionDeViandas;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.colaboraciones.contribuciones.viandas.Vianda;
import org.example.config.Configuracion;
import org.example.excepciones.EmailNoRegistradoException;
import org.example.excepciones.NoRegistroDireccionException;
import org.example.incidentes.FallaTecnica;
import org.example.personas.Persona;
import org.example.repositorios.RepoIncidente;
import org.example.repositorios.RepoPersona;
import org.example.repositorios.RepoTarjetas;
import org.example.tarjetas.Apertura;
import org.example.tarjetas.TarjetaColaborador;
import org.example.colaboraciones.contribuciones.ofertas.Oferta;
import org.example.excepciones.PuntosInsuficienteParaCanjearOferta;
import org.example.colaboraciones.Contribucion;
import org.example.repositorios.RepoContribucion;
import org.example.tarjetas.TipoDeApertura;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.mail.MessagingException;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
public class Colaborador extends Rol {

    @OneToMany(mappedBy = "colaborador")
    private List<Contribucion> formasContribucion;

    private float puntuaje;

    @ManyToMany
    @JoinTable(name = "ofertas_canjeadas")
    private List<Oferta> ofertasCanjeadas;

    @OneToOne
    @JoinColumn(name="id_tarjeta_colaborador")
    private TarjetaColaborador tarjetaColaborador;

    @Transient
    private static final Logger log = LoggerFactory.getLogger(Colaborador.class);

    public Colaborador(){
        this.formasContribucion = new ArrayList<>();
        this.ofertasCanjeadas = new ArrayList<>();
        this.puntuaje = 0;
    }

    public void agregarContribucion(Contribucion contribucion) {
        contribucion.setColaborador(this);
        this.formasContribucion.add(contribucion);
        RepoContribucion.getInstancia().agregarContribucion(contribucion);
    }


    public void calcularPuntuaje()
    {
        this.puntuaje=0;//recalculamos el puntaje desde cero


        List<Contribucion> contribuciones = RepoContribucion.getInstancia().obtenerContribucionesPorPersona(this.getPersona().getIdPersona());

        for (Contribucion contribucion: contribuciones) {
            log.info("calculando contribucion {}", contribucion);
            log.info("puntaje: {}", contribucion.obtenerPuntaje());
            this.puntuaje += contribucion.obtenerPuntaje();
        }

        log.info("puntaje final: {}", this.puntuaje);

        int puntosCanjeados = 0;

        for (Oferta oferta: this.ofertasCanjeadas) {
            puntosCanjeados = oferta.getPuntosNecesarios();

        }

        this.puntuaje -= puntosCanjeados;

        log.info("puntaje - ofertas canjeadas: {}", this.puntuaje);

        if (this.puntuaje < 0) this.puntuaje = 0;

        RepoPersona.getInstancia().actualizarColaborador(this);
    }

    public void canjearOferta(Oferta oferta) throws PuntosInsuficienteParaCanjearOferta {
        if(this.puedeCanjearOferta(oferta)){
            this.ofertasCanjeadas.add(oferta);
            this.puntuaje -= oferta.getPuntosNecesarios();
            RepoPersona.getInstancia().actualizarColaborador(this);
        } else {
            throw new PuntosInsuficienteParaCanjearOferta();
        }
    }

    private boolean puedeCanjearOferta(Oferta oferta){
        return this.puntuaje >= oferta.getPuntosNecesarios();
    }


    public boolean noTieneTarjetaAsignada(){
        return tarjetaColaborador == null;
    }
    private void asignarTarjeta(){
        Persona persona = this.getPersona();
        TarjetaColaborador tarjetaNueva = new TarjetaColaborador();
        RepoTarjetas.getInstancia().agregar(tarjetaNueva);

        if(persona.getDireccion() == null){//si no se sabe su direccion no se le puede enviar la tarjeta por correo
            throw new NoRegistroDireccionException(Configuracion.obtenerProperties("mensaje.solicitud-apertura-heladera.direccion-no-registrada"));
        }

        this.setTarjetaColaborador(tarjetaNueva);
    }

    //Se llama a este método si solicita una apertura a una heladera
    public void emitirAvisoHeladera(Heladera heladera){
        if(this.noTieneTarjetaAsignada()){
            this.asignarTarjeta();
        }

        Apertura solicitudDeApertura = new Apertura(this.getTarjetaColaborador(),heladera,LocalDateTime.now(), TipoDeApertura.SOLICITUD_APERTURA);
        Broker broker = new Broker();
        broker.gestionarSolicitudApertura(solicitudDeApertura, this.getPersona());
    }
    //se llama cuando un colaborador reporta una falla
    public void reportarFallaTecnica(String descripcion, String foto, Heladera heladera, LocalDateTime fechaReportada) throws MessagingException, EmailNoRegistradoException {
        FallaTecnica fallaTecnica = new FallaTecnica(this.getPersona(),descripcion,foto,heladera,"Falla Técnica",fechaReportada);

        fallaTecnica.reportarIncidente();
        RepoIncidente.getInstancia().agregarFalla(fallaTecnica);
    }
    public List<Vianda> cantidadDeViandasDistribuidasEnLaSemana(LocalDateTime inicioSemana, LocalDateTime finSemana){

       return this.formasContribucion.stream()
                .filter(contribucion -> contribucion instanceof DonacionDeViandas &&
                        (contribucion.getFecha().isAfter(ChronoLocalDate.from(inicioSemana)) &&
                        contribucion.getFecha().isBefore(ChronoLocalDate.from(finSemana))
                        ) || (contribucion.getFecha().equals(inicioSemana.toLocalDate()) ||
                                    contribucion.getFecha().equals(finSemana.toLocalDate())
                            )
                        )
                .map(contribucion -> {
                    assert contribucion instanceof DonacionDeViandas;
                    return ((DonacionDeViandas) contribucion).getViandas();
                })
               .filter(Objects::nonNull) //filtro las viandas null
               .flatMap(List::stream)//junto las viandas en un stream
               .toList(); // lo paso a list
    }

    public Persona getPersona(){
        return RepoPersona.getInstancia().buscarPersonaAsociadaAlRol(this);
    }
}
