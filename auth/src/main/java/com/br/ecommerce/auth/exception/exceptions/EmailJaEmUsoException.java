package com.br.ecommerce.auth.exception.exceptions;

import lombok.Getter;

@Getter
public class EmailJaEmUsoException extends RuntimeException {
  private String campo;
  private String mensagem;
  public EmailJaEmUsoException() {
    this.campo = "Email";
    this.mensagem = "Email já em uso";
  }
}
