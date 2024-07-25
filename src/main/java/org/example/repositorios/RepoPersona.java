package org.example.repositorios;

import org.example.colaboraciones.Ubicacion;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.colaboraciones.contribuciones.heladeras.MovimientoViandas;
import org.example.personas.Persona;
import org.example.personas.roles.Colaborador;
import org.example.personas.roles.Tecnico;
import org.example.recomendacion.Zona;
import org.example.reportes.ItemReporteColaborador;
import org.example.reportes.ItemReporteHeladera;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RepoPersona {
    private List<Persona> personas;

    private static RepoPersona instancia = null;
    private RepoPersona() {
        this.personas = new ArrayList<>();
    }

    public static RepoPersona getInstancia() {
        if (instancia == null) {
            RepoPersona.instancia = new RepoPersona();
        }
        return instancia;
    }

    public void agregarTodas(List<Persona> personas) {
        this.personas.addAll(personas);
    }

    public void agregar(Persona persona) {
        this.personas.add(persona);
    }

    public void eliminar(Persona persona) {
        this.personas.remove(persona);
    }

    //TODO LLEVAR METODO A UBICACION
    private double calcularDistancia(Ubicacion ubicacion1, Ubicacion ubicacion2) {
        final int RADIO_TIERRA_KM = 6371; // Radio de la tierra en kilómetros
        double lat1 = ubicacion1.getLatitud();
        double lon1 = ubicacion1.getLongitud();
        double lat2 = ubicacion2.getLatitud();
        double lon2 = ubicacion2.getLongitud();

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return RADIO_TIERRA_KM * c;
    }
    public Persona tecnicoMasCercanoAHeladera(Heladera heladeraDada){
        List<Persona> tecnicos = this.buscarPersonasConRol(Tecnico.class);
        Persona tecnicoMasCercano = null;
        double distanciaMinima = Double.MAX_VALUE;//para obtener al tecnico con la distancia minima
        Ubicacion ubicacionHeladera = heladeraDada.getUbicacion();

        for (Persona tecnico : tecnicos) {
            Tecnico rolTecnico = (Tecnico) tecnico.getRol();
            for (Zona zona : rolTecnico.getAreasDeCobertura()) {
                double distancia = calcularDistancia(ubicacionHeladera, zona.getUbicacion());
                if (distancia <= zona.getRadio() && distancia < distanciaMinima) {
                    distanciaMinima = distancia;
                    tecnicoMasCercano = tecnico;
                }
            }
        }

        return tecnicoMasCercano;
    }

    public void clean(){
        this.personas.clear();
    }

    public Persona buscarPorNombre(String nombre) {
        return this.personas.stream()
                .filter(persona -> persona.getNombre().equals(nombre))
                .findFirst()
                .orElseThrow();
    }

    public Persona buscarPorNombreYRol(String nombre, Class<?> rol) {
        return this.personas.stream()
            .filter(persona -> persona.getNombre().equals(nombre) && persona.getRol().getClass().equals(rol))
            .findFirst()
            .orElseThrow();
    }

    public List<Persona> buscarPersonasConRol(Class<?> rol) {
        return this.personas.stream()
                .filter(persona -> persona.getRol().getClass().equals(rol))
                .toList();
    }

    public List<ItemReporteColaborador> obtenerCantidadDeViandasDistribuidasPorColaborador(LocalDateTime inicioSemanaActual, LocalDateTime finSemanaActual) {
        List<Persona> colaboradores = this.buscarPersonasConRol(Colaborador.class);
        List<ItemReporteColaborador> reporte = new ArrayList<>();

        for (Persona colaborador : colaboradores) {
            Colaborador  rolColaborador = (Colaborador) colaborador.getRol();
            int viandasDonadasEnLaSemana = rolColaborador.cantidadDeViandasDistribuidasEnLaSemana(inicioSemanaActual, finSemanaActual);

           reporte.add(new ItemReporteColaborador(viandasDonadasEnLaSemana, colaborador));
        }

        return reporte;
    }
}