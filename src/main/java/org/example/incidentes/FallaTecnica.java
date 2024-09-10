package org.example.incidentes;

import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.personas.Persona;

import javax.mail.MessagingException;
import java.time.LocalDateTime;

@Getter
@Setter
public class FallaTecnica extends Incidente {
    private Persona colaborador;
    private String descripcion;
    private String foto;

    public FallaTecnica(Persona colaborador, String descripcion, String foto, Heladera heladera, String tipoDeIncidente, LocalDateTime fechaDeEmision) throws MessagingException {
        super(heladera, tipoDeIncidente,fechaDeEmision);
        this.colaborador = colaborador;
        this.descripcion = descripcion;
        this.foto = foto;
    }
}
