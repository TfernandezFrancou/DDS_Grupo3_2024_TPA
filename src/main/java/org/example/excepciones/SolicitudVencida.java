package org.example.excepciones;

public class SolicitudVencida extends Exception {
    public SolicitudVencida(String m) {
        super(m);
    }
}