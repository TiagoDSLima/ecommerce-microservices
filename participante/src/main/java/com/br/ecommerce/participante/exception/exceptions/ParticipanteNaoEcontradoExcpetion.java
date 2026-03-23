package com.br.ecommerce.participante.exception.exceptions;

import lombok.Getter;

@Getter
public class ParticipanteNaoEcontradoExcpetion extends RuntimeException {

    private String campo;
    private String mensagem;

    public ParticipanteNaoEcontradoExcpetion(String campo) {
        this.campo = campo;
        this.mensagem = "Participante não encontrado!";
    }
}
