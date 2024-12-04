package org.example.repositorios;


import org.example.autenticacion.Usuario;
import org.example.config.Configuracion;
import org.example.excepciones.UserException;
import org.example.personas.documentos.Documento;
import org.example.utils.BDUtils;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RepoUsuario {
  private static RepoUsuario instancia = null;

  private RepoUsuario() {
  }

  public static RepoUsuario getInstancia() {
    if (instancia == null) {
      RepoUsuario.instancia = new RepoUsuario();
    }
    return instancia;
  }

  public void agregarUsuarios(Usuario usuarioNuevo) {
    EntityManager em = BDUtils.getEntityManager();
    BDUtils.comenzarTransaccion(em);
    em.persist(usuarioNuevo);
    em.getTransaction().commit();
  }

  public boolean existeNombreUsuarioRegistrado(String nombreUsuario) {
    EntityManager em = BDUtils.getEntityManager();

    List<Usuario> usuarios1 = em.createQuery("FROM Usuario where nombreDeUsuario=:nombreDeUsuario", Usuario.class)
            .setParameter("nombreDeUsuario",nombreUsuario).getResultList();

    return usuarios1.size() > 0;
  }

  public Usuario obtenerUsuarioPorDocumento(Documento documento) throws  UserException{
    EntityManager em = BDUtils.getEntityManager();
    try{
      List<Usuario> usuarios1 = em.createQuery(
                      "SELECT u FROM Usuario u JOIN u.documento d WHERE d.numeroDocumento = :numeroDocumento", Usuario.class)
              .setParameter("numeroDocumento", documento.getNumeroDocumento())
              .getResultList();

      if(usuarios1.isEmpty()){
        throw new UserException(Configuracion.obtenerProperties("mensaje.repositorio.Usuarios.no-encontrado-documento")
                .replace("{documento}", documento.getNumeroDocumento()));
      }

      return usuarios1.get(0);
    } catch ( Exception ex){
      throw new UserException(Configuracion.obtenerProperties("mensaje.inicio-sesion.error-documento")
              .replace("{doc}", documento.getNumeroDocumento()));
    }
  }

  public Usuario obtenerUsuarioPorNombreDeUsuario(String nombreUsuario){
    EntityManager em = BDUtils.getEntityManager();
    try{
      List<Usuario> usuarios1 = em.createQuery(
                      "SELECT u FROM Usuario u WHERE u.nombreDeUsuario=:nombreDeUsuario", Usuario.class)
              .setParameter("nombreDeUsuario", nombreUsuario)
              .getResultList();
      if(usuarios1.isEmpty()){
        throw new UserException(Configuracion.obtenerProperties("mensaje.inicio-sesion.nombre-usuario-incorrecto"));
      }
      return usuarios1.get(0);
    }catch (Exception exception){
      throw new UserException(Configuracion.obtenerProperties("mensaje.inicio-sesion.error")
              .replace("{username}", nombreUsuario));
    }
  }

  public Usuario obtenerUsuarioPorId(int idUsuario) {
    EntityManager em = BDUtils.getEntityManager();
    try {
      List<Usuario> usuarios1 = em.createQuery(
                      "SELECT u FROM Usuario u WHERE u.idUsuario=:idUsuario", Usuario.class)
              .setParameter("idUsuario", idUsuario)
              .getResultList();
      if(usuarios1.isEmpty()){
        throw new UserException("Usuario inexistente");
      }
      return usuarios1.get(0);
    } catch (Exception exception){
      throw new UserException("Usuario inexistente");
    }
  }

  public void clean(){
    EntityManager em = BDUtils.getEntityManager();
    BDUtils.comenzarTransaccion(em);
    em.createQuery("delete from Usuario").executeUpdate();
    em.getTransaction().commit();
  }

}
