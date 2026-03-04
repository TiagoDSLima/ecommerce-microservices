package com.br.ecommerce.auth.exception.exceptions;

import lombok.Getter;

@Getter
public class CredenciaisInvalidasException extends RuntimeException {

    private String campo;
    private String mensagem;
    public CredenciaisInvalidasException() {
        this.campo = "Email/Senha";
        this.mensagem = "Login ou senha inválidos!";
    }
}
