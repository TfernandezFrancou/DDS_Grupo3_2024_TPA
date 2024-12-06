package org.example.excepciones;

public class EmailNoRegistradoException extends Exception{
    public EmailNoRegistradoException(String msg){
        super(msg);
    }
}
