package org.example.autenticacion;

import org.example.config.Configuracion;
import org.example.excepciones.EmailYaRegistrado;
import org.example.excepciones.PasswordException;
import org.example.excepciones.UserException;
import org.example.repositorios.RepoUsuario;
import org.example.validaciones.*;

import java.time.LocalDateTime;

public class RegistrarUsuario {
  private RepoUsuario repoUsuario;


  public RegistrarUsuario() {
    this.repoUsuario = RepoUsuario.getInstancia();
  }


  public Usuario registrarUsuario(String usuario, String contrasenia) throws EmailYaRegistrado, PasswordException {
    VerificadorContrasenia.getInstancia().validarContrasenia(contrasenia); // validamos si cumple con todas las validaciones de contrasenia
    Usuario usuarioNuevo = new Usuario(usuario, contrasenia, LocalDateTime.now()); // Solo si pasa la validación instanciamos el usuario
    //Le sumo 90 días a la fecha que se registró el usuario
    usuarioNuevo.setFechaExpiracionContrasenia(LocalDateTime.now().plusDays(90));

    if(this.repoUsuario.existeNombreUsuarioRegistrado(usuario)){
      throw new UserException(Configuracion.obtenerProperties("mensaje.registrar-usuario.usuario-existente")
              .replace("{username}", usuario));
    }
    System.out.println(Configuracion.obtenerProperties("mensaje.registrar-usuario.registro-correcto"));

    return usuarioNuevo;
  }


}