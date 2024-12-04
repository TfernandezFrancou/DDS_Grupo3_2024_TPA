package org.example.repositorios;

import com.fasterxml.classmate.AnnotationConfiguration;
import com.fasterxml.classmate.AnnotationInclusion;
import org.example.colaboraciones.Ubicacion;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.colaboraciones.contribuciones.heladeras.MovimientoViandas;
import org.example.colaboraciones.contribuciones.viandas.Vianda;
import org.example.reportes.itemsReportes.ItemReporte;
import org.example.reportes.itemsReportes.ItemReporteViandasColocadasPorHeladera;
import org.example.reportes.itemsReportes.ItemReporteViandasRetiradasPorHeladera;
import org.example.subscripcionesHeladeras.SubscripcionDesperfecto;
import org.example.subscripcionesHeladeras.SubscripcionHeladera;
import org.example.subscripcionesHeladeras.SubscripcionViandasDisponibles;
import org.example.subscripcionesHeladeras.SubscripcionViandasFaltantes;
import org.example.utils.BDUtils;
import org.hibernate.SessionFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.lang.annotation.Annotation;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RepoHeladeras {

    private static RepoHeladeras instancia = null;
    private RepoHeladeras() { }

    public static RepoHeladeras getInstancia() {
        if (instancia == null) {
            RepoHeladeras.instancia = new RepoHeladeras();
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
            heladera.getEstadoHeladeraActual();
            heladera.getHistorialEstadoHeladera().size();
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
        List<Heladera> heladeras = null;
        try{
            heladeras = em.createQuery("FROM Heladera WHERE idHeladera=:idHeladera", Heladera.class)
                    .setParameter("idHeladera", heladeraAEncontrar.getIdHeladera())
                    .getResultList();

        } catch (Exception e){
            e.printStackTrace();
            BDUtils.rollback(em);
        }finally {
            em.close();
        }

        assert heladeras != null;
        return heladeras.stream().findFirst().get();
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
                    .collect(Collectors.toList());

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

        return heladeras.stream().filter(heladera -> heladera.getNombre().equals(nombreABuscar)).findFirst();
    }

    public Optional<Heladera> buscarPorId(Integer id) {
        EntityManager em = BDUtils.getEntityManager();
        Optional<Heladera> result = null;
        try{
            result = em
                    .createQuery("from Heladera where idHeladera = :id", Heladera.class)
                    .setParameter("id", id)
                    .getResultList()
                    .stream()
                    .findFirst();

            //lazy initializations
            if(!result.isEmpty()){
                result.get().getHistorialEstadoHeladera().size();
            }
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
            result.forEach(h -> h.getHistorialEstadoHeladera().size());
            result.forEach(h -> h.getHistorialMovimientos().forEach(hm -> {hm.getViandasSacadas().size();  hm.getViandasIntroducidas().size();}));
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
        em.getTransaction().begin();
        em.persist(subscripcion);
        em.getTransaction().commit();
    }

    public void eliminarSuscripcion(int idSuscripcion) {
        EntityManager em = BDUtils.getEntityManager();
        em.getTransaction().begin();
        em.remove(em.find(SubscripcionHeladera.class, idSuscripcion));
        em.getTransaction().commit();
    }

    public List<SubscripcionHeladera> obtenerSubscripcionesPorPersona(int idHeladera, int idPersona) {
        return BDUtils
                .getEntityManager()
                .createQuery("from SubscripcionHeladera s where s.subscriptor.id = :idPersona and s.heladera.id = :idHeladera", SubscripcionHeladera.class)
                .setParameter("idPersona", idPersona)
                .setParameter("idHeladera", idHeladera)
                .getResultList();
    }

    public List<SubscripcionDesperfecto> obtenerSubscripcionesDesperfecto(int idHeladera) {
        return BDUtils
                .getEntityManager()
                .createQuery("from SubscripcionDesperfecto s where s.heladera.id = :idHeladera", SubscripcionDesperfecto.class)
                .setParameter("idHeladera", idHeladera)
                .getResultList();
    }

    public List<SubscripcionViandasFaltantes> obtenerSubscripcionesViandasFaltantes(int idHeladera) {
        return BDUtils
                .getEntityManager()
                .createQuery("from SubscripcionViandasFaltantes s where s.heladera.id = :idHeladera", SubscripcionViandasFaltantes.class)
                .setParameter("idHeladera", idHeladera)
                .getResultList();
    }

    public List<SubscripcionViandasDisponibles> obtenerSubscripcionesViandasDisponibles(int idHeladera) {
        return BDUtils
                .getEntityManager()
                .createQuery("from SubscripcionViandasFaltantes s where s.heladera.id = :idHeladera", SubscripcionViandasDisponibles.class)
                .setParameter("idHeladera", idHeladera)
                .getResultList();
    }
}