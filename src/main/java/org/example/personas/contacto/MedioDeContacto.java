package org.example.personas.contacto;

import lombok.Getter;
import lombok.Setter;

import javax.mail.MessagingException;
import javax.persistence.*;


@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "discriminator")
public abstract class MedioDeContacto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private int idMedioDeContacto;

    public abstract void notificar(Mensaje mensaje) throws MessagingException;

    protected boolean esAmbienteDePrueba() {
        return System.getProperty("env") != null && System.getProperty("env").equals("test");
    }
}