package org.example.personas.contacto;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import lombok.Getter;
import lombok.Setter;
import org.example.config.Configuracion;

@Getter
@Setter

public class Whatsapp implements MedioDeContacto {
    private static final String ACCOUNT_SID = Configuracion.obtenerProperties("twilio.account-sid");
    private static final String AUTH_TOKEN = Configuracion.obtenerProperties("twilio.auth-token");
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
