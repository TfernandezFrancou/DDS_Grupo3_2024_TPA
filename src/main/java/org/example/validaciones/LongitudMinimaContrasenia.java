package org.example.validaciones;

import org.example.config.Configuracion;

public class LongitudMinimaContrasenia extends ValidacionContrasenia {
  private Integer LONGITUD_MINIMA = 8;

  public LongitudMinimaContrasenia() {
    super(Configuracion.obtenerProperties("mensaje.validacion.longitud-pequenia"));
  }

  @Override
  public boolean condition(String password) {
    return password.length() < LONGITUD_MINIMA;
  }
}
