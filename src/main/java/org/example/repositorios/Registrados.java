package org.example.repositorios;


import org.example.autenticacion.Usuario;
import org.example.config.Configuracion;
import org.example.excepciones.UserException;
import org.example.personas.documentos.Documento;

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
    Optional<Usuario> usuario = instancia.usuarios.stream().filter(x -> x.getNombreDeUsuario().equals(nombreUsuario)).findFirst();
    return usuario.isPresent();
  }

  public Usuario obtenerUsuarioPorDocumento(Documento documento) throws  UserException{
    Optional<Usuario> usuario = instancia.usuarios.stream().filter(x -> x.getDocumento().getNumeroDocumento().equals(documento.getNumeroDocumento()) && x.getDocumento().getTipoDocumento().equals(documento.getTipoDocumento())).findFirst();

    if(usuario.isEmpty()){
      throw new UserException(Configuracion.obtenerProperties("mensaje.repositorio.Usuarios.no-encontrado"));
    }

    return usuario.get();
  }

  public Usuario obtenerUsuarioPorNombreDeUsuarioYContrasenia(String nombreUsuario, String contrasenia){
    Optional<Usuario> usuario = instancia.usuarios.stream()
              .filter(x -> x.getNombreDeUsuario().equals(nombreUsuario))
              .filter(x -> x.getContrasenia().equals(contrasenia))
              .findFirst();

    if(usuario.isEmpty()){
      throw new UserException(Configuracion.obtenerProperties("mensaje.repositorio.Usuarios.no-encontrado"));
    }

    return usuario.get();
  }

}
