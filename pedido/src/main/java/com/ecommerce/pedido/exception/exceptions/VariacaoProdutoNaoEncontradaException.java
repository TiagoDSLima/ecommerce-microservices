package com.ecommerce.pedido.exception.exceptions;

import lombok.Getter;

@Getter
public class VariacaoProdutoNaoEncontradaException extends RuntimeException {

    private String campo;
    private String mensagem;

    public VariacaoProdutoNaoEncontradaException(String campo, String mensagem) {
        this.campo = campo;
        this.mensagem = mensagem;
    }
}
