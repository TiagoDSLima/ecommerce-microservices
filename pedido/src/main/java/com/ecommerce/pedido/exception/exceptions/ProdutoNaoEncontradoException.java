package com.ecommerce.pedido.exception.exceptions;

import lombok.Getter;

@Getter
public class ProdutoNaoEncontradoException extends RuntimeException {

    private String campo;
    private String mensagem;

    public ProdutoNaoEncontradoException(String campo, String mensagem) {
        this.campo = campo;
        this.mensagem = mensagem;
    }
}
