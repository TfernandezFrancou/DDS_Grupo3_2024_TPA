package org.example.colaboraciones;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Ubicacion {
    private float latitud;
    private float longitud;

    public double calcularDistanciaA(Ubicacion ubicacion) {
        final int RADIO_TIERRA_KM = 6371; // Radio de la tierra en kilómetros
        double lat1 = this.getLatitud();
        double lon1 = this.getLongitud();
        double lat2 = ubicacion.getLatitud();
        double lon2 = ubicacion.getLongitud();

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return RADIO_TIERRA_KM * c;
    }
}
