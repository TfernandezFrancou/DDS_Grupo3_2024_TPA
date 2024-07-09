package org.example.migracion;

import lombok.Getter;
import lombok.Setter;
import org.example.autenticacion.Usuario;
import org.example.colaboraciones.Contribucion;
import org.example.colaboraciones.TipoDePersona;
import org.example.colaboraciones.contribuciones.DistribucionDeViandas;
import org.example.colaboraciones.contribuciones.DonacionDeViandas;
import org.example.colaboraciones.contribuciones.RegistrarPersonasEnSituacionVulnerable;
import org.example.excepciones.UserException;
import org.example.migracion.estrategiasColaboracion.*;
import org.example.personas.PersonaHumana;
import org.example.personas.contacto.CorreoElectronico;
import org.example.personas.contacto.MedioDeContacto;
import org.example.personas.documentos.Documento;
import org.example.repositorios.Registrados;

import javax.mail.MessagingException;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class MigradorContribucion {
    private List<CSVColaborador> csvColaboradores;
    private EstrategiaColaboracion estrategiaColaboracion;


    public MigradorContribucion(){
        this.csvColaboradores = new ArrayList<>();
    }

    public void cargarCSV(FileInputStream archivoCSV) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(archivoCSV.getFD()));
        String linea = "";
        for (int i = 0;(linea = bufferedReader.readLine()) != null; i++) {
            if(i == 0) continue; // ignoro el nombre de las columnas
            CSVColaborador csvColaborador = new CSVColaborador();
            csvColaborador.cargarCSV(linea.split(","));

            this.csvColaboradores.add(csvColaborador);
        }
    }

    public void migrarColaboradores() throws MessagingException {
        for (CSVColaborador colaborador: this.csvColaboradores) {
            this.migrarContribucion(colaborador);
        }
    }

    private void migrarContribucion(CSVColaborador csvColaborador) throws MessagingException {
        Usuario usuarioColaborador = this.buscarOCrearUsuario(csvColaborador.getDocumento());

        PersonaHumana colab = (PersonaHumana) usuarioColaborador.getColaborador();

        colab.setNombre(csvColaborador.getNombre());
        colab.setApellido(csvColaborador.getApellido());
        CorreoElectronico email = new CorreoElectronico();
            email.setCorreoElectronico(csvColaborador.getMail());
        colab.setMediosDeContacto(List.of(email));

        TipoColaboracion tipoColaboracion = csvColaborador.getFormaDeColaboarcion();
        Integer cantidad = csvColaborador.getCantidad();

        switch (tipoColaboracion){
            case DINERO -> estrategiaColaboracion = new EstrategiaDinero();
            case DONACION_VIANDAS ->  estrategiaColaboracion = new EstrategiaDonacionViandas();
            case REDISTRIBUCION_VIANDAS -> estrategiaColaboracion = new EstrategiaDistribucionDeViandas();
            case ENTREGA_TARJETAS -> estrategiaColaboracion = new EstrategiaEntregarTarjetas();
        }

        Contribucion contribucion = estrategiaColaboracion.cargarColaboracion(cantidad);

        colab.agregarContribucion(contribucion);
        //todo guardar Colaborador y contribuciones en la DB
    }

    private Usuario buscarOCrearUsuario(Documento documento) throws MessagingException {
        Usuario usuarioColaborador = null;
        try {
            usuarioColaborador = Registrados.getInstancia()
                    .obtenerUsuarioPorDocumento(documento);
        } catch (UserException userException) {
            usuarioColaborador = new Usuario();
            usuarioColaborador.setDocumento(documento);
            usuarioColaborador.setContrasenia(this.generarCredenciales());

            PersonaHumana colaborador = new PersonaHumana();
            usuarioColaborador.setColaborador(colaborador);

            Registrados.getInstancia().agregarUsuarios(usuarioColaborador);
            this.enviarCredenciales(usuarioColaborador);
        }
        return usuarioColaborador;
    }
    private void enviarCredenciales(Usuario usuario) throws MessagingException {
        //enviarCredenciales via Mail
        MedioDeContacto medioDeContacto= usuario.getColaborador().getMediosDeContacto().get(0);
        medioDeContacto.notificar("Credenciales",usuario.getContrasenia());//TODO agregarle mensaje de bienvenida bien completo
    }
    private String generarCredenciales(){
        //TODO generar credenciales
        return UUID.randomUUID().toString();
    }
}
