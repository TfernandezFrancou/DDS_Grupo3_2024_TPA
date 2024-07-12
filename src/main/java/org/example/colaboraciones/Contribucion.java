package org.example.colaboraciones;

import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.contribuciones.*;
import org.example.migracion.TipoColaboracion;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public abstract class Contribucion {
    private TipoDePersona tiposDePersona;
    private float coeficientePuntaje;
    private LocalDate fecha;

    public abstract void ejecutarContribucion();

    public abstract boolean puedeRealizarContribucion();

    public abstract float obtenerPuntaje();

    public static Contribucion fromCsv(String[] columnas) throws ParseException {
        if (columnas.length != 3) {
            throw new ParseException("Deberian ser 3 columnas", 0);
        }
        LocalDate fecha;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            fecha = LocalDate.parse(columnas[0], formatter);
        } catch (Exception e) {
            throw new ParseException("Fecha de contribucion invalida", 0);
        }
        Integer cantidad;
        try {
            cantidad = Integer.parseInt(columnas[2]);
        } catch (Exception e) {
            throw new ParseException("Cantidad invalida", 2);
        }
        TipoColaboracion tipo;
        try {
            tipo = TipoColaboracion.valueOf(columnas[1]);
        } catch (Exception e) {
            throw new ParseException("Tipo de colaboracion invalida", 1);
        }
        return switch (tipo) {
            case DINERO -> new DonacionDeDinero(TipoDePersona.HUMANA, fecha, cantidad);
            case DONACION_VIANDAS -> new DonacionDeViandas(TipoDePersona.HUMANA, fecha, cantidad);
            case REDISTRIBUCION_VIANDAS -> new DistribucionDeViandas(TipoDePersona.HUMANA, fecha, cantidad);
            case ENTREGA_TARJETAS -> new RegistrarPersonasEnSituacionVulnerable(TipoDePersona.HUMANA, fecha, cantidad);
        };
    }
}
