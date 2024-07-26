package tests;

import org.example.personas.PersonaHumana;
import org.example.personas.contacto.CorreoElectronico;
import org.example.personas.contacto.Mensaje;
import org.example.personas.contacto.Telefono;
import org.example.personas.contacto.Whatsapp;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.mail.MessagingException;

public class ApiTwilioTest {
    @Test
    @Disabled
    void enviarSMS() {
        // al ser una cuenta de prueba, el telefono debe estar habilitado manualmente para poder recibir sms
        Telefono telefono = new Telefono("+541167079611");
        telefono.notificar(new Mensaje("titulo", "soy un sms", new PersonaHumana()));
    }

    @Test
    @Disabled
    void enviarWhatsapp() {
        // al ser una cuenta de prueba, el telefono debe estar habilitado manualmente para poder recibir whatsapps
        Whatsapp wpp = new Whatsapp("+5491167079611");
        wpp.notificar(new Mensaje("hola", "buenas buenas", new PersonaHumana()));
    }

    @Test
    @Disabled
    void enviarEmail() throws MessagingException {
        CorreoElectronico correo = new CorreoElectronico("ffusse@frba.utn.edu.ar");
        correo.notificar(new Mensaje("ASUNTO", "aca va el contenido", new PersonaHumana()));
    }
}
