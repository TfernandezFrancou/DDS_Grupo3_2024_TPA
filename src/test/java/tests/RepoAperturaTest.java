package tests;

import org.example.colaboraciones.Ubicacion;
import org.example.colaboraciones.contribuciones.heladeras.Direccion;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.repositorios.RepoApertura;
import org.example.repositorios.RepoHeladeras;
import org.example.repositorios.RepoTarjetas;
import org.example.repositorios.RepoUbicacion;
import org.example.tarjetas.Apertura;
import org.example.tarjetas.TarjetaColaborador;
import org.example.tarjetas.TipoDeApertura;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

public class RepoAperturaTest {


    RepoApertura repoApertura;
    RepoUbicacion repoUbicacion;

    @BeforeEach
    public void setUp(){
        this.repoApertura = RepoApertura.getInstancia();
        this.repoApertura.clean();
        this.repoUbicacion = RepoUbicacion.getInstancia();
        this.repoUbicacion.clean();

        Apertura apertura_fehaciente = new Apertura(
                null,
                null,
                LocalDateTime.now(),
                TipoDeApertura.APERTURA_FEHACIENTE
        );
        this.repoApertura.agregarApertura(apertura_fehaciente);

        Apertura solicitud_apertura = new Apertura(
                null,
                null,
                LocalDateTime.now(),
                TipoDeApertura.SOLICITUD_APERTURA
        );
        this.repoApertura.agregarApertura(solicitud_apertura);
    }

    @Test
    public void testObtenerAperturaFehaciente(){
       List<Apertura> aperturas= this.repoApertura.obtenerAperturasFehacientes();

        Assertions.assertEquals(1, aperturas.size());
    }

    @Test
    public void testObtenerSolicitudesDeApertura(){
        List<Apertura> aperturas= this.repoApertura.obtenerSolicitudesDeAperturas();

        Assertions.assertEquals(1, aperturas.size());
    }

    @Test
    public void testPuedeQuitarAperturas(){
        List<Apertura> aperturasAntes= this.repoApertura.obtenerAperturasFehacientes();
        Assertions.assertEquals(1, aperturasAntes.size());

        this.repoApertura.quitarApertura(aperturasAntes.get(0));
        List<Apertura> aperturasDespues= this.repoApertura.obtenerAperturasFehacientes();

        Assertions.assertEquals(0, aperturasDespues.size());
    }

    @Test
    public void testBuscarSolicitudesDeAperturaDeTarjeta(){
        TarjetaColaborador tarjeta = new TarjetaColaborador();
        tarjeta.setLimiteDeTiempoDeUsoEnHoras(24);
        RepoTarjetas repoTarjetas =RepoTarjetas.getInstancia();
        repoTarjetas.agregar(tarjeta);

        Apertura solicitud_apertura = new Apertura(
                tarjeta,
                null,
                LocalDateTime.now(),
                TipoDeApertura.SOLICITUD_APERTURA
        );
        this.repoApertura.agregarApertura(solicitud_apertura);

       List<Apertura> aperturas= this.repoApertura.buscarSolicitudesDeAperturaDeTarjeta(tarjeta);
       repoApertura.clean();
       repoTarjetas.clean();
       Assertions.assertEquals(1, aperturas.size());
    }

    @Test
    public void testExisteSolicitudDeAperturaDeTarjetaParaHeladera(){
        TarjetaColaborador tarjeta = new TarjetaColaborador();
        tarjeta.setLimiteDeTiempoDeUsoEnHoras(24);
        RepoTarjetas repoTarjetas =RepoTarjetas.getInstancia();
        repoTarjetas.agregar(tarjeta);
        Ubicacion ubicacion = new Ubicacion(-34.598620F,-58.420090F);
        repoUbicacion.agregar(ubicacion);

        Heladera heladera1 = new Heladera("Medarno UTN",
                    ubicacion,
                    new Direccion("Av. Medrano","951","CABA"),
                    200);
        RepoHeladeras repoHeladeras = RepoHeladeras.getInstancia();
        repoHeladeras.agregar(heladera1);

        Apertura solicitud_apertura = new Apertura(
                tarjeta,
                heladera1,
                LocalDateTime.now(),
                TipoDeApertura.SOLICITUD_APERTURA
        );
        this.repoApertura.agregarApertura(solicitud_apertura);

        boolean existe = this.repoApertura.existeSolicitudDeAperturaDeTarjetaParaHeladera(tarjeta, heladera1);
        repoApertura.clean();
        repoTarjetas.clean();
        repoHeladeras.clean();

        Assertions.assertTrue(existe);
    }
}
