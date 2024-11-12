package org.example.personas.contacto;

import lombok.Getter;
import lombok.Setter;
import org.example.autenticacion.Usuario;
import org.example.personas.Persona;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Mensaje {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idMensaje;

    private String titulo;
    private String contenido;
    private LocalDateTime fechaDeEnvio;
    @ManyToOne
    private Persona destinatario;

    public Mensaje(String titulo, String contenido, Persona destinatario) {
        this.titulo = titulo;
        this.contenido = contenido;
        this.destinatario = destinatario;
        this.fechaDeEnvio = null;
    }
}