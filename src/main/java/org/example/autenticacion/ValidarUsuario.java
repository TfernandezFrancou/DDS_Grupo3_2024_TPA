package org.example.autenticacion;


import org.example.repositorios.Registrados;

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


  public Usuario validarUsuarioIngresado(String nombreDeUsuario, String contrasenia) {
    // busca a usuario por usuario y contrasena en la db
    //  en caso de que no este thow exeption
    //  si lo encuentro el usuario en la lista retorno el usuario
    return Registrados.getInstancia().obtenerUsuarioPorNombreDeUsuarioYContrasenia(nombreDeUsuario, contrasenia);
  }

}
