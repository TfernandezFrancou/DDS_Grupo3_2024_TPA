package org.example.personas.contacto;

import org.example.autenticacion.Usuario;

import javax.mail.MessagingException;

public interface MedioDeContacto {

    void notificar(Mensaje mensaje) throws MessagingException;
}