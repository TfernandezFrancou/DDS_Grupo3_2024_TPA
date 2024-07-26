package org.example.validaciones;

import org.example.excepciones.PasswordException;

public abstract class ValidacionContrasenia {
  protected String mensaje;

  protected ValidacionContrasenia(String mensaje) {
    this.mensaje = mensaje;
  }


  public void validate(String password) {
    if (this.condition(password))
      throw new PasswordException(mensaje);

  }

  protected abstract boolean condition(String password);
}
