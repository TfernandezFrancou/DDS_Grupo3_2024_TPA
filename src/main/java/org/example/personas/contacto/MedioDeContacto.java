package org.example.personas.contacto;

import org.example.autenticacion.Usuario;

import javax.mail.MessagingException;

public interface MedioDeContacto {

    public void notificar(String subject, String message) throws MessagingException;
}