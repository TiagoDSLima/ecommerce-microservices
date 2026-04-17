package com.ecommerce.pedido.exception.exceptions;

import lombok.Getter;

@Getter
public class ParticipanteNaoEncontradoException extends RuntimeException {

    private String campo;
    private String mensagem;

    public ParticipanteNaoEncontradoException(String campo, String mensagem) {
        this.campo = campo;
        this.mensagem = mensagem;
    }
}
