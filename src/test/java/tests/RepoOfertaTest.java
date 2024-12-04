package tests;

import org.example.colaboraciones.contribuciones.ofertas.Oferta;
import org.example.repositorios.RepoOfertas;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class RepoOfertaTest {
    private RepoOfertas repoOfertas;

    private Oferta oferta1;
    private Oferta oferta2;

    @BeforeEach
    public void setUp() {
        repoOfertas = RepoOfertas.getInstancia();
        repoOfertas.limpiar(); // Aseguramos que el repositorio esté limpio antes de cada test

        // Creamos ofertas para usar en los tests
        oferta1 = new Oferta("Producto 1", 100, "http://imagen.com/prod1.jpg");
        oferta2 = new Oferta("Producto 2", 200, "http://imagen.com/prod2.jpg");

        // Agregamos una oferta inicial para pruebas
        repoOfertas.agregarOferta(oferta1);
    }

    @Test
    public void testAgregarOferta() {
        // Agregar una nueva oferta
        repoOfertas.agregarOferta(oferta2);

        // Verificar que ahora hay dos ofertas en el repositorio
        List<Oferta> ofertas = repoOfertas.obtenerTodas();
        Assertions.assertEquals(2, ofertas.size(), "Debe haber dos ofertas en el repositorio");
        Assertions.assertTrue(ofertas.contains(oferta2), "La oferta recién agregada debe estar en el repositorio");
    }

    @Test
    public void testEliminarOferta() {
        // Eliminar la oferta inicial
        repoOfertas.eliminarOferta(oferta1);

        // Verificar que el repositorio ahora está vacío
        List<Oferta> ofertas = repoOfertas.obtenerTodas();
        Assertions.assertEquals(0, ofertas.size(), "El repositorio debe estar vacío después de eliminar la oferta");
    }

    @Test
    public void testObtenerTodas() {
        // Verificar que la oferta inicial está presente
        List<Oferta> ofertas = repoOfertas.obtenerTodas();
        Assertions.assertEquals(1, ofertas.size(), "Debe haber una oferta en el repositorio");
        Assertions.assertEquals(oferta1.getNombre(), ofertas.get(0).getNombre(), "La oferta inicial debe coincidir");
    }

    @Test
    public void testBuscarPorNombre() {
        // Buscar la oferta por nombre
        Oferta ofertaBuscada = repoOfertas.buscarPorNombre("Producto 1");

        // Verificar que se encontró la oferta correcta
        Assertions.assertNotNull(ofertaBuscada, "Debe encontrar una oferta con el nombre dado");
        Assertions.assertEquals(oferta1.getIdOferta(), ofertaBuscada.getIdOferta(), "La oferta encontrada debe coincidir con la esperada");
    }

    /*@Test
    public void testAgregarTodas() {
        // Agregar múltiples ofertas
        repoOfertas.agregarTodas(List.of(oferta2));

        // Verificar que ambas ofertas están presentes
        List<Oferta> ofertas = repoOfertas.obtenerTodas();
        Assertions.assertEquals(2, ofertas.size(), "Debe haber dos ofertas en el repositorio");
        Assertions.assertTrue(ofertas.contains(oferta1), "La oferta inicial debe estar presente");
        Assertions.assertTrue(ofertas.contains(oferta2), "La oferta recién agregada debe estar presente");
    } */
}