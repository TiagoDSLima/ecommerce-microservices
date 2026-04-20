package com.br.ecommerce.produto.exception;


import com.ecommerce.pedido.dto.ErrorResponse;
import com.ecommerce.pedido.exception.exceptions.EstoqueInsuficienteException;
import com.ecommerce.pedido.exception.exceptions.ProdutoNaoEncontradoException;
import com.ecommerce.pedido.exception.exceptions.VariacaoProdutoNaoEncontradaException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProdutoNaoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleProdutoNaoEncontradoException(ProdutoNaoEncontradoException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(
                        ex.getCampo(),
                        ex.getMensagem(),
                        LocalDateTime.now()
                ));
    }

    @ExceptionHandler(VariacaoProdutoNaoEncontradaException.class)
    public ResponseEntity<ErrorResponse> handleVariacaoProdutoNaoEncontradaException(VariacaoProdutoNaoEncontradaException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(
                        ex.getCampo(),
                        ex.getMensagem(),
                        LocalDateTime.now()
                ));
    }

    @ExceptionHandler(EstoqueInsuficienteException.class)
    public ResponseEntity<ErrorResponse> handleEstoqueInsuficienteException(EstoqueInsuficienteException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(
                        ex.getCampo(),
                        ex.getMensagem(),
                        LocalDateTime.now()
                ));
    }


}
