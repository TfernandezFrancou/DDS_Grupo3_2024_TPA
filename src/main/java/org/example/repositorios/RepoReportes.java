package org.example.repositorios;

import lombok.Getter;
import org.example.reportes.ReportesDeLaSemana;

import java.util.ArrayList;
import java.util.List;

@Getter
public class RepoReportes {
    private List<ReportesDeLaSemana> reportes;

    private static RepoReportes instancia = null;
    private RepoReportes() {
        this.reportes = new ArrayList<>();
    }

    public static RepoReportes getInstancia() {
        if (instancia == null) {
            RepoReportes.instancia = new RepoReportes();
        }
        return instancia;
    }
    public void agregarReporte(ReportesDeLaSemana nuevoReporte){
        this.reportes.add(nuevoReporte);
    }
    public void eliminarReporte(ReportesDeLaSemana reporte){
        this.reportes.remove(reporte);
    }

    public void clean(){
        this.reportes.clear();
    }
}
