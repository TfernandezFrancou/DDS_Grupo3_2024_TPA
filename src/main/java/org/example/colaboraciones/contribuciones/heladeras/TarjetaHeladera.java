package org.example.colaboraciones.contribuciones.heladeras;

import lombok.Getter;
import lombok.Setter;
import org.example.excepciones.LimiteDeUsosDiariosSuperados;
import org.example.personas.roles.PersonaEnSituacionVulnerable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TarjetaHeladera {
    private static final int usosMinimosPorDia = 4;
    private static final int multiplicadorPorHijos = 2;
    @Getter
    private String id;
    @Getter
    private List<Uso> usos;
    @Getter
    private Integer limiteDeUsuarios;
    @Getter
    private Integer cantidadDeUsosEnElDia;
    @Getter
    @Setter
    private LocalDate diaActual;

    public TarjetaHeladera(){
        this.usos = new ArrayList<>();
        this.limiteDeUsuarios = 0;
        this.cantidadDeUsosEnElDia = 0;
        this.diaActual = LocalDate.now();
    }
    public void calcularLimiteTarjeta(Integer cantMenores){
        this.limiteDeUsuarios =  usosMinimosPorDia + multiplicadorPorHijos * cantMenores;
    }

    public void usar(PersonaEnSituacionVulnerable duenio, Heladera heladera) throws LimiteDeUsosDiariosSuperados { // AGREGUE HELADERA AL METODO
        if(puedeUsarTarjeta(duenio)){
            LocalDateTime fechaActual = LocalDateTime.now();
            Uso nuevoUso = new Uso(fechaActual, heladera);

            this.usos.add(nuevoUso);
            cantidadDeUsosEnElDia++;
        } else {
            throw new LimiteDeUsosDiariosSuperados();
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
