package com.ecommerce.pedido.exception.exceptions;

import lombok.Getter;

@Getter
public class EstoqueInsuficienteException extends RuntimeException {

    private String campo;
    private String mensagem;

    public EstoqueInsuficienteException(String campo, String mensagem) {
        this.campo = campo;
        this.mensagem = mensagem;
    }
}
