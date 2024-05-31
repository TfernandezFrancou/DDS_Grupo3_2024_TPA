package org.example.puntaje;

import lombok.Getter;
import lombok.Setter;


@Setter
public class RegistroContribucion extends CalculadorDePuntos{
    private int mesesHeladeraActiva;
    private int cantidadHeladerasActivas;
    private int tarjetasRepartidas;
    private int viandasDonadas;
    private int viandasDistribuidas;
    private int dineroDonado;


    @Override
    public float getMesesHeladeraActiva(){
        return mesesHeladeraActiva;
    }
    @Override
    public float getCantidadDeHeladerasActivas() {
        return cantidadHeladerasActivas;
    }

    @Override
    public  float getTarjetasRepartidas(){
        return tarjetasRepartidas;
    }

    @Override
    public  float getViandasDonadas(){
        return viandasDonadas;
    }

    @Override
    public float getViandasDistribuidas(){
        return viandasDistribuidas;
    }

    @Override
    public float getDineroDonado(){
        return dineroDonado;
    }

}
