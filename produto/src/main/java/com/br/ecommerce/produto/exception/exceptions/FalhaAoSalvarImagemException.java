package com.br.ecommerce.produto.exception.exceptions;

import lombok.Getter;

@Getter
public class FalhaAoSalvarImagemException extends RuntimeException {

    private String campo;
    private String mensagem;

    public FalhaAoSalvarImagemException(String campo, String mensagem) {
        this.campo = campo;
        this.mensagem = mensagem;
    }
}
