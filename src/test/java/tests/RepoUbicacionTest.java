package tests;

import org.example.colaboraciones.Ubicacion;
import org.example.repositorios.RepoUbicacion;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RepoUbicacionTest {

    RepoUbicacion repoUbicacion;
    Ubicacion ubicacion1;

    @BeforeEach
    public void setUp() {
        this.repoUbicacion = RepoUbicacion.getInstancia();
        this.repoUbicacion.clean();

        ubicacion1 = new Ubicacion(-34.705722F, -58.501592F);

        this.repoUbicacion.agregar(ubicacion1);
    }

    @Test
    public void testAgregarUbicacion() {
        Ubicacion ubicacion2 = new Ubicacion(-34.705722F, -58.501592F);
        this.repoUbicacion.agregar(ubicacion2);

        Assertions.assertEquals(2, this.repoUbicacion.getUbicaciones().size());
    }

    @Test
    public void testEliminarUbicacion() {
        this.repoUbicacion.eliminar(ubicacion1);

        Assertions.assertEquals(0, this.repoUbicacion.getUbicaciones().size());
    }

}
