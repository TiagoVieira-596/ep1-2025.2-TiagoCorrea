package br.hospital.exceptions;

public class CpfInvalidoException extends Exception {
  public CpfInvalidoException(String mensagem) {
      super(mensagem);
  }
}