package org.example.colaboraciones.contribuciones.heladeras;

import lombok.Getter;
import lombok.Setter;
import org.example.broker.Broker;
import org.example.excepciones.EmailNoRegistradoException;
import org.example.incidentes.Alerta;

import javax.mail.MessagingException;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="discriminator")
public abstract class Sensor {

    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idSensor;

    @Getter
    @Setter
    @ManyToOne
    private Heladera heladera;

    public void notificar() throws MessagingException, EmailNoRegistradoException {
        heladera.actualizarEstadoHeladera(this.getEstadoHeladera());
    }

    public abstract boolean getEstadoHeladera() throws MessagingException, EmailNoRegistradoException;

    public void emitirAlerta(String tipoDeAlerta) throws MessagingException, EmailNoRegistradoException {
        Alerta alerta = new Alerta(tipoDeAlerta, heladera,"Alerta", LocalDateTime.now());
        Broker broker = new Broker();
        broker.gestionarAlerta(alerta);
    }

    public void emitirAlertaFallaDeConexion() throws MessagingException, EmailNoRegistradoException {//lo dispara automáticamente el sensor fisico
        this.emitirAlerta("Falla en la conexión");
    }
}
