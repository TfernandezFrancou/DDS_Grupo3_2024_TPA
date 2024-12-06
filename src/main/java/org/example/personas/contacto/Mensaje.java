package org.example.personas.contacto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.personas.Persona;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Mensaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idMensaje;

    private String titulo;

    @Column(columnDefinition = "TEXT") // Ajustar longitud máxima si el contenido puede ser extenso
    private String contenido;

    @Column(name = "fecha_enviado") // Asegura coincidencia con el DER
    private LocalDateTime fechaDeEnvio;

    @ManyToOne
    @JoinColumn(name = "id_persona_destinatario", nullable = false) // Define explicitamente la FK
    private Persona destinatario;

    public Mensaje(String titulo, String contenido, Persona destinatario) {
        this.titulo = titulo;
        this.contenido = contenido;
        this.destinatario = destinatario;
        this.fechaDeEnvio = LocalDateTime.now(); // Define fecha actual al enviar el mensaje
    }

}