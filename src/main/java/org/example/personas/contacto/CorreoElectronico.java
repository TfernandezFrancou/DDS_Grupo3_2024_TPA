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

    private static final String USUARIO_FROM_GMAIL = Configuracion.obtenerProperties("mail.from.usrename");
    private static final String CONTRASENIA_FROM_GMAIL = Configuracion.obtenerProperties("mail.from.password_app");


    @Getter
    @Setter
    private String mail;

    @Override
    public void notificar(String subject, String texto) throws MessagingException {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", SMTP_HOST);
        properties.put("mail.smtp.port", SMTP_PORT);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");


        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USUARIO_FROM_GMAIL, CONTRASENIA_FROM_GMAIL);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(USUARIO_FROM_GMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(this.mail));
            message.setSubject(subject);
            message.setText(texto);

            Transport.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
