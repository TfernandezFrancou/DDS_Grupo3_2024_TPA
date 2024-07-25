package tests;

import org.example.colaboraciones.TipoDePersona;
import org.example.colaboraciones.contribuciones.DonacionDeViandas;
import org.example.personas.Persona;
import org.example.personas.PersonaHumana;
import org.example.personas.roles.Colaborador;
import org.example.reportes.ItemReporteColaborador;
import org.example.reportes.ItemReporteHeladera;
import org.example.repositorios.RepoPersona;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.time.LocalDateTime;
import java.util.List;


public class RepoPersonaTests {
    private RepoPersona repoPersona;

    private Persona colaborador1;

    private Persona colaborador2;

    @BeforeEach
    public void setUp(){
        this.repoPersona  = RepoPersona.getInstancia();

        this.colaborador1 = new PersonaHumana();
        this.colaborador1.setRol(new Colaborador());

        this.colaborador2 = new PersonaHumana();
        this.colaborador2.setRol(new Colaborador());

        repoPersona.agregar(colaborador1);
        repoPersona.agregar(colaborador2);
    }

    @Test
    public void testObtenerCantidadDeViandasDistribuidasPorColaborador(){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime inicioSemana = now.with(java.time.temporal.TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY)).toLocalDate().atStartOfDay();
        LocalDateTime finSemana = now.with(java.time.temporal.TemporalAdjusters.nextOrSame(java.time.DayOfWeek.SUNDAY)).toLocalDate().atTime(23, 59, 59);

        Colaborador rolColaborador1 = (Colaborador) colaborador1.getRol();
        rolColaborador1.agregarContribucion(new DonacionDeViandas(TipoDePersona.HUMANA, inicioSemana.plusDays(2).toLocalDate(), 10)); // miercoles
        rolColaborador1.agregarContribucion(new DonacionDeViandas(TipoDePersona.HUMANA, inicioSemana.plusDays(3).toLocalDate(), 10)); // jueves
        rolColaborador1.agregarContribucion(new DonacionDeViandas(TipoDePersona.HUMANA, inicioSemana.minusDays(1).toLocalDate(), 10)); // domingo de la anterior semana

        Colaborador rolColaborador2 = (Colaborador) colaborador2.getRol();
        rolColaborador2.agregarContribucion(new DonacionDeViandas(TipoDePersona.HUMANA, inicioSemana.plusDays(4).toLocalDate(), 10)); // viernes
        rolColaborador2.agregarContribucion(new DonacionDeViandas(TipoDePersona.HUMANA, inicioSemana.toLocalDate(), 15)); // lunes
        rolColaborador2.agregarContribucion(new DonacionDeViandas(TipoDePersona.HUMANA, inicioSemana.minusDays(2).toLocalDate(), 10)); // sabado de la anterior semana

        List<ItemReporteColaborador> reporte = repoPersona.obtenerCantidadDeViandasDistribuidasPorColaborador(inicioSemana, finSemana);

        Assertions.assertEquals(2, reporte.size(), "Debe haber reportes para dos heladeras");

        for (ItemReporteColaborador item : reporte) {
            if (item.getColaborador().equals(colaborador1)) {
                Assertions.assertEquals(20, item.getCantidad(), "Colaborador 1 debe tener 20 viandas donadas en la semana");
            } else if (item.getColaborador().equals(colaborador2)) {
                Assertions.assertEquals(25, item.getCantidad(), "Colaborador 2 debe tener 25 viandas donadas en la semana");
            } else {
                Assertions.fail("Colaborador no esperado en el reporte");
            }
        }

    }
}
