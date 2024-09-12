package org.example.personas.roles;

import lombok.Getter;
import lombok.Setter;
import org.example.broker.Broker;
import org.example.colaboraciones.contribuciones.DonacionDeViandas;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.config.Configuracion;
import org.example.excepciones.NoRegistroDireccionException;
import org.example.incidentes.FallaTecnica;
import org.example.personas.Persona;
import org.example.repositorios.RepoIncidente;
import org.example.repositorios.RepoPersona;
import org.example.repositorios.RepoTarjetas;
import org.example.tarjetas.SolicitudDeApertura;
import org.example.tarjetas.TarjetaColaborador;
import org.example.colaboraciones.contribuciones.ofertas.Oferta;
import org.example.excepciones.PuntosInsuficienteParaCanjearOferta;
import org.example.colaboraciones.Contribucion;
import org.example.repositorios.RepoContribucion;


import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter

public class Colaborador extends Rol {
    private List<Contribucion> formasContribucion;
    private float puntuaje;
    private List<Oferta> ofertasCanjeadas;
    private TarjetaColaborador tarjetaColaborador;

    public Colaborador(){
        this.formasContribucion = new ArrayList<>();
        this.ofertasCanjeadas = new ArrayList<>();
        this.puntuaje = 0;
    }

    public void agregarContribucion(Contribucion contribucion)
    {
        RepoContribucion.getInstancia().agregarContribucion(contribucion);
        this.formasContribucion.add(contribucion);
    }

    public void calcularPuntuaje()
    {

        for (Contribucion contribucion:this.formasContribucion) {
            this.puntuaje += contribucion.obtenerPuntaje();
        }

        int puntosCanjeados = 0;

        for(Oferta oferta: this.ofertasCanjeadas){
            puntosCanjeados = oferta.getPuntosNecesarios();
        }

        this.puntuaje  -= puntosCanjeados;
    }

    public void canjearOferta(Oferta oferta) throws PuntosInsuficienteParaCanjearOferta {
        if(this.puedeCanjearOferta(oferta)){
            this.ofertasCanjeadas.add(oferta);
            this.puntuaje -= oferta.getPuntosNecesarios();
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

        SolicitudDeApertura solicitudDeApertura = new SolicitudDeApertura(heladera,LocalDateTime.now(), this.getTarjetaColaborador());
        Broker broker = new Broker();
        broker.gestionarSolicitudApertura(solicitudDeApertura, this.getPersona());
    }
    //se llama cuando un colaborador reporta una falla
    public void reportarFallaTecnica(String descripcion, String foto, Heladera heladera) throws MessagingException {
        FallaTecnica fallaTecnica = new FallaTecnica(this.getPersona(),descripcion,foto,heladera,"Falla Técnica",LocalDateTime.now());

        fallaTecnica.reportarIncidente();
        RepoIncidente.getInstancia().agregarFalla(fallaTecnica);
    }
    public int cantidadDeViandasDistribuidasEnLaSemana(LocalDateTime inicioSemana, LocalDateTime finSemana){

       return this.formasContribucion.stream()
                .filter(contribucion -> contribucion instanceof DonacionDeViandas &&
                        (contribucion.getFecha().isAfter(ChronoLocalDate.from(inicioSemana)) &&
                        contribucion.getFecha().isBefore(ChronoLocalDate.from(finSemana))
                        ) || (contribucion.getFecha().equals(inicioSemana.toLocalDate()) ||
                                    contribucion.getFecha().equals(finSemana.toLocalDate())
                            )
                        )
                .mapToInt((contribucion -> ((DonacionDeViandas) contribucion).getCantidadDeViandas()))
                .sum();
    }

    public Persona getPersona(){
        return RepoPersona.getInstancia().buscarPersonaAsociadaAlRol(this);
    }
}
