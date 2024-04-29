package org.example.validaciones;

public class CaracterRepetido extends ValidacionContrasenia {

  public CaracterRepetido() {
    super("Hay caracteres repetidos!");
  }

  @Override
  public boolean condition(String username, String password) {
    boolean esRepetido = true;
    // Recorremos la cadena de caracteres
    for (int i = 0; i < password.length() - 1; i++) {
      char caracterActual = password.charAt(i);
      char siguienteCaracter = password.charAt(i + 1);

      // Si encontramos dos caracteres consecutivos diferentes, no hay repeticiones consecutivas
      if (caracterActual != siguienteCaracter) {
        esRepetido = false;
      }
    }

    // Si llegamos hasta aquí, todos los caracteres son iguales y consecutivos
    return esRepetido;
  }
}
