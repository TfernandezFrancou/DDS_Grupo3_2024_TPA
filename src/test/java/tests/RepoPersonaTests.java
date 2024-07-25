package tests;

import org.example.colaboraciones.TipoDePersona;
import org.example.colaboraciones.Ubicacion;
import org.example.colaboraciones.contribuciones.DonacionDeViandas;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.personas.Persona;
import org.example.personas.PersonaHumana;
import org.example.personas.roles.Colaborador;
import org.example.personas.roles.Tecnico;
import org.example.recomendacion.Zona;
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
        repoPersona.clean();

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

    @Test
    public void testTecnicoMasCercanoAHeladera(){
        repoPersona.clean();//limpio el repo

        //creo los tecnicos
        Persona tecnico1 = new PersonaHumana();
        Tecnico rolTecnico1 = new Tecnico();
        Zona zona1 = new Zona();
        zona1.setUbicacion(new Ubicacion(-34.609722F, -58.382592F));// Cerca de Buenos Aires
        zona1.setRadio(10); // dentro de la zona
        rolTecnico1.agregarAreaDeCovertura(zona1);
        tecnico1.setRol(rolTecnico1);

        Persona tecnico2 = new PersonaHumana();
        Tecnico rolTecnico2 = new Tecnico();
        Zona zona2 = new Zona();
        zona2.setUbicacion(new Ubicacion(-34.705722F, -58.501592F)); // Más lejos de Buenos Aires
        zona2.setRadio(20);//dentro de la zona pero a mayor distancia que el primer tecnico
        rolTecnico2.agregarAreaDeCovertura(zona2);
        tecnico2.setRol(rolTecnico2);

        //heladera central
        Heladera heladeraEnBuenosAires = new Heladera(); // Ejemplo en Buenos Aires
        heladeraEnBuenosAires.setUbicacion(new Ubicacion(-34.603722F, -58.381592F));

        // los agrego al repo
        repoPersona.agregarTodas(List.of(tecnico1, tecnico2));

        //llamo al metodo para probarlo
        Persona tecnicoMasCercano = repoPersona.tecnicoMasCercanoAHeladera(heladeraEnBuenosAires);

       Assertions.assertEquals(tecnico1, tecnicoMasCercano);
    }
    @Test
    public void testSiNingunTecnicoCubreLaZonaDevuelveNull(){
        repoPersona.clean();//limpio el repo

        //creo los tecnicos
        Persona tecnico1 = new PersonaHumana();
        Tecnico rolTecnico1 = new Tecnico();
        Zona zona1 = new Zona();
        zona1.setUbicacion(new Ubicacion(-34.603722F, -60.381592F));// Lejos de Buenos Aires
        zona1.setRadio(10);
        rolTecnico1.agregarAreaDeCovertura(zona1);
        tecnico1.setRol(rolTecnico1);

        Persona tecnico2 = new PersonaHumana();
        Tecnico rolTecnico2 = new Tecnico();
        Zona zona2 = new Zona();
        zona2.setUbicacion(new Ubicacion(-34.705722F, -58.501592F)); // Más lejos de Buenos Aires
        zona2.setRadio(10);
        rolTecnico2.agregarAreaDeCovertura(zona2);
        tecnico2.setRol(rolTecnico2);

        //heladera central
        Heladera heladeraEnBuenosAires = new Heladera(); // Ejemplo en Buenos Aires
        heladeraEnBuenosAires.setUbicacion(new Ubicacion(-34.603722F, -58.381592F));

        // los agrego al repo
        repoPersona.agregarTodas(List.of(tecnico1, tecnico2));

        //llamo al metodo para probarlo
        Persona tecnicoMasCercano = repoPersona.tecnicoMasCercanoAHeladera(heladeraEnBuenosAires);

        Assertions.assertNull(tecnicoMasCercano);
    }

}
