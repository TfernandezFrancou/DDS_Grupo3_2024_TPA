package org.example.autenticacion;

import org.example.excepciones.EmailYaRegistrado;
import org.example.excepciones.PasswordException;
import org.example.validaciones.*;

import java.time.LocalDateTime;

public class RegistrarUsuario {
  private Registrados registrados;


  public RegistrarUsuario() {

  }

  public RegistrarUsuario(Registrados registrados) {
    this.registrados = registrados;
  }

  public void registrarUsuario(String usuario, String email, String contrasenia) throws EmailYaRegistrado, PasswordException {
    VerificadorContrasenia.getInstancia().validarContrasenia(contrasenia); // validamos si cumple con todas las validaciones de contrasenia
    Usuario usuarioNuevo = new Usuario(usuario, contrasenia, email, LocalDateTime.now()); // Solo si pasa la validación instanciamos el usuario
    //Le sumo 90 días a la fecha que se registró el usuario
    usuarioNuevo.setFechaExpiracionContrasenia(LocalDateTime.now().plusDays(90));

    this.registrados.agregarUsuarios(usuarioNuevo); // guardamos al usuario en una lista a modo de ejemplo
    System.out.println("Usuario registrado correctamente.");
  }


}