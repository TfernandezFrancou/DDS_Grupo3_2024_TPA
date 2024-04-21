package org.example.Authenticacion;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Registrados {
  private static Registrados instancia = null;
  private List<Usuario> usuarios;

  private Registrados() {
    this.usuarios = new ArrayList<>();
  }

  public static Registrados getInstancia() {
    if (instancia == null) {
      Registrados.instancia = new Registrados();
    }
    return instancia;
  }


  public void agregarUsuarios(Usuario usuarioNuevo) {
    instancia.usuarios.add(usuarioNuevo);
  }

  public boolean existeNombreUsuarioRegistrado(String nombreUsuario) {
    Optional<Usuario> usuario = instancia.usuarios.stream().filter(x -> x.getNombre().equals(nombreUsuario)).findFirst();
    return usuario.isPresent();
  }

}
