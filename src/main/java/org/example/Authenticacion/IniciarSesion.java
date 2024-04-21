package org.example.Authenticacion;


import org.example.excepciones.UserException;
import org.example.validaciones.ValidarUsuario;

import java.time.LocalDateTime;

public class IniciarSesion {
  private int cont = 0;
  int agregarSegundos = 0;

  public boolean iniciarSesion(String username, String password) {
    boolean pudoIniciarSesion = false; // por el momento devuelvo un boolean para identificar si se pudo iniciar sesión
    if (cont == 3) {
      try {
        System.out.println("No puede probar iniciar sesión nuevamente hasta en..."); //Cambiar por un redirect

        int segundos = 10 + agregarSegundos;
        while (segundos != 0) {
          System.out.println(segundos);
          segundos--;
          Thread.sleep(1000);
          cont = 0;
        }
        agregarSegundos += 5;
      } catch (InterruptedException ie) {
        Thread.currentThread().interrupt();
      }
    }

    try {
      Usuario usuarioQueQuiereIngresar = ValidarUsuario.getInstancia().validarUsuarioIngresado(username, password); //válido que el usuario esté en una lista de usuarios registrados
      if (usuarioQueQuiereIngresar.getFechaExpiracionContrasenia().isAfter(LocalDateTime.now())) { // valido si la fecha de expiración es mayor a hoy
        System.out.println("Su contraseña expiro y debe cambiarlo para ingresar al sistema");
      } else {
        System.out.println("Pudo iniciar sesión correctamente");
      }
      cont = 0;
      pudoIniciarSesion = true;
    } catch (UserException e) {
      System.out.println(e.getMessage());
      cont++;
    }
    return pudoIniciarSesion;
  }
}