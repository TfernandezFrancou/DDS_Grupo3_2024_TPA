package org.example.excepciones;

public class EmailYaRegistrado extends Exception {

  public EmailYaRegistrado(String msg) {
    super(msg);
  }
}
