package org.example.tarjetas;

import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.colaboraciones.contribuciones.heladeras.Uso;
import org.example.excepciones.LimiteDeUsosDiariosSuperados;
import org.example.personas.roles.PersonaEnSituacionVulnerable;
import org.example.personas.roles.Rol;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Entity
public class TarjetaHeladera extends Tarjeta{

    @Transient
    private static final int USOS_MINIMOS_POR_DIA = 4;

    @Transient
    private static final int MULTIPLICADOR_POR_HIJOS = 2;

    @Getter
    @Setter
    private Integer limiteDeUsuarios;
    @Getter
    @Setter
    private Integer cantidadDeUsosEnElDia;
    @Getter
    @Setter
    private LocalDate diaActual;

    public TarjetaHeladera(){
        this.setUsos(new ArrayList<>());
        this.limiteDeUsuarios = 0;
        this.cantidadDeUsosEnElDia = 0;
        this.diaActual = LocalDate.now();
    }
    public void calcularLimiteTarjeta(Integer cantMenores){
        this.limiteDeUsuarios =  USOS_MINIMOS_POR_DIA + MULTIPLICADOR_POR_HIJOS * cantMenores;
    }
    @Override
    public void usar(Rol duenio, Heladera heladera) throws LimiteDeUsosDiariosSuperados { // AGREGUE HELADERA AL METODO
        if(duenio instanceof PersonaEnSituacionVulnerable pDuenio)
        {
            if(puedeUsarTarjeta(pDuenio)){
                LocalDateTime fechaActual = LocalDateTime.now();
                Uso nuevoUso = new Uso(fechaActual, heladera);

                this.getUsos().add(nuevoUso);
                cantidadDeUsosEnElDia++;
            } else {
                throw new LimiteDeUsosDiariosSuperados();
            }
        }
    }

    private boolean puedeUsarTarjeta(PersonaEnSituacionVulnerable duenio){
        this.calcularLimiteTarjeta(duenio.getCantMenores());
        if(this.diaActual.isBefore(LocalDate.now()) ){
            this.cantidadDeUsosEnElDia  = 0;
        }
        return (this.cantidadDeUsosEnElDia+1) <= this.limiteDeUsuarios;
    }
}
