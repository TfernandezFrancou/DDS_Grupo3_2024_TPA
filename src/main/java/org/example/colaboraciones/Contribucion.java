package org.example.colaboraciones;

import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.contribuciones.*;
import org.example.migracion.TipoColaboracion;
import org.example.personas.roles.Colaborador;

import javax.persistence.*;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Contribucion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_contribucion")
    private int idContribucion;

    @ManyToOne
    protected Colaborador colaborador;

    @ElementCollection(targetClass = TipoDePersona.class)
    @CollectionTable(
            name = "tipo_persona",
            joinColumns = @JoinColumn(name = "id_contribucion")
    )
    @Column(name = "descripcion")
    @Enumerated(EnumType.STRING)
    protected Set<TipoDePersona> tiposDePersona;

    private LocalDate fecha;

    public abstract float getCoeficientePuntaje();

    public void ejecutarContribucion()  throws Exception {
        colaborador.agregarContribucion(this);
        colaborador.calcularPuntuaje();
    }

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
            case DINERO -> new DonacionDeDinero(fecha, cantidad);
            case DONACION_VIANDAS -> new DonacionDeViandas(fecha, cantidad);
            case REDISTRIBUCION_VIANDAS -> new DistribucionDeViandas(fecha, cantidad);
            case ENTREGA_TARJETAS -> new RegistrarPersonasEnSituacionVulnerable(fecha, cantidad);
        };
    }
}
