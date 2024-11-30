package tests;

import org.example.repositorios.RepoTarjetas;
import org.example.tarjetas.Tarjeta;
import org.example.tarjetas.TarjetaColaborador;
import org.example.tarjetas.TarjetaHeladera;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RepoTarjetasTest {

    private RepoTarjetas repoTarjetas;

    @BeforeEach
    public void setUp() {
        this.repoTarjetas = RepoTarjetas.getInstancia();
        this.repoTarjetas.clean();
    }

    @Test
    public void testAgregarTodas() {
        TarjetaHeladera tarjetaHeladera = new TarjetaHeladera();
        TarjetaHeladera tarjetaHeladera2 = new TarjetaHeladera();
        List<TarjetaHeladera> tarjetas = new ArrayList<>(Arrays.asList(tarjetaHeladera,tarjetaHeladera2));
        this.repoTarjetas.agregarTodas(tarjetas);
        Assertions.assertEquals(2, this.repoTarjetas.getTarjetas().size());
    }

    @Test
    public void testAgregarTodasTarjetasColaboradores() {
        TarjetaColaborador tarjetaColaborador = new TarjetaColaborador();
        TarjetaColaborador tarjetaColaborador2 = new TarjetaColaborador();
        List<TarjetaColaborador> tarjetas = new ArrayList<>(Arrays.asList(tarjetaColaborador,tarjetaColaborador2));
        this.repoTarjetas.agregarTodasTarjetasColaboradores(tarjetas);
        Assertions.assertEquals(2, this.repoTarjetas.getTarjetas().size());
    }

    @Test
    public void testAgregar() {
        TarjetaHeladera tarjetaHeladera = new TarjetaHeladera();
        this.repoTarjetas.agregar(tarjetaHeladera);
        Assertions.assertEquals(1, this.repoTarjetas.getTarjetas().size());
        TarjetaColaborador tarjetaColaborador = new TarjetaColaborador();
        this.repoTarjetas.agregar(tarjetaColaborador);
        Assertions.assertEquals(2, this.repoTarjetas.getTarjetas().size());
    }

    @Test
    public void testBuscarTarjetaPorId() {
        TarjetaHeladera tarjetaHeladera = new TarjetaHeladera();
        this.repoTarjetas.agregar(tarjetaHeladera);
        Tarjeta tarjeta = this.repoTarjetas.buscarTarjetaPorId(tarjetaHeladera.getIdTarjeta());
        Assertions.assertEquals(tarjetaHeladera.getIdTarjeta(), tarjeta.getIdTarjeta());
    }

}