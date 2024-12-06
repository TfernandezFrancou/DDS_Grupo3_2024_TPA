package org.example.personas.contacto;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.config.Configuracion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.persistence.Entity;
import javax.persistence.Transient;
import java.io.IOException;

@Entity
@NoArgsConstructor
public class CorreoElectronico extends MedioDeContacto {
    @Getter
    @Setter
    private String mail;

    public CorreoElectronico(String mail) {
        this.mail = mail;
    }

    @Transient
    private static final String KEY = Configuracion.obtenerProperties("twilio.sendgrid.key");
    @Transient
    private static final String SENDER = Configuracion.obtenerProperties("twilio.sendgrid.sender");
    @Transient
    private static final Logger log = LoggerFactory.getLogger(CorreoElectronico.class);

    @Override
    public void notificar(Mensaje mensaje) throws MessagingException {
        if(super.esAmbienteDePrueba()){
            return;
        }
        Email from = new Email(SENDER);
        String subject = mensaje.getTitulo();
        Email to = new Email(mail);
        Content content = new Content("text/plain", mensaje.getContenido());
        Mail email = new Mail(from, subject, to, content);
        SendGrid sg = new SendGrid(KEY);
        Request request = new Request();
        try {
          request.setMethod(Method.POST);
          request.setEndpoint("mail/send");
          request.setBody(email.build());
          Response response = sg.api(request);
          if(log.isInfoEnabled()){
              log.info(String.valueOf(response.getStatusCode()));
              log.info(response.getBody());
              log.info(String.valueOf(response.getHeaders()));
          }
        } catch (IOException ex) {
            ex.printStackTrace();
          throw new MessagingException(ex.getMessage());
        }
    }

}
