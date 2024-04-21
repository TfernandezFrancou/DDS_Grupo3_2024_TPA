package org.example.validaciones;


import org.example.Authenticacion.Usuario;

public class ValidarUsuario {
  private static ValidarUsuario instancia;

  private ValidarUsuario() {
  }

  public static ValidarUsuario getInstancia() {
    if (instancia != null) {
      instancia = new ValidarUsuario();
    }
    return instancia;
  }


  public Usuario validarUsuarioIngresado(String nombre, String contrasenia) {
    // TODO buscar a usuario por usuario y contrasena en la db
    //  en caso de que no este thow exeption
    //  si lo encuentro el usuario en la lista retorno el usuario

    return new Usuario();
  }

}
