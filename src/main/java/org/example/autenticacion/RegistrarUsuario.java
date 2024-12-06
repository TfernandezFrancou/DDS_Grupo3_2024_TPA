package org.example.autenticacion;

import org.example.config.Configuracion;
import org.example.excepciones.PasswordException;
import org.example.excepciones.UserException;
import org.example.repositorios.RepoUsuario;
import org.example.validaciones.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegistrarUsuario {
  private final RepoUsuario repoUsuario;
  private final Logger log = LoggerFactory.getLogger(RegistrarUsuario.class);

  public RegistrarUsuario() {
    this.repoUsuario = RepoUsuario.getInstancia();
  }


  public Usuario registrarUsuario(String usuario, String contrasenia) throws PasswordException {
    VerificadorContrasenia.getInstancia().validarContrasenia(contrasenia); // validamos si cumple con todas las validaciones de contrasenia
    Usuario usuarioNuevo = new Usuario(usuario, contrasenia); // Solo si pasa la validación instanciamos el usuario

    if(this.repoUsuario.existeNombreUsuarioRegistrado(usuario)){
      throw new UserException(Configuracion.obtenerProperties("mensaje.registrar-usuario.usuario-existente")
              .replace("{username}", usuario));
    }

    if (log.isInfoEnabled()) {
      log.info(Configuracion.obtenerProperties("mensaje.registrar-usuario.registro-correcto"));
    }

    return usuarioNuevo;
  }


}