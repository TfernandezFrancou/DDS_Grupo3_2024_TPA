package org.example.personas.contacto;

import lombok.Getter;
import lombok.Setter;
import org.example.config.Configuracion;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;


public class CorreoElectronico implements MedioDeContacto {
    private static final String SMTP_HOST = Configuracion.obtenerProperties("mail.smtp.host");
    private static final String SMTP_PORT = Configuracion.obtenerProperties("mail.smtp.port");

    @Getter
    @Setter
    public String mail;
    @Override
    public void notificar(String subject, String texto) throws MessagingException {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", SMTP_HOST);
        properties.put("mail.smtp.port", SMTP_PORT);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        final String username = "accesoalimentario@gmail.com";
        //TODO CONFIGURAR CONTRASEÑA
        final String password = "";

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(this.mail));
            message.setSubject(subject);
            message.setText(texto);

            Transport.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}
