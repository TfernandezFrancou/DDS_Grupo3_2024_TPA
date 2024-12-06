package tests;

import org.example.colaboraciones.Ubicacion;
import org.example.colaboraciones.contribuciones.DonacionDeViandas;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.colaboraciones.contribuciones.viandas.Vianda;
import org.example.personas.Persona;
import org.example.personas.PersonaHumana;
import org.example.personas.roles.Colaborador;
import org.example.personas.roles.PersonaEnSituacionVulnerable;
import org.example.personas.roles.Tecnico;
import org.example.recomendacion.Zona;
import org.example.reportes.items_reportes.ItemReporte;
import org.example.reportes.items_reportes.ItemReporteViandasDistribuidasPorColaborador;
import org.example.repositorios.RepoHeladeras;
import org.example.repositorios.RepoPersona;

import org.example.repositorios.RepoUbicacion;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public class RepoPersonaTests {
    private RepoPersona repoPersona;

    private RepoUbicacion repoUbicacion;

    private Persona colaborador1;

    private Persona colaborador2;

    @BeforeEach
    public void setUp(){
        this.repoPersona  = RepoPersona.getInstancia();
        repoPersona.clean();
        this.repoUbicacion = RepoUbicacion.getInstancia();
        repoUbicacion.clean();

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
        Heladera heladeraRandom = new Heladera();

        RepoHeladeras.getInstancia().agregar(heladeraRandom);

        rolColaborador1.agregarContribucion(new DonacionDeViandas(heladeraRandom, rolColaborador1, List.of(new Vianda(), new Vianda(), new Vianda()), inicioSemana.plusDays(2).toLocalDate())); // miercoles
        rolColaborador1.agregarContribucion(new DonacionDeViandas(heladeraRandom, rolColaborador1, List.of(new Vianda(), new Vianda()),inicioSemana.plusDays(3).toLocalDate())); // jueves
        rolColaborador1.agregarContribucion(new DonacionDeViandas(heladeraRandom, rolColaborador1, List.of(new Vianda(), new Vianda()), inicioSemana.minusDays(1).toLocalDate())); // domingo de la anterior semana

        //los migrados los ignoro ya que no hay forma de saber que vianda se dono
        rolColaborador1.agregarContribucion(new DonacionDeViandas(inicioSemana.plusDays(4).toLocalDate(), 10)); // Viernes

        Colaborador rolColaborador2 = (Colaborador) colaborador2.getRol();
        rolColaborador2.agregarContribucion(new DonacionDeViandas(heladeraRandom, rolColaborador2, List.of(new Vianda(), new Vianda(), new Vianda()), inicioSemana.plusDays(4).toLocalDate())); // viernes
        rolColaborador2.agregarContribucion(new DonacionDeViandas(heladeraRandom, rolColaborador2, List.of(new Vianda(), new Vianda(), new Vianda(), new Vianda()), inicioSemana.toLocalDate())); // lunes
        rolColaborador2.agregarContribucion(new DonacionDeViandas(heladeraRandom, rolColaborador2, List.of(new Vianda()), inicioSemana.minusDays(2).toLocalDate())); // sabado de la anterior semana

        List<ItemReporte> reporte = repoPersona.obtenerCantidadDeViandasDistribuidasPorColaborador(inicioSemana, finSemana);

        Assertions.assertEquals(2, reporte.size(), "Debe haber reportes para dos heladeras");

        for (ItemReporte item : reporte) {
            ItemReporteViandasDistribuidasPorColaborador itemReporteViandasDistribuidasPorColaborador = (ItemReporteViandasDistribuidasPorColaborador) item;
            if (itemReporteViandasDistribuidasPorColaborador.getColaborador().getIdPersona() == (colaborador1.getIdPersona())) {
                Assertions.assertEquals(5, itemReporteViandasDistribuidasPorColaborador.getViandasDistribuidas().size(), "Colaborador 1 debe tener 5 viandas donadas en la semana");
            } else if (itemReporteViandasDistribuidasPorColaborador.getColaborador().getIdPersona() == (colaborador2.getIdPersona())) {
                Assertions.assertEquals(7, itemReporteViandasDistribuidasPorColaborador.getViandasDistribuidas().size(), "Colaborador 2 debe tener 7 viandas donadas en la semana");
            } else {
                Assertions.fail("Colaborador no esperado en el reporte");
            }
        }


    }

    @Test
    public void testTecnicoMasCercanoAHeladera(){
        repoPersona.clean();//limpio el repo
        Ubicacion ubicacion1 = new Ubicacion(-34.609722F, -58.382592F);
        Ubicacion ubicacion2 = new Ubicacion(-34.705722F, -58.501592F);
        repoUbicacion.agregarTodas(List.of(ubicacion1, ubicacion2));

        //creo los tecnicos
        Persona tecnico1 = new PersonaHumana();
        Tecnico rolTecnico1 = new Tecnico();
        Zona zona1 = new Zona();
        zona1.setUbicacion(ubicacion1);// Cerca de Buenos Aires
        zona1.setRadio(10); // dentro de la zona
        rolTecnico1.agregarAreaDeCovertura(zona1);
        tecnico1.setRol(rolTecnico1);

        Persona tecnico2 = new PersonaHumana();
        Tecnico rolTecnico2 = new Tecnico();
        Zona zona2 = new Zona();
        zona2.setUbicacion(ubicacion2); // Más lejos de Buenos Aires
        zona2.setRadio(20);//dentro de la zona pero a mayor distancia que el primer tecnico
        rolTecnico2.agregarAreaDeCovertura(zona2);
        tecnico2.setRol(rolTecnico2);

        //heladera central
        Heladera heladeraEnBuenosAires = new Heladera(); // Ejemplo en Buenos Aires
        Ubicacion ubicacionEnBuenosAires = new Ubicacion(-34.603722F, -58.381592F);
        repoUbicacion.agregar(ubicacionEnBuenosAires);
        heladeraEnBuenosAires.setUbicacion(ubicacionEnBuenosAires);

        // los agrego al repo
        repoPersona.agregarTodas(List.of(tecnico1, tecnico2));

        //llamo al metodo para probarlo
        Optional<Persona> tecnicoMasCercanoOp = repoPersona.tecnicoMasCercanoAHeladera(heladeraEnBuenosAires);

       Assertions.assertEquals(tecnico1.getIdPersona(), tecnicoMasCercanoOp.get().getIdPersona());
    }
    @Test
    public void testSiNingunTecnicoCubreLaZonaDevuelveNull(){
        repoPersona.clean();//limpio el repo
        Ubicacion ubicacion1 = new Ubicacion(-34.603722F, -60.381592F);
        Ubicacion ubicacion2 = new Ubicacion(-34.705722F, -58.501592F);
        repoUbicacion.agregarTodas(List.of(ubicacion1, ubicacion2));

        //creo los tecnicos
        Persona tecnico1 = new PersonaHumana();
        Tecnico rolTecnico1 = new Tecnico();
        Zona zona1 = new Zona();
        zona1.setUbicacion(ubicacion1);// Lejos de Buenos Aires
        zona1.setRadio(10);
        rolTecnico1.agregarAreaDeCovertura(zona1);
        tecnico1.setRol(rolTecnico1);

        Persona tecnico2 = new PersonaHumana();
        Tecnico rolTecnico2 = new Tecnico();
        Zona zona2 = new Zona();
        zona2.setUbicacion(ubicacion2); // Más lejos de Buenos Aires
        zona2.setRadio(10);
        rolTecnico2.agregarAreaDeCovertura(zona2);
        tecnico2.setRol(rolTecnico2);

        //heladera central
        Heladera heladeraEnBuenosAires = new Heladera(); // Ejemplo en Buenos Aires
        Ubicacion ubicacionEnBuenosAires = new Ubicacion(-34.603722F, -58.381592F);
        repoUbicacion.agregar(ubicacionEnBuenosAires);
        heladeraEnBuenosAires.setUbicacion(ubicacionEnBuenosAires);

        // los agrego al repo
        repoPersona.agregarTodas(List.of(tecnico1, tecnico2));

        //llamo al metodo para probarlo
        Optional<Persona> tecnicoMasCercanoOp = repoPersona.tecnicoMasCercanoAHeladera(heladeraEnBuenosAires);

        Assertions.assertTrue(tecnicoMasCercanoOp.isEmpty());
    }


    @Test
    public void testObtenerPersonasEnSituacionVulnerable(){
        repoPersona.clean();

        PersonaHumana persona1 = new PersonaHumana();
        persona1.setNombre("Carlitos");
        persona1.setApellido("1.0");

        PersonaEnSituacionVulnerable rolPersonaEnSituacionVulnerable = new PersonaEnSituacionVulnerable();
        rolPersonaEnSituacionVulnerable.setFechaNac(LocalDate.now());

        persona1.setRol(rolPersonaEnSituacionVulnerable);

        PersonaHumana persona2 = new PersonaHumana();
        persona1.setNombre("Carlitos");
        persona1.setApellido("2.0");

        PersonaEnSituacionVulnerable rolPersonaEnSituacionVulnerable2 = new PersonaEnSituacionVulnerable();
        rolPersonaEnSituacionVulnerable2.setFechaNac(LocalDate.now());

        persona2.setRol(rolPersonaEnSituacionVulnerable2);

        repoPersona.agregar(persona1);
        repoPersona.agregar(persona2);

        List<PersonaHumana> personas = repoPersona.obtenerPersonasEnSituacionVulnerable();

        Assertions.assertEquals(2, personas.size());
        Assertions.assertTrue(personas.stream().anyMatch(personaHumana -> personaHumana.getIdPersona() == persona1.getIdPersona()));
        Assertions.assertTrue(personas.stream().anyMatch(personaHumana -> personaHumana.getIdPersona() == persona2.getIdPersona()));
    }

    @Test
    public void testBuscarPersonasConRol(){
        repoPersona.clean();
        PersonaHumana p = new PersonaHumana();
        p.setNombre("Franco");
        p.setApellido("Callero");
        Colaborador rol = new Colaborador();
        rol.setPuntuaje(100);
        p.setRol(rol);
        repoPersona.agregar(p);
        List<Persona> personas = repoPersona.buscarPersonasConRol(Colaborador.class);
        Assertions.assertEquals(1, personas.size());
    }

}
