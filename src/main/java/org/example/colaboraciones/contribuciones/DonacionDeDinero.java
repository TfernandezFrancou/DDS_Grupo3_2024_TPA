package org.example.colaboraciones.contribuciones;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.colaboraciones.Contribucion;
import org.example.colaboraciones.TipoDePersona;
import org.example.personas.roles.Colaborador;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@Entity
@PrimaryKeyJoinColumn(name = "id_contribucion")
public class DonacionDeDinero extends Contribucion {
    private float monto;//monto por pago periódico
    private Integer frecuencia;
    private float montoPagado; //monto pagado acumulado hasta el momento

    public DonacionDeDinero(LocalDate fecha, Integer cantidad) {
        this.tiposDePersona = Set.of(TipoDePersona.HUMANA, TipoDePersona.JURIDICA);
        this.setFecha(fecha);
        this.monto = cantidad;
        this.frecuencia = null;
        this.montoPagado = cantidad;
    }

    public DonacionDeDinero(Colaborador colaborador, Integer cantidad, Integer frecuencia) {
        this.tiposDePersona = Set.of(TipoDePersona.HUMANA, TipoDePersona.JURIDICA);
        this.setFecha(LocalDate.now());
        this.colaborador = colaborador;
        this.monto = cantidad;
        this.frecuencia = frecuencia;
        this.montoPagado = cantidad;
    }

    public DonacionDeDinero(Colaborador colaborador, LocalDate fecha, Integer cantidad) {
        this.tiposDePersona = Set.of(TipoDePersona.HUMANA, TipoDePersona.JURIDICA);
        this.setFecha(fecha);
        this.colaborador = colaborador;
        this.monto = cantidad;
        this.frecuencia = null;
        this.montoPagado = cantidad;
    }


    @Override
    public float getCoeficientePuntaje() {
        return 0.5f;
    }

    @Override
    public float obtenerPuntaje(){
        if (frecuencia == null||frecuencia <= 0) {
            return monto  * this.getCoeficientePuntaje();
        }

        //cantidad de días desde la fecha inicial hasta hoy
        long diasTranscurridos = ChronoUnit.DAYS.between(this.getFecha(), LocalDate.now());

        // cantidad de pagos realizados hasta ahora.
        int cantidadPagos = (int) (diasTranscurridos / frecuencia);

        return monto * cantidadPagos * this.getCoeficientePuntaje();
    }
}
