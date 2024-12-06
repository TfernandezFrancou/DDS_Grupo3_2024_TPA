package org.example.validaciones;

import org.example.config.Configuracion;

public class LongitudMaximaContrasenia extends ValidacionContrasenia {

    private static final Integer LONGITUD_MAXIMA = 64;
    public LongitudMaximaContrasenia() {
        super(Configuracion.obtenerProperties("mensaje.validacion.longitud-grande"));
    }

    @Override
    public boolean condition(String password) {
        return password.trim().length() > LONGITUD_MAXIMA;
    }
}
