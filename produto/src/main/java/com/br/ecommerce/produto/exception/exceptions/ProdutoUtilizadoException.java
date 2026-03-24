package com.br.ecommerce.produto.exception.exceptions;

public class ProdutoUtilizadoException extends RuntimeException {

    private String campo;
    private String mensagem;

    public ProdutoUtilizadoException(String campo, String mensagem) {
        this.campo = campo;
        this.mensagem = mensagem;
    }
}
