package org.example.excepciones;

public class AlmacenarPersonaVulnerable extends RuntimeException {
    public AlmacenarPersonaVulnerable(String message) {
        super(message);
    }
}
