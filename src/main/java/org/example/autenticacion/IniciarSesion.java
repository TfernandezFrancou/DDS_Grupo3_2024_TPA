package org.example.autenticacion;


import org.example.config.Configuracion;
import org.example.excepciones.UserException;

import java.time.LocalDateTime;

public class IniciarSesion {
  private int intentosRealizados = 0;
  private int agregarSegundos = 0;

  public boolean iniciarSesion(String username, String password) {
    boolean pudoIniciarSesion = false; // por el momento devuelvo un boolean para identificar si se pudo iniciar sesión
    if (intentosRealizados == 3) {
      try {
        System.out.println(Configuracion.obtenerProperties("mensaje.inicio-seccion.volver-a-intentar")); //Cambiar por un redirect

        int segundos = 10 + agregarSegundos;
        while (segundos != 0) {
          System.out.println(segundos);
          segundos--;
          Thread.sleep(1000);
          intentosRealizados = 0;
        }
        agregarSegundos += 5;
      } catch (InterruptedException ie) {
        Thread.currentThread().interrupt();
      }
    }

    try {
      Usuario usuarioQueQuiereIngresar = ValidarUsuario.getInstancia().validarUsuarioIngresado(username, password); //válido que el usuario esté en una lista de usuarios registrados
      if (usuarioQueQuiereIngresar.getFechaExpiracionContrasenia().isAfter(LocalDateTime.now())) { // valido si la fecha de expiración es mayor a hoy
        System.out.println(Configuracion.obtenerProperties("mensaje.inicio-seccion.contrasenia-expirada"));
      } else {
        System.out.println(Configuracion.obtenerProperties("mensaje.inicio-seccion.inicio-seccion-correcto"));
      }
      intentosRealizados = 0;
      pudoIniciarSesion = true;
    } catch (UserException e) {
      System.out.println(e.getMessage());
      intentosRealizados++;
    }
    return pudoIniciarSesion;
  }
}