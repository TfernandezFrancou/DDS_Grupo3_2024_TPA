package org.example.colaboraciones.contribuciones.heladeras;

import lombok.Getter;
import lombok.Setter;
import org.example.broker.Broker;
import org.example.incidentes.Alerta;

import javax.mail.MessagingException;
import java.time.LocalDateTime;

public abstract class Sensor {
    @Getter
    @Setter
    private Heladera heladera;

    public void agregar(Heladera heladera) {
        this.heladera = heladera;
    }

    public void quitar(Heladera heladera) {
        this.heladera = null;
    }

    public void notificar() throws MessagingException {
        heladera.actualizarEstadoHeladera(this);
    }

    public abstract boolean getEstadoHeladera() throws MessagingException;

    public void emitirAlerta(String tipoDeAlerta) throws MessagingException {
        Alerta alerta = new Alerta(tipoDeAlerta, heladera,"Alerta", LocalDateTime.now());
        Broker broker = new Broker();
        broker.gestionarAlerta(alerta);
    }

    public void emitirAlertaFallaDeConexion() throws MessagingException {//lo dispara automáticamente el sensor fisico
        this.emitirAlerta("Falla en la conexión");
    }
}
