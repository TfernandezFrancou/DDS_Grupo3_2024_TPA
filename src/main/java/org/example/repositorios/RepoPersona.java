package org.example.repositorios;

import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.colaboraciones.contribuciones.heladeras.MovimientoViandas;
import org.example.personas.Persona;
import org.example.personas.roles.Colaborador;
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