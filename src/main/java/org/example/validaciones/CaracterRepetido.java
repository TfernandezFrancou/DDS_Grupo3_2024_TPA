package org.example.validaciones;

import org.example.config.Configuracion;


public class CaracterRepetido extends ValidacionContrasenia {

  public CaracterRepetido() {
      super(Configuracion.obtenerProperties("mensaje.validacion.caracter-repetido"));
  }

  @Override
  public boolean condition(String password) {
    // Recorremos la cadena de caracteres
    for (int i = 0; i < password.length() - 2; i++) {
      char caracterActual = password.charAt(i);
      char siguienteCaracter = password.charAt(i + 1);

      if (caracterActual == siguienteCaracter) {
        return true;
      }
    }
    return false;
  }
}
