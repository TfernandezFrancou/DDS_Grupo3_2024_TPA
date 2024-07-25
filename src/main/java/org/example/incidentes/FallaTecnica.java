package org.example.incidentes;

import org.example.personas.Persona;

public class FallaTecnica extends Incidente {
    private Persona colaborador;
    private String descripcion;
    private String foto;

    public FallaTecnica(Persona colaborador, String descripcion, String foto) {
        this.colaborador = colaborador;
        this.descripcion = descripcion;
        this.foto = foto;
    }
}
