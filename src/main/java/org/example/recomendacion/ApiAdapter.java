package org.example.recomendacion;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.core.Response;
import org.apache.cxf.jaxrs.client.WebClient;
import org.example.colaboraciones.Ubicacion;
import org.example.config.Configuracion;
import org.example.excepciones.ApiRequestFailedException;


import java.util.Arrays;
import java.util.List;

public class ApiAdapter implements IAdapter {
    @Override
    public List<Ubicacion> consultarUbicaciones(Zona zona) throws JsonProcessingException, ApiRequestFailedException {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);


        WebClient clientPoints = WebClient.create(Configuracion.obtenerProperties("api.recomendacion_puntos.url"));

        Response response = clientPoints
                .header("Content-Type", "application/json")
                .post(objectMapper.writeValueAsString(zona));

        int status = response.getStatus();

        if (status == 200){
            String puntosResponse = response.readEntity(String.class);

            Ubicacion[] ubicaciones = objectMapper.readValue(puntosResponse, Ubicacion[].class);

            return Arrays.stream(ubicaciones).toList();
        } else {
            throw new ApiRequestFailedException();
        }
    }
}
