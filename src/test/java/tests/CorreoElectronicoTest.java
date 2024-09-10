package tests;
import static org.mockito.Mockito.*;

import org.example.personas.contacto.CorreoElectronico;
import org.example.personas.contacto.Mensaje;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
        correoElectronico.setMail("morasofiahidalgo@gmail.com"); // Seteo el correo electrónico
    }

    @Test
    @Disabled
    public void testNotificar() throws MessagingException {

        try (MockedStatic<Transport> mockedTransport = mockStatic(Transport.class)) {
            correoElectronico.notificar(new Mensaje("Bienvenido!", "Bienvenido a la comunidad", null));

            mockedTransport.verify(() -> Transport.send(any(Message.class)));
        }
    }
}