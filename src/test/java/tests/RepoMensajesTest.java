package tests;

import com.fasterxml.jackson.databind.jsontype.impl.AsExternalTypeSerializer;
import org.example.personas.Persona;
import org.example.personas.PersonaHumana;
import org.example.personas.contacto.Mensaje;
import org.example.repositorios.RepoMensajes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.example.personas.Persona;

import java.time.LocalDateTime;
import java.util.List;

public class RepoMensajesTest {

    private RepoMensajes repoMensajes;
    private Mensaje mensaje;
    private PersonaHumana destinatario;

    @BeforeEach
    public void setUp() {
        this.repoMensajes = RepoMensajes.getInstancia();
        this.repoMensajes.clean();

        mensaje = new Mensaje();
        destinatario = new PersonaHumana();
        mensaje.setDestinatario(destinatario);
        this.repoMensajes.agregarMensaje(mensaje);
    }

    private void limpiarMensajes() {
        List<Mensaje> mensajes = this.repoMensajes.obtenerMensajes();
        for (Mensaje mensaje : mensajes) {
            this.repoMensajes.quitarMensaje(mensaje);
        }
    }

    @Test
    public void testAgregarMensaje() {
        Mensaje mensaje = new Mensaje();
        Persona destinatario1 = new PersonaHumana();
        mensaje.setDestinatario(destinatario1);
        this.repoMensajes.agregarMensaje(mensaje);

        Assertions.assertEquals(2, repoMensajes.getMensajes().size());
    }

    @Test
    void testQuitarMensaje() {
        this.repoMensajes.quitarMensaje(this.mensaje);

        Assertions.assertEquals(0, this.repoMensajes.getMensajes().size());
    }

    @Test
    void testObtenerMensajes() {
        List<Mensaje> mensajes = this.repoMensajes.obtenerMensajes();

        Assertions.assertEquals(1, mensajes.size());
    }

    @Test
    void testObtenerMensajesPorDestinatario() {
        List<Mensaje> mensajes = this.repoMensajes.obtenerMensajesPorDestinatario(destinatario);

        Assertions.assertEquals(1, mensajes.size());
    }
}
