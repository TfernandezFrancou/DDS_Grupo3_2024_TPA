package org.example.validaciones;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;


public class ValidacionTopPeoresContrasenia extends ValidacionContrasenia {

  private List<String> contrasenias;
  private final String nombreDeArchivo = "Authenticacion/10-million-password-list-top-10000.txt";

  public ValidacionTopPeoresContrasenia() {
    super("La contraseña es debil");

    this.cargarPeoresContraseniasDesdeArchivo();
  }

  // Verifica mediante un archivo txt de 10k peores contrasenias
  @Override
  public boolean condition(String username, String password) {
    return contrasenias.stream().anyMatch(contrasenia -> contrasenia.equals(password));
  }

  public String getNombreDeArchivo() {
    return nombreDeArchivo;
  }

  private void cargarPeoresContraseniasDesdeArchivo() {
    try {
      InputStream inputStream = getClass().getClassLoader().getResourceAsStream(this.getNombreDeArchivo());
      BufferedReader lector = new BufferedReader(new InputStreamReader(inputStream));
      String linea;
      while ((linea = lector.readLine()) != null) {
        this.contrasenias.add(linea.trim());
      }
      lector.close();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

}
