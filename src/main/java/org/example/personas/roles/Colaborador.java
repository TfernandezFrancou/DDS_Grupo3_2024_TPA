package org.example.personas.roles;

import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.contribuciones.DistribucionDeViandas;
import org.example.colaboraciones.contribuciones.DonacionDeViandas;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.tarjetas.SolicitudDeApertura;
import org.example.tarjetas.TarjetaColaborador;
import org.example.colaboraciones.contribuciones.ofertas.Oferta;
import org.example.excepciones.PuntosInsuficienteParaCanjearOferta;
import org.example.colaboraciones.Contribucion;
import org.example.repositorios.RepoContribucion;


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

    public SolicitudDeApertura emitirAvisoHeladera(Heladera heladera){
        // TODO emitirAvisoHeladera: falta enviarlo a la heladera (broker?)
        return new SolicitudDeApertura(heladera,LocalDateTime.now(), this.getTarjetaColaborador());
    }
    public void reportarFallaTecnica(){
        // TODO reportarFallaTecnica porque iria? entiendo que el tecnico haria un post y el controller crea la falla tecnica, sino que se hace aca?
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
}
