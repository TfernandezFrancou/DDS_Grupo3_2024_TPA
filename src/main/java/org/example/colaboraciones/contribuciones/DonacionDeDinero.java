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
import java.util.Optional;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@Entity
@PrimaryKeyJoinColumn(name = "id_contribucion")
public class DonacionDeDinero extends Contribucion {
    private float monto;
    private Integer frecuencia;

    public DonacionDeDinero(LocalDate fecha, Integer cantidad) {
        this.tiposDePersona = Set.of(TipoDePersona.HUMANA, TipoDePersona.JURIDICA);
        this.setFecha(fecha);
        this.monto = cantidad;
        this.frecuencia = null;
    }

    public DonacionDeDinero(Colaborador colaborador, Integer cantidad, Integer frecuencia) {
        this.tiposDePersona = Set.of(TipoDePersona.HUMANA, TipoDePersona.JURIDICA);
        this.setFecha(LocalDate.now());
        this.colaborador = colaborador;
        this.monto = cantidad;
        this.frecuencia = frecuencia;
    }

    public DonacionDeDinero(Colaborador colaborador, LocalDate fecha, Integer cantidad) {
        this.tiposDePersona = Set.of(TipoDePersona.HUMANA, TipoDePersona.JURIDICA);
        this.setFecha(fecha);
        this.colaborador = colaborador;
        this.monto = cantidad;
        this.frecuencia = null;
    }

    @Override
    public void ejecutarContribucion() throws Exception{
        super.ejecutarContribucion();
    }

    @Override
    public float getCoeficientePuntaje() {
        return 0.5f;
    }

    @Override
    public float obtenerPuntaje(){
        if (frecuencia == null) {
            return monto  * this.getCoeficientePuntaje();
        }
        long dias = ChronoUnit.DAYS.between(this.getFecha(), LocalDate.now());
        int pagos = (int) (dias / frecuencia);
        return monto * pagos * this.getCoeficientePuntaje();
    }

}
