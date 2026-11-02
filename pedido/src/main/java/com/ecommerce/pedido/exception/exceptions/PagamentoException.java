package com.ecommerce.pedido.exception.exceptions;

import lombok.Getter;

@Getter
public class PagamentoException extends RuntimeException {

    private String campo;
    private String mensagem;

    public PagamentoException(String campo, String mensagem) {
        super(mensagem);
        this.campo = campo;
        this.mensagem = mensagem;
    }

    public PagamentoException(String campo, String mensagem, Throwable causa) {
        super(mensagem, causa);
        this.campo = campo;
        this.mensagem = mensagem;
    }
}
