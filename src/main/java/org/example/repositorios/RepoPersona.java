package org.example.repositorios;

import org.example.colaboraciones.Contribucion;
import org.example.colaboraciones.Ubicacion;
import org.example.colaboraciones.contribuciones.*;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.colaboraciones.contribuciones.viandas.Vianda;
import org.example.excepciones.PersonaInexistenteException;
import org.example.incidentes.FallaTecnica;
import org.example.personas.Persona;
import org.example.personas.PersonaHumana;
import org.example.personas.PersonaJuridica;
import org.example.personas.roles.Colaborador;
import org.example.personas.roles.PersonaEnSituacionVulnerable;
import org.example.personas.roles.Rol;
import org.example.personas.roles.Tecnico;
import org.example.recomendacion.Zona;
import org.example.reportes.itemsReportes.ItemReporte;
import org.example.reportes.itemsReportes.ItemReporteViandasDistribuidasPorColaborador;
import org.example.utils.BDUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RepoPersona {

    private static RepoPersona instancia = null;

    public static RepoPersona getInstancia() {
        if (instancia == null) {
            RepoPersona.instancia = new RepoPersona();
        }
        return instancia;
    }

    public void agregarTodas(List<Persona> personas) {
        EntityManager em = BDUtils.getEntityManager();
        BDUtils.comenzarTransaccion(em);
        for (Persona persona: personas) {
            em.persist(persona);
        }
        em.getTransaction().commit();;
    }

    public void agregar(Persona persona) {
        EntityManager em = BDUtils.getEntityManager();
        BDUtils.comenzarTransaccion(em);
        em.persist(persona);
        em.getTransaction().commit();
    }

    public void eliminar(Persona persona) {
        EntityManager em = BDUtils.getEntityManager();
        BDUtils.comenzarTransaccion(em);
        int idPersona =persona.getIdPersona();
        em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();//deshabilito el check de FKs

        PersonaHumana ph = em.find(PersonaHumana.class, idPersona);
        if(ph != null)
            em.remove(ph);

        PersonaJuridica pj = em.find(PersonaJuridica.class, idPersona);
        if(pj != null)
            em.remove(pj);

        Persona p = em.find(Persona.class, idPersona);
        if(p != null)
            em.remove(p);
        em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();//habilito el check de FKs
        em.getTransaction().commit();
    }


    public Optional<Persona> tecnicoMasCercanoAHeladera(Heladera heladeraDada){
        List<Persona> tecnicos = this.buscarPersonasConRol(Tecnico.class);
        Optional<Persona> tecnicoMasCercano = Optional.empty();
        double distanciaMinima = Double.MAX_VALUE;//para obtener al tecnico con la distancia minima
        Ubicacion ubicacionHeladera = heladeraDada.getUbicacion();

        for (Persona tecnico : tecnicos) {
            Tecnico rolTecnico = (Tecnico) tecnico.getRol();
            for (Zona zona : rolTecnico.getAreasDeCobertura()) {
                double distancia = ubicacionHeladera.calcularDistanciaA(zona.getUbicacion());
                if (distancia <= zona.getRadio() && distancia < distanciaMinima) {
                    distanciaMinima = distancia;
                    tecnicoMasCercano = Optional.of(tecnico);
                }
            }
        }

        return tecnicoMasCercano;
    }

    public void clean(){
        EntityManager em = BDUtils.getEntityManager();
        BDUtils.comenzarTransaccion(em);
        em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();//deshabilito el check de FKs
        em.createNativeQuery("DELETE FROM Tecnico").executeUpdate();
        em.createNativeQuery("DELETE FROM Colaborador").executeUpdate();
        em.createNativeQuery("DELETE FROM PersonaEnSituacionVulnerable").executeUpdate();
        em.createNativeQuery("DELETE FROM Documento").executeUpdate();
        em.createNativeQuery("DELETE FROM MedioDeContacto").executeUpdate();
        em.createNativeQuery("DELETE FROM PersonaHumana").executeUpdate();
        em.createNativeQuery("DELETE FROM PersonaJuridica").executeUpdate();
        em.createNativeQuery("DELETE FROM Persona").executeUpdate();
        em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();//habilito el check de FKs
        em.getTransaction().commit();
    }

    public Persona buscarPorId(int idPersona) {
        EntityManager em = BDUtils.getEntityManager();
        Persona persona =
                em.createQuery("SELECT p FROM Persona p WHERE p.idPersona=:id", Persona.class)
                        .setParameter("id", idPersona)
                        .getSingleResult();
        if(persona == null) {
            throw new PersonaInexistenteException("No existe la persona con id " + idPersona);
        }
        return persona;
    }

    public Persona buscarPorNombre(String nombreYApellido) {
        String[] nombreYApellidoArray = nombreYApellido.split(" ");
        String nombre = nombreYApellidoArray[0];
        String apellido = nombreYApellidoArray[1];
        EntityManager em = BDUtils.getEntityManager();
        PersonaHumana persona =
                em.createQuery("SELECT p FROM PersonaHumana p WHERE p.nombre=:nombre and p.apellido=:apellido", PersonaHumana.class)
                        .setParameter("nombre", nombre)
                        .setParameter("apellido", apellido)
                        .getSingleResult();
        if (persona == null) {
            throw new PersonaInexistenteException("No existe la persona con nombre " + nombreYApellido);
        }
        return persona;
    }

    public List<Persona> buscarPersonasConRol(Class<?> rol) {
        EntityManager em = BDUtils.getEntityManager();
        if(rol.equals(Colaborador.class)){
            return  em.createQuery("SELECT p FROM Persona p JOIN Colaborador c ON p.rol.idrol=c.idrol", Persona.class)
                    .getResultList();
        } else if(rol.equals(Tecnico.class)){
            return  em.createQuery("SELECT p FROM Persona p JOIN Tecnico t ON p.rol.idrol=t.idrol", Persona.class)
                    .getResultList();
        }else if(rol.equals(PersonaEnSituacionVulnerable.class)){
            return  em.createQuery("SELECT p FROM Persona p JOIN PersonaEnSituacionVulnerable psv ON p.rol.idrol=psv.idrol", Persona.class)
                    .getResultList();
        }else return null;
    }

    public Persona buscarPersonaAsociadaAlRol(Rol rol) {
        EntityManager em = BDUtils.getEntityManager();
        Persona persona = em.createQuery("SELECT p FROM Persona p WHERE p.rol.idrol=:idRol", Persona.class)
                .setParameter("idRol", rol.getIdrol())
                .getSingleResult();
        if(persona == null){
            throw new PersonaInexistenteException("No existe la persona que tiene el rol con id="+rol.getIdrol());
        }
        return persona;
    }

    public Colaborador getRolColaboradorById(int idRol) {
        System.out.println("buscando id rol: "+ idRol);
        EntityManager em = BDUtils.getEntityManager();
        BDUtils.comenzarTransaccion(em);
        Colaborador colaborador = em.createQuery("SELECT c FROM Colaborador c WHERE c.idrol=:idRol", Colaborador.class)
                .setParameter("idRol", idRol)
                .getSingleResult();
        BDUtils.commit(em);
        if(colaborador == null){
            BDUtils.rollback(em);
            throw new PersonaInexistenteException("No existe el rol colaborador con id="+idRol);
        }
        //fetch lazy de formas de contribucion
        System.out.println("formas De Contribucion: "+ colaborador.getFormasContribucion().size());
        return colaborador;
    }

    public List<ItemReporte> obtenerCantidadDeViandasDistribuidasPorColaborador(LocalDateTime inicioSemanaActual, LocalDateTime finSemanaActual) {
        List<Persona> colaboradores = this.buscarPersonasConRol(Colaborador.class);
        List<ItemReporte> reporte = new ArrayList<>();

        for (Persona colaborador : colaboradores) {
            Colaborador  rolColaborador = (Colaborador) colaborador.getRol();
            List<Vianda> viandasDonadasEnLaSemana = rolColaborador.cantidadDeViandasDistribuidasEnLaSemana(inicioSemanaActual, finSemanaActual);
            ItemReporteViandasDistribuidasPorColaborador item = new ItemReporteViandasDistribuidasPorColaborador();
            item.setColaborador(colaborador);
            item.setViandasDistribuidas(viandasDonadasEnLaSemana);
           reporte.add(item);
        }

        return reporte;
    }

    public List<PersonaHumana> obtenerPersonasEnSituacionVulnerable(){
        EntityManager em = BDUtils.getEntityManager();
        List<Rol> rolesDepersonasEnSituacionVulnerable =
                em.createQuery("SELECT p FROM PersonaEnSituacionVulnerable p", Rol.class)
                .getResultList();
        List<PersonaHumana> personasHumanas = new ArrayList<>();
        for (Rol rol:rolesDepersonasEnSituacionVulnerable) {
            int idRol = rol.getIdrol();
            List<Persona> persona_n=
                    em.createQuery("SELECT p FROM Persona p WHERE p.rol.idrol=:idRol", Persona.class)
                    .setParameter("idRol", idRol)
                    .getResultList();
            if(persona_n.size() == 1){
                PersonaHumana ph = em.find(PersonaHumana.class, persona_n.get(0).getIdPersona());
                if(ph != null)
                    personasHumanas.add(ph);
            }
        }

        return personasHumanas;
    }

    public void actualizarColaborador(Colaborador colaborador) {
        EntityManager em = BDUtils.getEntityManager();
        BDUtils.comenzarTransaccion(em);
        em.merge(colaborador);
        em.getTransaction().commit();
    }

    public Persona actualizarPersona(Persona personaUser) {
        EntityManager em = BDUtils.getEntityManager();
        Persona personaActualizada = em.find(Persona.class, personaUser.getIdPersona());
        try {
            BDUtils.comenzarTransaccion(em);
            personaActualizada = em.merge(personaUser);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        }
        return personaActualizada;
    }

}