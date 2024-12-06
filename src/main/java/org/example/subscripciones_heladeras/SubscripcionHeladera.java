package org.example.subscripciones_heladeras;

import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.personas.Persona;
import org.example.personas.contacto.MedioDeContacto;

import javax.mail.MessagingException;
import javax.persistence.*;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="discriminator")
public abstract class SubscripcionHeladera {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idSuscripcion;

    @ManyToOne
    protected Persona subscriptor;

    @ManyToOne
    protected MedioDeContacto medioDeContactoElegido;

    @ManyToOne
    private Heladera heladera;

    public abstract void notificar(Heladera heladera) throws MessagingException;
}
