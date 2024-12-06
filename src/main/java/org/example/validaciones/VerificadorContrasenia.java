package org.example.validaciones;

import java.util.Arrays;
import java.util.List;

public class VerificadorContrasenia {
    private final List<ValidacionContrasenia> validaciones = Arrays.asList(new CaracterRepetido(), new LongitudMinimaContrasenia(), new ValidacionTopPeoresContrasenia(), new FormatoContrasenia());
    private static VerificadorContrasenia instancia = null;

    private VerificadorContrasenia() {
    }

    public static VerificadorContrasenia getInstancia() {
        if (instancia == null) {
            instancia = new VerificadorContrasenia();
        }
        return instancia;
    }



    public void validarContrasenia(String contrasenia) {
        validaciones.forEach(validacion -> validacion.validate(contrasenia));
    }

}
