package org.example.autenticacion;

import org.example.repositorios.RepoUsuario;

public class ValidarUsuario {
  private static ValidarUsuario instancia = null;

  private ValidarUsuario() {}

  public static ValidarUsuario getInstancia() {
    if (instancia == null) {
      instancia = new ValidarUsuario();
    }
    return instancia;
  }


  public Usuario validarUsuarioIngresado(String nombreDeUsuario, String contrasenia) {
    // busca a usuario por usuario en la db
    //  en caso de que no este lanza un UserExeption
    //  si lo encuentro el usuario retorno el usuario
    Usuario user = RepoUsuario.getInstancia().obtenerUsuarioPorNombreDeUsuario(nombreDeUsuario);

    if(HashGenerator.verify(contrasenia,user.getContrasenia())){
      return user;
    } else {
      return null;
    }
  }

}
