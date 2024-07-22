package org.example.Tarjetas;

import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.personas.Persona;

@Getter
@Setter
public class TarjetaColaborador extends Tarjeta  {
    private int limiteDeTiempoDeUso;

    @Override
    public void usar(Persona duenio, Heladera heladera)
    {
        //TODO
    }
}
