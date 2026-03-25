package com.br.ecommerce.loja.exception.exception;

import lombok.Getter;

@Getter
public class CorInvalidaException extends RuntimeException {

    private String campo;
    private String mensagem;

    public CorInvalidaException(String campo, String mensagem) {
        this.campo = campo;
        this.mensagem = mensagem;
    }
}
