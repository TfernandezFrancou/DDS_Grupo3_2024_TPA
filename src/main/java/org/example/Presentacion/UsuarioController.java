package org.example.Presentacion;

import io.javalin.http.Context;
import org.example.autenticacion.IniciarSesion;
import org.example.autenticacion.RegistrarUsuario;
import org.example.autenticacion.Usuario;
import org.example.colaboraciones.contribuciones.heladeras.Direccion;
import org.example.personas.Persona;
import org.example.personas.PersonaHumana;
import org.example.personas.PersonaJuridica;
import org.example.personas.TipoJuridico;
import org.example.personas.documentos.Documento;
import org.example.personas.documentos.TipoDocumento;
import org.example.repositorios.RepoPersona;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class UsuarioController  {

    public static void postInicioSeccion(@NotNull Context context){
        String nombreDeUsuario = context.formParam("username");
        String contraseniaUsuario = context.formParam("password");

        IniciarSesion iniciarSesion = new IniciarSesion();
        if(iniciarSesion.iniciarSesion(nombreDeUsuario, contraseniaUsuario)){
            context.redirect("/heladeras");
        }else {
            context.redirect("/views/usuarios/InicioSesion.html");
        }
    }

    public static void postRegistrarse(@NotNull Context context) {
        String nombreDeUsuario = context.formParam("username");
        String contraseniaUsuario = context.formParam("password");

        RegistrarUsuario registrarUsuario = new RegistrarUsuario();

        Usuario usuario;

        try{
            usuario = registrarUsuario.registrarUsuario(nombreDeUsuario, contraseniaUsuario);

            almacenarPersona(context, usuario);
            context.redirect("/views/usuarios/InicioSesion.html");
        }catch (Exception exception){
            Map<String, Object> model = new HashMap<>();
            model.put("error", exception.getMessage());
            context.render("/views/usuarios/Registrarse.mustache", model);
        }
    }

    private static void almacenarPersona(Context context,  Usuario usuario){
        boolean esJuridica = "on".equals(context.formParam("esJuridica"));
        String direccion = context.formParam("direccion");
        String localidad = context.formParam("localidad");

        Direccion direccion1 = new Direccion();
        String[] partes = direccion.split(" ");
        direccion1.setNombreCalle(partes[0]);
        direccion1.setAltura(partes[1]);
        direccion1.setLocalidad(localidad.toLowerCase());

        boolean donarDinero = "on".equals(context.formParam("donarDinero"));
        boolean cargoHeladera = "on".equals(context.formParam("cargoHeladera"));

        String documento = context.formParam("documento");
        String tipoDocumento = context.formParam("tipoDocumento");

        Documento documento1 = new Documento();
        documento1.setNumeroDocumento(documento);
        documento1.setTipoDocumento(TipoDocumento.valueOf(tipoDocumento));

        // medios de contacto
        String tipoContacto = context.formParam("tipoContacto");
        String contacto = context.formParam("contacto");//TODO medios de contacto


        Persona persona;

        if(!esJuridica){
            String nombre = context.formParam("nombre");
            String apellido = context.formParam("apellido");
            String fechaNacimiento = context.formParam("fechaNacimiento");//cast to date

            boolean donarVianda = "on".equals(context.formParam("donarVianda"));
            boolean distribuirViandas = "on".equals(context.formParam("distribuirViandas"));
            boolean registrarPersonaEnSituaciónVulnerable = "on".equals(context.formParam("registrarPersonasSituacionVulnerable"));

            persona =  new PersonaHumana();

            ((PersonaHumana) persona).setNombre(nombre);
            ((PersonaHumana) persona).setApellido(apellido);
            ((PersonaHumana) persona).setFechaNacimiento(LocalDate.parse(fechaNacimiento));
            persona.setDireccion(direccion1);
            persona.setDocumento(documento1);

        } else {
            String razonSocial = context.formParam("razonSocial");
            String tipoRazonSocial = context.formParam("tipoRazonSocial");
            String rubro = context.formParam("rubro");


            boolean ofrecerProductos = "on".equals(context.formParam("ofrecerProductos"));

            persona =  new PersonaJuridica(razonSocial);
            ((PersonaJuridica) persona).setTipo(TipoJuridico.valueOf(tipoRazonSocial.toUpperCase()));
            ((PersonaJuridica) persona).setRubro(rubro);
            persona.setDireccion(direccion1);
            persona.setDocumento(documento1);
        }

        RepoPersona.getInstancia().agregar(persona);
        usuario.setDocumento(persona.getDocumento());
        usuario.setColaborador(persona);
    }

    public static void getRegistrarUsuario(@NotNull Context context){
        context.render("/views/usuarios/Registrarse.mustache");
    }
}
