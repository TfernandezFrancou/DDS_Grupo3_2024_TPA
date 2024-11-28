package org.example.autenticacion;


import com.twilio.rest.chat.v1.service.User;
import org.example.config.Configuracion;
import org.example.excepciones.UserException;

import java.time.LocalDateTime;

public class IniciarSesion {
  private int intentosRealizados = 0;
  private int agregarSegundos = 0;

  public boolean iniciarSesion(String username, String password)throws UserException {
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
      if(usuarioQueQuiereIngresar == null){
        throw new UserException(Configuracion.obtenerProperties("mensaje.inicio-seccion.contrasenia-incorrecta"));
      }
      if (usuarioQueQuiereIngresar.getFechaExpiracionContrasenia().isAfter(LocalDateTime.now())) { // valido si la fecha de expiración es mayor a hoy
        //throw new UserException(Configuracion.obtenerProperties("mensaje.inicio-seccion.contrasenia-expirada"));
        //TODO lo ignoro por ahora, porque sino hay que crear otra vista para cambiar la contraseña expirada
      } else {
        System.out.println(Configuracion.obtenerProperties("mensaje.inicio-seccion.inicio-seccion-correcto"));
      }
      intentosRealizados = 0;
      pudoIniciarSesion = true;
      SessionManager.getInstancia().crearSesion("usuario",usuarioQueQuiereIngresar);
    } catch (UserException e) {
      intentosRealizados++;
      throw e;//devuelvo el mismo error para que se muestre en la vista
    }
    return pudoIniciarSesion;
  }
}