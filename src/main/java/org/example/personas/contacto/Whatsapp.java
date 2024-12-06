package org.example.personas.contacto;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.config.Configuracion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Entity;
import javax.persistence.Transient;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Whatsapp extends MedioDeContacto {
    @Transient
    private static final String ACCOUNT_SID = Configuracion.obtenerProperties("twilio.account-sid");
    @Transient
    private static final String AUTH_TOKEN = Configuracion.obtenerProperties("twilio.auth-token");
    @Transient
    private static final String WHATSAPP_SENDER = Configuracion.obtenerProperties("twilio.whatsapp-sender");

    private String telefono;

    @Transient
    private static final Logger log = LoggerFactory.getLogger(Whatsapp.class);

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
        log.info("status: {}", message.getStatus());
        log.info("sid: {}", message.getSid());
    }
}
