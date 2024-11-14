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
    private List<Heladera> heladeras;

    private static RepoHeladeras instancia = null;
    private RepoHeladeras() {
        this.heladeras = new ArrayList<>();
    }

    public static RepoHeladeras getInstancia() {
        if (instancia == null) {
            RepoHeladeras.instancia = new RepoHeladeras();
        }
        return instancia;
    }

    public void clean(){
        EntityManager em = BDUtils.getEntityManager();
        em.getTransaction().begin();
        em.createQuery("delete from MovimientoViandas").executeUpdate();
        em.createQuery("delete from Heladera").executeUpdate();
        em.createQuery("delete from Ubicacion").executeUpdate();
        em.getTransaction().commit();
        //this.heladeras.clear();
    }

    public void agregarTodas(List<Heladera> heladeras) {
        //this.heladeras.addAll(heladeras);
        EntityManager em = BDUtils.getEntityManager();
        em.getTransaction().begin();

        for(Heladera heladera: heladeras){
            em.persist(heladera);
        }
        em.getTransaction().commit();
    }

    public void agregar(Heladera heladera) {
        EntityManager em = BDUtils.getEntityManager();
        em.getTransaction().begin();
        em.persist(heladera);
        em.getTransaction().commit();
    }

    public void actualizar(Heladera heladeraActualizada) {
        Heladera heladeraGuardada = this.buscarHeladera(heladeraActualizada);

        heladeraGuardada.setCapacidadEnViandas(heladeraActualizada.getCapacidadEnViandas());
        heladeraGuardada.setViandasEnHeladera(heladeraActualizada.getViandasEnHeladera());
        heladeraGuardada.setFechaInicioFuncionamiento(heladeraActualizada.getFechaInicioFuncionamiento());
        heladeraGuardada.setEstadoHeladeraActual(heladeraActualizada.getEstadoHeladeraActual());
        heladeraGuardada.setHistorialEstadoHeldera(heladeraActualizada.getHistorialEstadoHeldera());
        heladeraGuardada.setHistorialMovimientos(heladeraActualizada.getHistorialMovimientos());
        heladeraGuardada.setTemperaturasDeFuncionamiento(heladeraActualizada.getTemperaturasDeFuncionamiento());
    }

    public void eliminar(Heladera heladera) {
        //this.heladeras.remove(heladera);

        EntityManager em = BDUtils.getEntityManager();
        em.getTransaction().begin();
        em.remove(heladera);
        em.getTransaction().commit();
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

        List<Heladera> heladeras1 = em.createQuery("FROM Heladera WHERE nombre=:nombre", Heladera.class)
                    .setParameter("nombre", heladeraAEncontrar.getNombre()).getResultList();

        return heladeras1.stream().findFirst().get();
    }
    public List<ItemReporte> obtenerCantidadDeViandasColocadasPorHeladeraDeLaSemana(LocalDateTime inicioSemanaActual, LocalDateTime finSemanaActual) {
        return obtenerViandasPorHeladeraDeLaSemana(inicioSemanaActual, finSemanaActual, true);
    }

    public List<ItemReporte> obtenerCantidadDeViandasRetiradasPorHeladeraDeLaSemana(LocalDateTime inicioSemanaActual, LocalDateTime finSemanaActual) {
        return obtenerViandasPorHeladeraDeLaSemana(inicioSemanaActual, finSemanaActual, false);
    }

    /*private List<MovimientoViandas> getHistorialDeMovimientos(){
        EntityManager em = BDUtils.getEntityManager();
        em.createQuery("FROM ")
    }*/

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

        List<Heladera> heladeras1 = em.createQuery("FROM Heladera WHERE nombre=:nombre", Heladera.class)
                .setParameter("nombre", nombreABuscar).getResultList();

        return heladeras1.stream().filter(heladera -> heladera.getNombre().equals(nombreABuscar)).findFirst();
    }

    public Optional<Heladera> buscarPorId(Integer id) {
        return BDUtils
                .getEntityManager()
                .createQuery("from Heladera where idHeladera = :id", Heladera.class)
                .setParameter("id", id)
                .getResultList()
                .stream()
                .findFirst();
    }

    public List<Heladera> obtenerTodas() {
        return BDUtils
                .getEntityManager()
                .createQuery("from Heladera", Heladera.class)
                .getResultList();
    }
}