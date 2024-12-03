package tests;

import com.fasterxml.jackson.databind.jsontype.impl.AsExternalTypeSerializer;
import org.example.personas.Persona;
import org.example.personas.PersonaHumana;
import org.example.personas.contacto.Mensaje;
import org.example.repositorios.RepoMensajes;
import org.example.repositorios.RepoPersona;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.example.personas.Persona;

import java.time.LocalDateTime;
import java.util.List;

public class RepoMensajesTest {

    private RepoMensajes repoMensajes;
    private RepoPersona repoPersona;
    private Mensaje mensaje;
    private PersonaHumana destinatario;

    @BeforeEach
    public void setUp() {
        this.repoMensajes = RepoMensajes.getInstancia();
        this.repoMensajes.clean();
        this.repoPersona = RepoPersona.getInstancia();
        this.repoPersona.clean();
        mensaje = new Mensaje();
        destinatario = new PersonaHumana();
        this.repoPersona.agregar(destinatario);
        mensaje.setDestinatario(destinatario);
        this.repoMensajes.agregarMensaje(mensaje);
    }

    @Test
    public void testAgregarMensaje() {
        Mensaje mensaje = new Mensaje();
        Persona destinatario1 = new PersonaHumana();
        this.repoPersona.agregar(destinatario1);
        mensaje.setDestinatario(destinatario1);
        this.repoMensajes.agregarMensaje(mensaje);

        Assertions.assertEquals(2, repoMensajes.obtenerMensajes().size());
    }

    @Test
    void testQuitarMensaje() {
        this.repoMensajes.quitarMensaje(this.mensaje);

        Assertions.assertEquals(0, repoMensajes.obtenerMensajes().size());
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
