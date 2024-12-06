package org.example.repositorios;


import org.example.autenticacion.Usuario;
import org.example.config.Configuracion;
import org.example.excepciones.UserException;
import org.example.personas.documentos.Documento;
import org.example.utils.BDUtils;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

public class RepoUsuario {
  private static RepoUsuario instancia = null;

  private RepoUsuario() {}

  public static RepoUsuario getInstancia() {
    if (instancia == null) {
      instancia = new RepoUsuario();
    }
    return instancia;
  }

  public void agregarUsuarios(Usuario usuarioNuevo) {
    EntityManager em = BDUtils.getEntityManager();
    try{
      BDUtils.comenzarTransaccion(em);
      em.persist(usuarioNuevo);
      em.getTransaction().commit();
    } catch (Exception e){
      e.printStackTrace();
      BDUtils.rollback(em);
      throw e;
    } finally {
      em.close();
    }
  }

  public boolean existeNombreUsuarioRegistrado(String nombreUsuario) {
    EntityManager em = BDUtils.getEntityManager();
    List<Usuario> usuarios1 = new ArrayList<>();
    try{
      usuarios1 = em.createQuery("FROM Usuario where nombreDeUsuario=:nombreDeUsuario", Usuario.class)
              .setParameter("nombreDeUsuario",nombreUsuario).getResultList();
    } finally {
      em.close();
    }

    return !usuarios1.isEmpty();
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
    } finally {
      em.close();
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
    } finally {
      em.close();
    }
  }

  public Usuario obtenerUsuarioPorId(int idUsuario) {
    EntityManager em = BDUtils.getEntityManager();
    try {
      Usuario usuario = em.createQuery(
                      "SELECT u FROM Usuario u WHERE u.idUsuario=:idUsuario", Usuario.class)
              .setParameter("idUsuario", idUsuario)
              .getSingleResult();
      if(usuario == null){
        throw new UserException("Usuario inexistente");
      }
      return usuario;
    } catch (Exception exception){
      throw new UserException("Usuario inexistente");
    } finally {
      em.close();
    }
  }

  public void clean(){
    EntityManager em = BDUtils.getEntityManager();
    try{
      BDUtils.comenzarTransaccion(em);
      em.createQuery("delete from Usuario").executeUpdate();
      em.getTransaction().commit();
    } catch (Exception e){
      e.printStackTrace();
      BDUtils.rollback(em);
      throw e;
    } finally {
      em.close();
    }
  }

}
