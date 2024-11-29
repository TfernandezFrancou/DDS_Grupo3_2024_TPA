package org.example.autenticacion;


import java.util.HashMap;
import java.util.Map;

public class SessionManager {
    private static SessionManager instancia;

    private Map<String, Object> sesiones;

    private SessionManager(){
        this.sesiones = new HashMap<>();
    }

    public static SessionManager getInstancia() {
        if (instancia == null) {
            instancia = new SessionManager();
        }
        return instancia;
    }

    public void crearSesion(String clave, Object valor) {
        this.agregarAtributo(clave,valor);
    }

    public Object obtenerAtributo(String clave) {
        return this.sesiones.get(clave);
    }

    public void agregarAtributo(String clave, Object valor) {
        this.sesiones.put(clave, valor);
    }

    public void agregarAtributos(Map<String, Object> nuevosAtributos) {
        this.sesiones.putAll(nuevosAtributos);
    }

    public Object eliminarAtributo(String clave) {
        return this.sesiones.remove(clave);
    }

    public void cerrarsesion() {
         this.sesiones.clear();
    }
}
