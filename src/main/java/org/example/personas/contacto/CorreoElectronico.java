package org.example.personas.contacto;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.Getter;
import lombok.Setter;
import org.example.config.Configuracion;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.Entity;
import java.io.IOException;
import java.util.Properties;

@Entity
public class CorreoElectronico extends MedioDeContacto {
    @Getter
    @Setter
    private String mail;

    public CorreoElectronico(String mail) {
        this.mail = mail;
    }

    private static final String KEY = Configuracion.obtenerProperties("twilio.sendgrid.key");
    private static final String SENDER = Configuracion.obtenerProperties("twilio.sendgrid.sender");
    @Override
    public void notificar(Mensaje mensaje) throws MessagingException {
        Email from = new Email(SENDER);
        String subject = mensaje.getTitulo();
        Email to = new Email(mail);
        Content content = new Content("text/plain", mensaje.getContenido());
        Mail mail = new Mail(from, subject, to, content);
        SendGrid sg = new SendGrid(KEY);
        Request request = new Request();
        try {
          request.setMethod(Method.POST);
          request.setEndpoint("mail/send");
          request.setBody(mail.build());
          Response response = sg.api(request);
          System.out.println(response.getStatusCode());
          System.out.println(response.getBody());
          System.out.println(response.getHeaders());
        } catch (IOException ex) {
          throw new MessagingException(ex.getMessage());
        }
    }

}
