package org.example.personas.contacto;

import lombok.Getter;
import lombok.Setter;
import org.example.autenticacion.Usuario;

import java.time.LocalDateTime;

@Getter()
@Setter()
public class Mensaje {
    private String titulo;
    private String contenido;
    private LocalDateTime fechaDeEnvio;
    private Usuario destinatario;

    public Mensaje(String titulo, String contenido, Usuario destinatario) {
        this.titulo = titulo;
        this.contenido = contenido;
        this.destinatario = destinatario;
        this.fechaDeEnvio = null;
    }
}