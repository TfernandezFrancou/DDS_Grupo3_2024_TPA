package org.example.validaciones;

import org.example.config.Configuracion;

public class FormatoContrasenia extends ValidacionContrasenia {
  public FormatoContrasenia() {
    super(Configuracion.obtenerProperties("mensaje.validacion.formato-incorrecto"));
  }

  @Override
  public boolean condition(String password) {
    return !password.matches("(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=_])(?=\\S+$).{8,}");
    /*
      (?=.*[0-9]) Un dígito debe aparecer al menos una vez
      (?=.*[a-z]) Una letra minúscula debe aparecer al menos una vez
      (?=.*[A-Z]) Una letra mayúscula debe aparecer al menos una vez
      (?=.*[@#$%^&+=]) Un caracter especial debe aparecer al menos una vez
      (?=\\S+$) No se permiten espacios en blanco en toda la cadena
      {8,} Maximo 8 caracteres
    */
  }
}
