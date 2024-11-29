package org.example.personas.contacto;

import lombok.Getter;
import lombok.Setter;
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

    @Column(length = 1000) // Ajustar longitud máxima si el contenido puede ser extenso
    private String contenido;

    @Column(name = "fecha_enviado") // Asegura coincidencia con el DER
    private LocalDateTime fechaDeEnvio;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "id_persona_destinatario", nullable = false) // Define explicitamente la FK
    private Persona destinatario;

    public Mensaje(String titulo, String contenido, Persona destinatario) {
        this.titulo = titulo;
        this.contenido = contenido;
        this.destinatario = destinatario;
        this.fechaDeEnvio = LocalDateTime.now(); // Define fecha actual al enviar el mensaje
    }

    // Constructor sin argumentos necesario para JPA
    public Mensaje() {
    }
}