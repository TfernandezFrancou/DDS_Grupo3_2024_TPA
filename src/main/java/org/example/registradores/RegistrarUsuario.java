package org.example.registradores;

import org.example.Authenticacion.Registrados;
import org.example.Authenticacion.Usuario;
import org.example.excepciones.EmailYaRegistrado;
import org.example.validaciones.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class RegistrarUsuario {
  private Registrados registrados;

  private List<ValidacionContrasenia> validaciones = Arrays.asList(new CaracterRepetido(), new LongitudMinimaContrasenia(), new ValidacionTopPeoresContrasenia(), new FormatoContrasenia());

  public RegistrarUsuario() {

  }

  public RegistrarUsuario(Registrados registrados) {
    this.registrados = registrados;
  }

  public void registrarUsuario(String usuario, String email, String contrasenia) throws EmailYaRegistrado {
    this.validarContrasenia(usuario, contrasenia); // validamos si cumple con todas las validaciones de contrasenia
    Usuario usuarioNuevo = new Usuario(usuario, contrasenia, email, LocalDateTime.now()); // Solo si pasa la validación instanciamos el usuario
    //Le sumo 90 días a la fecha que se registró el usuario
    usuarioNuevo.setFechaExpiracionContrasenia(LocalDateTime.now().plusDays(90));

    this.registrados.agregarUsuarios(usuarioNuevo); // guardamos al usuario en una lista a modo de ejemplo
    System.out.println("Usuario registrado correctamente.");
  }

  private void validarContrasenia(String nombre, String contrasenia) {
    this.validaciones.forEach(validacion -> validacion.validate(nombre, contrasenia));
  }
}