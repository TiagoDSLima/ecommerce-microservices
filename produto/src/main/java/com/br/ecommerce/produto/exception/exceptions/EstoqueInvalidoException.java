package com.br.ecommerce.produto.exception.exceptions;

import lombok.Getter;

@Getter
public class EstoqueInvalidoException extends RuntimeException {

    private String campo;
    private String mensagem;

    public EstoqueInvalidoException(String campo, String mensagem) {
        this.campo = campo;
        this.mensagem = mensagem;
    }
}
