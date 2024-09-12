package org.example.colaboraciones.contribuciones;

import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.Contribucion;
import org.example.colaboraciones.TipoDePersona;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.config.Configuracion;
import org.example.excepciones.LimiteDeTiempoSuperado;
import org.example.excepciones.SolicitudInexistente;
import org.example.repositorios.RepositorioAperturasHeladera;
import org.example.tarjetas.AperturaHeladera;
import org.example.validadores.VerificadorAperturaHeladera;

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
public class DistribucionDeViandas extends Contribucion {
    private Heladera origen;
    private Heladera destino;
    private Integer cantidad;
    private String motivo;

    public DistribucionDeViandas(LocalDate fecha, Integer cantidad) {
        this.tiposDePersona = Set.of(TipoDePersona.HUMANA);
        this.setFecha(fecha);
        this.cantidad = cantidad;
    }

    @Override
    public void ejecutarContribucion() throws Exception {
        super.ejecutarContribucion();

        this.ejecutarAperturaHeladera(origen, 0, cantidad);
        this.ejecutarAperturaHeladera(destino, cantidad, 0);

    }

    private void ejecutarAperturaHeladera(Heladera heladera, int viandasIntroducidas, int viandasSacadas) throws SolicitudInexistente, LimiteDeTiempoSuperado, MessagingException {
        if(VerificadorAperturaHeladera.getInstancia().puedeAbrirHeladera(heladera, colaborador)){
            colaborador.getTarjetaColaborador().usar(colaborador, heladera);


            RepositorioAperturasHeladera.getInstancia().agregarApertura(
                    new AperturaHeladera(
                            heladera,
                            colaborador.getTarjetaColaborador(),
                            LocalDateTime.now()
                    )
            );

            heladera.notificarCambioViandas(viandasIntroducidas,viandasSacadas);
            //si quiere volver a abrir la heladera, debe solicitar autorizacion
            heladera.desautorizarColaborador(colaborador.getPersona());
        }else {
            throw new SolicitudInexistente(Configuracion.obtenerProperties("mensaje.apertura-heladera.solicitud-heladera-inexistente"));
        }
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
