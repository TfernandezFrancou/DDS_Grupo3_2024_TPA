package org.example.excepciones;

public class NoRegistroDireccionException extends RuntimeException {

    public NoRegistroDireccionException(String msg){
        super(msg);
    }
}
