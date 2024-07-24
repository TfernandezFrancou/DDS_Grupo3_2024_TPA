package org.example.Tarjetas;

import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.colaboraciones.contribuciones.heladeras.Uso;
import org.example.excepciones.LimiteDeUsosDiariosSuperados;
import org.example.personas.Persona;

import java.util.List;

@Getter
@Setter
public abstract class Tarjeta {
    private String id;
    private List<Uso> usos;

    public abstract void usar(Persona duenio, Heladera heladera) throws LimiteDeUsosDiariosSuperados;
}
