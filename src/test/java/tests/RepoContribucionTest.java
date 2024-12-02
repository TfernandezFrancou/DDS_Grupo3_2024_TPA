package tests;

import org.example.colaboraciones.Contribucion;
import org.example.colaboraciones.contribuciones.DonacionDeDinero;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.personas.PersonaHumana;
import org.example.personas.contacto.CorreoElectronico;
import org.example.personas.documentos.Documento;
import org.example.personas.documentos.TipoDocumento;
import org.example.personas.roles.Colaborador;
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
    RepoPersona repoPersona;

    @BeforeEach
    public void setUp(){
       this.repoContribucion = RepoContribucion.getInstancia();

       this.repoContribucion.clean();
       this.repoPersona = RepoPersona.getInstancia();
       this.repoPersona.clean();;
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


    @Test
    void testObtenerContribucionesPorPersona(){
        Colaborador colaborador = new Colaborador();
        colaborador.setPuntuaje(18);
        colaborador.setEstaActivo(true);

        PersonaHumana personaColaborador = new PersonaHumana(
                "Franco",
                "Callero",
                new CorreoElectronico("si123@gmail.com"),
                new Documento(TipoDocumento.DNI,"12345678"),
                colaborador);
        this.repoPersona.agregar(personaColaborador);

        DonacionDeDinero contribucion = new DonacionDeDinero(LocalDate.now(), 100000);
        contribucion.setColaborador(colaborador);
        this.repoContribucion.agregarContribucion(contribucion);

        List<Contribucion> contribuciones = this.repoContribucion.obtenerContribucionesPorPersona(personaColaborador.getIdPersona());
        Assertions.assertEquals(1, contribuciones.size());
        Assertions.assertEquals(contribucion.getIdContribucion(), contribuciones.get(0).getIdContribucion());

    }

}
