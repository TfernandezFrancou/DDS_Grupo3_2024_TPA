package org.example.personas.contacto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import lombok.Getter;
import lombok.Setter;
import org.example.config.Configuracion;

import javax.persistence.Entity;

@Getter
@Setter
@Entity
public class Whatsapp extends MedioDeContacto {
    @JsonIgnore
    private static final String ACCOUNT_SID = Configuracion.obtenerProperties("twilio.account-sid");
    @JsonIgnore
    private static final String AUTH_TOKEN = Configuracion.obtenerProperties("twilio.auth-token");
    @JsonIgnore
    private static final String WHATSAPP_SENDER = Configuracion.obtenerProperties("twilio.whatsapp-sender");

    private String telefono;

    public Whatsapp(String telefono) {
        this.telefono = telefono;
    }

    @Override
    public void notificar(Mensaje mensaje){
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(
                new com.twilio.type.PhoneNumber("whatsapp:" + telefono),
                new com.twilio.type.PhoneNumber("whatsapp:" + WHATSAPP_SENDER),
                mensaje.getTitulo() + "\n" + mensaje.getContenido()
        ).create();
        System.out.println("status: " + message.getStatus());
        System.out.println("sid: " + message.getSid());
    }
}
