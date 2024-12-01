package tests;

import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.colaboraciones.contribuciones.heladeras.VisitaHeladera;
import org.example.incidentes.Alerta;
import org.example.repositorios.RepoHeladeras;
import org.example.repositorios.RepoIncidente;
import org.example.repositorios.RepositorioVisitasTecnicos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.time.LocalDate;

public class RepoVisitasTecnicosTest {

    private RepositorioVisitasTecnicos repoVisitasTecnicos;
    private RepoIncidente repoIncidente;
    private RepoHeladeras repoHeladeras;
    private Heladera heladera;
    private Alerta alerta;
    private VisitaHeladera visita1;

    @BeforeEach
    public void setUp(){
        this.repoVisitasTecnicos = RepositorioVisitasTecnicos.getInstancia();
        repoVisitasTecnicos.clean();
        this.repoIncidente = RepoIncidente.getInstancia();
        repoIncidente.clean();
        this.repoHeladeras = RepoHeladeras.getInstancia();
        repoHeladeras.clean();

        this.heladera = new Heladera();
        this.repoHeladeras.agregar(heladera);

        this.alerta = new Alerta();
        this.alerta.setHeladera(heladera);
        this.repoIncidente.agregarAlerta(alerta);

        this.visita1 = new VisitaHeladera(LocalDate.now(), "Trabajo realizado", "foto", true, alerta);
        repoVisitasTecnicos.agregarVisita(visita1);
    }

    @Test
    public void testAgregarVisita(){
        VisitaHeladera visita2 = new VisitaHeladera(LocalDate.now(), "Trabajo realizado", "foto", true, alerta);
        repoVisitasTecnicos.agregarVisita(visita2);

        Assertions.assertEquals(2, repoVisitasTecnicos.getVisitas().size());
    }

    @Test
    public void testQuitarVisita(){
        repoVisitasTecnicos.quitarVisita(visita1);

        Assertions.assertEquals(0, repoVisitasTecnicos.getVisitas().size());
    }

}
