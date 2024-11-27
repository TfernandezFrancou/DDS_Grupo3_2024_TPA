package tests;

import org.example.colaboraciones.Contribucion;
import org.example.colaboraciones.contribuciones.DonacionDeDinero;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.personas.PersonaHumana;
import org.example.personas.documentos.Documento;
import org.example.personas.documentos.TipoDocumento;
import org.example.repositorios.RepoContribucion;
import org.example.repositorios.RepoIncidente;
import org.example.repositorios.RepoPersona;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;

public class RepoContribucionTest {

    RepoContribucion repoContribucion;

    @BeforeEach
    public void setUp(){
       this.repoContribucion = RepoContribucion.getInstancia();

       this.repoContribucion.clean();
    }

    @Test
    void testAgregarContribucion() {
        DonacionDeDinero contribucion = new DonacionDeDinero(LocalDate.now(), 100000);
        this.repoContribucion.agregarContribucion(contribucion);

        List<Contribucion> contribuciones = this.repoContribucion.obtenerContribuciones();
        Assertions.assertEquals(1, contribuciones.size());
    }

    @Test
    void testQuitarContribucion(){
        DonacionDeDinero contribucion = new DonacionDeDinero(LocalDate.now(), 100000);
        this.repoContribucion.agregarContribucion(contribucion);

        List<Contribucion> contribucionesAntes = this.repoContribucion.obtenerContribuciones();
        Assertions.assertEquals(1, contribucionesAntes.size());

        this.repoContribucion.eliminarContribucion(contribucion);
        List<Contribucion> contribucionesDespues = this.repoContribucion.obtenerContribuciones();
        Assertions.assertEquals(0, contribucionesDespues.size());
    }


}
