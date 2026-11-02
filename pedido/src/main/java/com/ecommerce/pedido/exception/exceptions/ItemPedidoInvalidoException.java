package com.ecommerce.pedido.exception.exceptions;

import lombok.Getter;

@Getter
public class ItemPedidoInvalidoException extends RuntimeException {

    private String campo;
    private String mensagem;

    public ItemPedidoInvalidoException(String campo, String mensagem) {
        this.campo = campo;
        this.mensagem = mensagem;
    }
}
