package org.example.colaboraciones.contribuciones;

import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.Contribucion;
import org.example.colaboraciones.TipoDePersona;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.colaboraciones.contribuciones.viandas.Vianda;
import org.example.config.Configuracion;
import org.example.excepciones.LimiteDeTiempoSuperado;
import org.example.excepciones.SolicitudInexistente;
import org.example.repositorios.RepoApertura;
import org.example.tarjetas.Apertura;
import org.example.tarjetas.TipoDeApertura;
import org.example.validadores.VerificadorAperturaHeladera;

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class DistribucionDeViandas extends Contribucion {
    private Heladera origen;
    private Heladera destino;
    private Integer cantidad;
    private String motivo;
    private List<Vianda> viandas;

    public DistribucionDeViandas(LocalDate fecha, Integer cantidad) {
        this.tiposDePersona = Set.of(TipoDePersona.HUMANA);
        this.setFecha(fecha);
        this.cantidad = cantidad;
        this.viandas = new ArrayList<>();
    }

    public DistribucionDeViandas(LocalDate fecha, List<Vianda> viandas) {
        this.tiposDePersona = Set.of(TipoDePersona.HUMANA);
        this.setFecha(fecha);
        this.cantidad = viandas.size();
        this.viandas = viandas;
    }

    @Override
    public void ejecutarContribucion() throws Exception {
        super.ejecutarContribucion();

        this.ejecutarAperturaHeladera(origen, List.of(), viandas);
        this.ejecutarAperturaHeladera(destino, viandas, List.of());

    }

    private void ejecutarAperturaHeladera(Heladera heladera, List<Vianda> viandasIntroducidas, List<Vianda> viandasSacadas) throws SolicitudInexistente, LimiteDeTiempoSuperado, MessagingException {
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

            heladera.notificarCambioViandas(viandasIntroducidas,viandasSacadas);
            //si quiere volver a abrir la heladera, debe solicitar autorizacion
            heladera.desautorizarColaborador(colaborador.getPersona());
        }else {
            throw new SolicitudInexistente(Configuracion.obtenerProperties("mensaje.apertura-heladera.solicitud-heladera-inexistente"));
        }
    }
    public void agregarVianda(Vianda vianda){
        this.viandas.add(vianda);
    }
    public void quitarVianda(Vianda vianda){
        this.viandas.remove(vianda);
    }

    @Override
    public float getCoeficientePuntaje() {
        return 1;
    }

    @Override
    public float obtenerPuntaje(){
        return cantidad * this.getCoeficientePuntaje();
    }
}
