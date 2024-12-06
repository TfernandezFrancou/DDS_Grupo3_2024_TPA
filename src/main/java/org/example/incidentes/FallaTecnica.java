package org.example.incidentes;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.personas.Persona;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class FallaTecnica extends Incidente {
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Persona colaborador;

    private String descripcion;

    @Column(columnDefinition = "TEXT") //la url puede ser muy larga por eso no basta con un varchar
    private String foto;

    public FallaTecnica(Persona colaborador, String descripcion, String foto, Heladera heladera, String tipoDeIncidente, LocalDateTime fechaDeEmision) {
        super(heladera, tipoDeIncidente,fechaDeEmision);
        this.colaborador = colaborador;
        this.descripcion = descripcion;
        this.foto = foto;
    }

    public FallaTecnica(Heladera heladera, String tipoDeIncidente, LocalDateTime fechaDeEmision) {
        super(heladera, tipoDeIncidente, fechaDeEmision);
        //foto placeholder
        this.foto = "https://img.freepik.com/vector-premium/refrigerador-congelador-domestico-almacenamiento-alimentos-icono-vector-estilo-arte-linea-aislar-blanco_456865-713.jpg";
    }
}
