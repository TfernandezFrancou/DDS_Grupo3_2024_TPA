package org.example.colaboraciones.contribuciones;

import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.Contribucion;
import org.example.colaboraciones.TipoDePersona;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.colaboraciones.contribuciones.viandas.Vianda;
import org.example.config.Configuracion;
import org.example.excepciones.SolicitudInexistente;
import org.example.personas.roles.Colaborador;
import org.example.repositorios.RepoApertura;
import org.example.tarjetas.Apertura;
import org.example.tarjetas.TipoDeApertura;
import org.example.validadores.VerificadorAperturaHeladera;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Getter
@Setter
@Entity
@PrimaryKeyJoinColumn(name = "id_contribucion")
public class DonacionDeViandas extends Contribucion {
    @ManyToOne
    private Heladera heladera;
    @OneToMany(cascade = CascadeType.PERSIST)
    private List<Vianda> viandas;

    private Integer cantidadDeViandas = 0;

    public DonacionDeViandas(){
        this.tiposDePersona = Set.of(TipoDePersona.HUMANA);
        this.viandas= new ArrayList<>();
    }

    public DonacionDeViandas(Heladera heladera, Colaborador colaborador, List<Vianda> viandas, LocalDate fecha){
        this.tiposDePersona = Set.of(TipoDePersona.HUMANA);
        this.viandas= viandas;
        this.cantidadDeViandas = viandas.size();
        this.heladera = heladera;
        this.colaborador = colaborador;
        this.setFecha(fecha);
    }

    public DonacionDeViandas(LocalDate fecha, Integer cantidad) {
        this.tiposDePersona = Set.of(TipoDePersona.HUMANA);
        this.setFecha(fecha);
        this.cantidadDeViandas = cantidad;
    }

    @Override
    public void ejecutarContribucion() throws Exception {
        super.ejecutarContribucion();

        if(VerificadorAperturaHeladera.getInstancia().puedeAbrirHeladera(heladera, colaborador)){
            colaborador.getTarjetaColaborador().usar(colaborador, heladera);

            RepoApertura.getInstancia().agregarApertura(
                    new Apertura(
                            colaborador.getTarjetaColaborador(),
                            heladera,
                            LocalDateTime.now(),
                            TipoDeApertura.APERTURA_FEHACIENTE
                    )
            );

            heladera.notificarCambioViandas(viandas, List.of());
            //si quiere volver a abrir la heladera, debe solicitar autorizacion
            heladera.desautorizarColaborador(colaborador.getPersona());
        }else {
           throw new SolicitudInexistente(Configuracion.obtenerProperties("mensaje.apertura-heladera.solicitud-heladera-inexistente"));
        }
    }


    public void agregarVianda(Vianda vianda){
        this.viandas.add(vianda);
        cantidadDeViandas++;
    }

    @Override
    public float getCoeficientePuntaje() {
        return 1.5f;
    }

    @Override
    public float obtenerPuntaje(){
        return cantidadDeViandas  * this.getCoeficientePuntaje();
    }
}
