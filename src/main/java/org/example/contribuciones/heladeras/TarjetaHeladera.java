package org.example.contribuciones.heladeras;

import lombok.Getter;
import org.example.personas.PersonaEnSituacionVulnerable;

import java.time.LocalDateTime;
import java.util.List;

public class TarjetaHeladera {
    private static final int usosMinimosPorDia = 4;
    private static final int multiplicadorPorHijos = 2;
    @Getter
    private String id;
    private List<Uso> usos;
    private Integer limiteDeUsuarios;
    private Integer cantidadDeUsosEnElDia;

    public void calcularLimiteTarjeta(Integer cantMenores){
        this.limiteDeUsuarios =  usosMinimosPorDia + multiplicadorPorHijos * cantMenores;
    }

    public void usar(PersonaEnSituacionVulnerable duenio, Heladera heladera) { // AGREGUE HELADERA AL METODO
        LocalDateTime fechaActual = LocalDateTime.now();
        Uso nuevoUso = new Uso(fechaActual, heladera);

        this.usos.add(nuevoUso);
        cantidadDeUsosEnElDia++;
    }
}
