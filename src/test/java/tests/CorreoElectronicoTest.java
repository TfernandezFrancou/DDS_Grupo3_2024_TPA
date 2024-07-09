package tests;
import static org.mockito.Mockito.*;


import org.example.personas.contacto.CorreoElectronico;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import javax.mail.*;


public class CorreoElectronicoTest {

    @InjectMocks
    private CorreoElectronico correoElectronico;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        correoElectronico.setCorreoElectronico("morasofiahidalgo@gmail.com"); // Seteo el correo electrónico
    }

    @Test
    public void testNotificar() throws MessagingException {

        try (MockedStatic<Transport> mockedTransport = mockStatic(Transport.class)) {
            correoElectronico.notificar("Bienvenido!", "Bienvenido a la comunidad");

            mockedTransport.verify(() -> Transport.send(any(Message.class)));
        }
    }
}



/*
--- manda el mail realmente

public class NotificarTest {

    @Test
    void mandarMail() throws MessagingException {
        CorreoElectronico mail = new CorreoElectronico();
        mail.setCorreoElectronico("morasofiahidalgo@gmail.com");
        mail.notificar("Bienvenido!","Bienvenido a la comunidad");
    }
}
*/