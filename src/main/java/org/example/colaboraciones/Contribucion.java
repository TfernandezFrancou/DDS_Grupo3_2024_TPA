package org.example.colaboraciones;

import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.contribuciones.*;
import org.example.migracion.TipoColaboracion;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public abstract class Contribucion {
    private TipoDePersona tiposDePersona;
    private float coeficientePuntaje;

    public abstract void ejecutarContribucion();

    public abstract boolean puedeRealizarContribucion();

    public abstract float obtenerPuntaje();

    public static Contribucion fromCsv(String[] columnas) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate fecha = LocalDate.parse(columnas[0], formatter);
        Integer cantidad = Integer.parseInt(columnas[2]);
        TipoColaboracion tipo = TipoColaboracion.valueOf(columnas[1]);
        return switch (tipo) {
            case DINERO -> new DonacionDeDinero(cantidad);
            case DONACION_VIANDAS -> new DonacionDeViandas(cantidad);
            case REDISTRIBUCION_VIANDAS -> new DistribucionDeViandas(cantidad);
            case ENTREGA_TARJETAS -> new RegistrarPersonasEnSituacionVulnerable(cantidad);
        };
    }
}
