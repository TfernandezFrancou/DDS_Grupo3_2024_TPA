package org.example.excepciones;

public class PersonaInexistenteException extends RuntimeException{
    public PersonaInexistenteException(String msg) {
        super(msg);
    }
}
