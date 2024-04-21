package org.example.validaciones;


public class LongitudMaximaContrasenia extends ValidacionContrasenia {
    private Integer LONGITUD_MAXIMA = 64;

    public LongitudMaximaContrasenia() {
        super("No cumple con el maximo de caracteres!!!");
    }

    @Override
    public boolean condition(String username, String password) {
        return password.length() < LONGITUD_MAXIMA;
    }
}