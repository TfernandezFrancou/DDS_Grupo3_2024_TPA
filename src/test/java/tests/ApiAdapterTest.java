package tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.colaboraciones.Ubicacion;
import org.example.excepciones.ApiRequestFailedException;
import org.example.recomendacion.ApiAdapter;
import org.example.recomendacion.IAdapter;
import org.example.recomendacion.Zona;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ApiAdapterTest {

    private final Zona zonaDePrueba = new Zona();

    @BeforeEach
    public void setZonaDePrueba(){
        zonaDePrueba.setRadio(12);
        zonaDePrueba.setNombreZona("Avellaneda");
        Ubicacion ubicacionDePrueba = new Ubicacion();
        ubicacionDePrueba.setLatitud(7.6805F);
        ubicacionDePrueba.setLongitud( -65.3262F);
        zonaDePrueba.setUbicacion(ubicacionDePrueba);
    }

    @Test
    public void debeDevolverLasUbicaciones() throws JsonProcessingException, ApiRequestFailedException {
        IAdapter api = new ApiAdapter();

        List<Ubicacion> ubicacionesRecomendadas = api.consultarUbicaciones(zonaDePrueba);

        Assertions.assertEquals(ubicacionesRecomendadas.size(), 5);

        for (Ubicacion ubicacionN: ubicacionesRecomendadas) {
            Assertions.assertEquals(ubicacionN.getLatitud(), -61.8546F);
            Assertions.assertEquals(ubicacionN.getLongitud(), 92.4792F);
        }
    }


}
