package org.example.colaboraciones.contribuciones;

import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.Contribucion;
import org.example.colaboraciones.TipoDePersona;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import java.time.LocalDate;
import java.util.Set;

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
        return monto  * this.getCoeficientePuntaje();
    }

}
