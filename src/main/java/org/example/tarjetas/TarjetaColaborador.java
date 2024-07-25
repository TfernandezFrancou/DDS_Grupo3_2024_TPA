package org.example.tarjetas;

import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.personas.Persona;
import org.example.personas.roles.Colaborador;
import org.example.personas.roles.Rol;

@Getter
@Setter
public class TarjetaColaborador extends Tarjeta  {
    private int limiteDeTiempoEnMinutos;

    @Override
    public void usar(Rol duenio, Heladera heladera)
    {
        //TODO
    }
}
