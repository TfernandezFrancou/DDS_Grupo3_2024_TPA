package org.example.validaciones;

public class CaracterRepetido extends ValidacionContrasenia {

  public CaracterRepetido() {
    super("Hay caracteres repetidos!");
  }

  @Override
  public boolean condition(String username, String password) {
    boolean esRepetido = false;
    return esRepetido;
  }
}
