package org.example.repositorios;

import org.example.colaboraciones.Ubicacion;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.colaboraciones.contribuciones.heladeras.MovimientoViandas;
import org.example.colaboraciones.contribuciones.viandas.Vianda;
import org.example.reportes.items_reportes.ItemReporte;
import org.example.reportes.items_reportes.ItemReporteViandasColocadasPorHeladera;
import org.example.reportes.items_reportes.ItemReporteViandasRetiradasPorHeladera;
import org.example.subscripciones_heladeras.SubscripcionDesperfecto;
import org.example.subscripciones_heladeras.SubscripcionHeladera;
import org.example.subscripciones_heladeras.SubscripcionViandasDisponibles;
import org.example.subscripciones_heladeras.SubscripcionViandasFaltantes;
import org.example.utils.BDUtils;
import org.hibernate.Hibernate;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public class RepoHeladeras {

    private static RepoHeladeras instancia = null;
    private RepoHeladeras() { }

    private static final String FIELD_ID_HELADERA = "idHeladera";

    public static RepoHeladeras getInstancia() {
        if (instancia == null) {
            instancia = new RepoHeladeras();
        }
        return instancia;
    }

    public void clean(){

        EntityManager em = BDUtils.getEntityManager();
        try{
            BDUtils.comenzarTransaccion(em);
            em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();//deshabilito el check de FKs
            em.createNativeQuery("DELETE FROM MovimientoViandasXVianda_sacadas").executeUpdate();
            em.createNativeQuery("DELETE FROM MovimientoViandasXVianda_introducidas").executeUpdate();
            em.createNativeQuery("DELETE FROM Vianda").executeUpdate();
            em.createNativeQuery("DELETE FROM MovimientoViandas").executeUpdate();
            em.createNativeQuery("DELETE FROM Incidente").executeUpdate();
            em.createNativeQuery("DELETE FROM Heladera").executeUpdate();
            em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();//habilito el check de FKs
            em.getTransaction().commit();
        }catch (Exception e){
            e.printStackTrace();
            BDUtils.rollback(em);
        }finally {
            em.close();
        }


    }

    public void agregarTodas(List<Heladera> heladeras) {
        EntityManager em = BDUtils.getEntityManager();
        try{
            BDUtils.comenzarTransaccion(em);
            for (Heladera heladera : heladeras) {
                em.persist(heladera);
            }
            em.getTransaction().commit();

        } catch (Exception e){
            e.printStackTrace();
            BDUtils.rollback(em);
        }finally {
            em.close();
        }
    }

    public void agregar(Heladera heladera) {
        EntityManager em = BDUtils.getEntityManager();
        try{
            BDUtils.comenzarTransaccion(em);
            em.persist(heladera);
            em.getTransaction().commit();
            //lazy initializations
            Hibernate.initialize(heladera.getEstadoHeladeraActual());
            Hibernate.initialize(heladera.getHistorialEstadoHeladera());
        } catch (Exception e){
            e.printStackTrace();
            BDUtils.rollback(em);
        } finally {
            em.close();
        }
    }

    public void actualizar(Heladera heladeraActualizada) {
        Heladera heladeraGuardada = this.buscarHeladera(heladeraActualizada);

        heladeraGuardada.setCapacidadEnViandas(heladeraActualizada.getCapacidadEnViandas());
        heladeraGuardada.setViandasEnHeladera(heladeraActualizada.getViandasEnHeladera());
        heladeraGuardada.setFechaInicioFuncionamiento(heladeraActualizada.getFechaInicioFuncionamiento());
        heladeraGuardada.setEstadoHeladeraActual(heladeraActualizada.getEstadoHeladeraActual());
        heladeraGuardada.setHistorialEstadoHeladera(heladeraActualizada.getHistorialEstadoHeladera());
        heladeraGuardada.setHistorialMovimientos(heladeraActualizada.getHistorialMovimientos());
        heladeraGuardada.setTemperaturasDeFuncionamiento(heladeraActualizada.getTemperaturasDeFuncionamiento());

        EntityManager em = BDUtils.getEntityManager();
        try{
            BDUtils.comenzarTransaccion(em);
            em.merge(heladeraGuardada);
            em.getTransaction().commit();
        } catch (Exception e){
            e.printStackTrace();
            BDUtils.rollback(em);
        } finally {
            em.close();
        }
    }

    public void eliminar(Heladera heladera) {
        EntityManager em = BDUtils.getEntityManager();
        try{
            BDUtils.comenzarTransaccion(em);
            em.remove(heladera);
            em.getTransaction().commit();
        }catch (Exception e){
            e.printStackTrace();
            BDUtils.rollback(em);
        }finally {
            em.close();
        }
    }

    public List<Heladera> buscarHeladerasActivas() {
        return this.obtenerTodas().stream().filter(Heladera::estaActiva).toList();
    }

    public List<Heladera> buscarHeladerasCercanasA(Heladera heladeraDada, double distanciaMaximaKm) {

        List<Heladera> heladerasCercanas = new ArrayList<>();
        Ubicacion ubicacionDada = heladeraDada.getUbicacion();

        for (Heladera heladera : this.obtenerTodas()) {
            if (!heladera.equals(heladeraDada)) {
                double distancia = ubicacionDada.calcularDistanciaA(heladera.getUbicacion());
                if (distancia <= distanciaMaximaKm) {
                    heladerasCercanas.add(heladera);
                }
            }
        }

        Optional<Heladera> heladeraOp = heladerasCercanas.stream()
                .filter(heladera ->
                        heladera.getIdHeladera() == heladeraDada.getIdHeladera())
                .findFirst();
        heladeraOp.ifPresent(heladerasCercanas::remove);

        return heladerasCercanas;
    }



    public Heladera buscarHeladera(Heladera heladeraAEncontrar){

        EntityManager em = BDUtils.getEntityManager();
        Heladera heladera = null;
        try{
            heladera = em.createQuery("FROM Heladera WHERE idHeladera=:idHeladera", Heladera.class)
                    .setParameter(FIELD_ID_HELADERA, heladeraAEncontrar.getIdHeladera())
                    .getSingleResult();

        } catch (Exception e){
            e.printStackTrace();
            BDUtils.rollback(em);
        }finally {
            em.close();
        }

        return heladera;
    }
    public List<ItemReporte> obtenerCantidadDeViandasColocadasPorHeladeraDeLaSemana(LocalDateTime inicioSemanaActual, LocalDateTime finSemanaActual) {
        return obtenerViandasPorHeladeraDeLaSemana(inicioSemanaActual, finSemanaActual, true);
    }

    public List<ItemReporte> obtenerCantidadDeViandasRetiradasPorHeladeraDeLaSemana(LocalDateTime inicioSemanaActual, LocalDateTime finSemanaActual) {
        return obtenerViandasPorHeladeraDeLaSemana(inicioSemanaActual, finSemanaActual, false);
    }

    private List<ItemReporte> obtenerViandasPorHeladeraDeLaSemana(LocalDateTime inicioSemanaActual, LocalDateTime finSemanaActual, boolean viandasColocadas) {
        List<ItemReporte> reporte = new ArrayList<>();

        List<Heladera> heladeras1 = this.obtenerTodas();

        for (Heladera heladera : heladeras1) {
            List<MovimientoViandas> movimientosDeLaSemana = heladera.getHistorialMovimientos().stream()
                    .filter(movimiento ->
                            (movimiento.getFechaMovimiento().isAfter(inicioSemanaActual) &&
                                    movimiento.getFechaMovimiento().isBefore(finSemanaActual)) ||
                                    (movimiento.getFechaMovimiento().equals(inicioSemanaActual) ||
                                            movimiento.getFechaMovimiento().equals(finSemanaActual)
                                    )
                    ).toList();

            Function<MovimientoViandas, Stream<Vianda>> extractorFuncion;
            if(viandasColocadas){//si necesito viandas colocadas
                extractorFuncion = (movimientoViandas -> movimientoViandas.getViandasIntroducidas().stream());
            } else {//si necesito viandas retiradas
                extractorFuncion = (movimientoViandas -> movimientoViandas.getViandasSacadas().stream());
            }

            List<Vianda> viandas =  movimientosDeLaSemana.stream()
                    .flatMap(extractorFuncion)
                    .toList();

            if(viandasColocadas){//si necesito viandas colocadas
                ItemReporteViandasColocadasPorHeladera item = new ItemReporteViandasColocadasPorHeladera();
                item.setHeladera(heladera);
                item.setViandasColocadas(viandas);
                reporte.add(item);
            } else {//si necesito viandas retiradas
                ItemReporteViandasRetiradasPorHeladera item = new ItemReporteViandasRetiradasPorHeladera();
                item.setHeladera(heladera);
                item.setViandasRetiradas(viandas);
                reporte.add(item);
            }
        }

        return reporte;
    }

    public Optional<Heladera> buscarHeladeraPorNombre(String nombreABuscar){
        EntityManager em = BDUtils.getEntityManager();
        List<Heladera> heladeras = null;
        try{
            heladeras = em.createQuery("FROM Heladera WHERE nombre=:nombre", Heladera.class)
                    .setParameter("nombre", nombreABuscar).getResultList();

        } catch (Exception e){
            e.printStackTrace();
            BDUtils.rollback(em);
        } finally {
            em.close();
        }

        assert heladeras != null;
        return heladeras.stream()
                .filter(heladera -> heladera.getNombre().equals(nombreABuscar))
                .findFirst();
    }

    public Optional<Heladera> buscarPorId(Integer id) {
        EntityManager em = BDUtils.getEntityManager();
        Optional<Heladera> result = Optional.empty();
        try{
            result = em
                    .createQuery("from Heladera where idHeladera = :id", Heladera.class)
                    .setParameter("id", id)
                    .getResultList()
                    .stream()
                    .findFirst();

            //lazy initializations
            result.ifPresent(heladera -> Hibernate.initialize(heladera.getHistorialEstadoHeladera()));
        }catch (Exception e){
            e.printStackTrace();
            BDUtils.rollback(em);
        } finally {
            em.close();
        }
        return result;
    }

    public List<Heladera> obtenerTodas() {
        EntityManager em = BDUtils.getEntityManager();
        List<Heladera> result = null;
        try{
            result = em
                    .createQuery("SELECT h from Heladera h", Heladera.class)
                    .getResultList();

            //lazy initializations
            result.forEach(h -> Hibernate.initialize(h.getHistorialEstadoHeladera()));
            result.forEach(h -> h.getHistorialMovimientos().forEach(hm -> {
                Hibernate.initialize(hm.getViandasSacadas());
                Hibernate.initialize(hm.getViandasIntroducidas());
            }));
        }catch (Exception e){
            e.printStackTrace();
            BDUtils.rollback(em);
        } finally {
            em.close();
        }
        return result;
    }

    public void agregarSubscripcion(SubscripcionHeladera subscripcion) {
        EntityManager em = BDUtils.getEntityManager();
        try{
            BDUtils.comenzarTransaccion(em);
            em.persist(subscripcion);
            em.getTransaction().commit();

        }catch (Exception e){
            e.printStackTrace();
            BDUtils.rollback(em);
            throw  e;
        }finally {
            em.close();
        }
    }

    public void eliminarSuscripcion(int idSuscripcion) {
        EntityManager em = BDUtils.getEntityManager();
        try{
            BDUtils.comenzarTransaccion(em);
            em.remove(em.find(SubscripcionHeladera.class, idSuscripcion));
            em.getTransaction().commit();
        } catch (Exception e){
            e.printStackTrace();
            BDUtils.rollback(em);
            throw e;
        } finally {
            em.close();
        }
    }

    public List<SubscripcionHeladera> obtenerSubscripcionesPorPersona(int idHeladera, int idPersona) {
        EntityManager em = BDUtils.getEntityManager();
        List<SubscripcionHeladera> subs = null;
        try{
            subs= em.createQuery("from SubscripcionHeladera s where s.subscriptor.id = :idPersona and s.heladera.id = :idHeladera", SubscripcionHeladera.class)
                    .setParameter("idPersona", idPersona)
                    .setParameter(FIELD_ID_HELADERA, idHeladera)
                    .getResultList();
        } catch (Exception e){
            e.printStackTrace();
            throw  e;
        } finally {
            em.close();
        }

        return subs;
    }

    public List<SubscripcionDesperfecto> obtenerSubscripcionesDesperfecto(int idHeladera) {
        EntityManager em = BDUtils.getEntityManager();
        List<SubscripcionDesperfecto> subs = null;
        try{
            subs= em.createQuery("from SubscripcionDesperfecto s where s.heladera.id = :idHeladera", SubscripcionDesperfecto.class)
                    .setParameter(FIELD_ID_HELADERA, idHeladera)
                    .getResultList();
        } catch (Exception e){
            e.printStackTrace();
            throw  e;
        } finally {
            em.close();
        }

        return subs;
    }

    public List<SubscripcionViandasFaltantes> obtenerSubscripcionesViandasFaltantes(int idHeladera) {
        EntityManager em = BDUtils.getEntityManager();
        List<SubscripcionViandasFaltantes> subs = null;
        try{
            subs= em.createQuery("from SubscripcionViandasFaltantes s where s.heladera.id = :idHeladera", SubscripcionViandasFaltantes.class)
                    .setParameter(FIELD_ID_HELADERA, idHeladera)
                    .getResultList();
        } catch (Exception e){
            e.printStackTrace();
            throw  e;
        } finally {
            em.close();
        }

        return subs;
    }

    public List<SubscripcionViandasDisponibles> obtenerSubscripcionesViandasDisponibles(int idHeladera) {

        EntityManager em = BDUtils.getEntityManager();
        List<SubscripcionViandasDisponibles> subs = null;
        try{
            subs= em.createQuery("from SubscripcionViandasDisponibles s where s.heladera.idHeladera = :idHeladera", SubscripcionViandasDisponibles.class)
                    .setParameter(FIELD_ID_HELADERA, idHeladera)
                    .getResultList();

        } catch (Exception e){
            e.printStackTrace();
            throw  e;
        } finally {
            em.close();
        }

        return subs;
    }
}