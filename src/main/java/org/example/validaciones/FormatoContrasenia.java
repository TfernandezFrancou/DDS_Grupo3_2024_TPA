package org.example.validaciones;

public class FormatoContrasenia extends ValidacionContrasenia {
  public FormatoContrasenia() {
    super("No cumple con el formato de contrasenia, debe tener al menos un dígito, una mayúscula, una minúscula y un caracter especial.");
  }

  @Override
  public boolean condition(String password) {
    return !password.matches("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}");
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
