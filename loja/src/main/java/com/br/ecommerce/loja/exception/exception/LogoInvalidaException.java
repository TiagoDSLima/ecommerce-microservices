package com.br.ecommerce.loja.exception.exception;

import lombok.Getter;

@Getter
public class LogoInvalidaException extends RuntimeException {

    private String campo;
    private String mensagem;

    public LogoInvalidaException(String campo, String mensagem) {
        this.campo = campo;
        this.mensagem = mensagem;
    }
}
