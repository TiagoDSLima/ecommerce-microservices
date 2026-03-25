package com.br.ecommerce.produto.exception;

import com.br.ecommerce.produto.dto.ErrorResponse;
import com.br.ecommerce.produto.exception.exceptions.EstoqueInvalidoException;
import com.br.ecommerce.produto.exception.exceptions.FalhaAoSalvarImagemException;
import com.br.ecommerce.produto.exception.exceptions.ProdutoNaoEcontradoException;
import com.br.ecommerce.produto.exception.exceptions.ProdutoUtilizadoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProdutoNaoEcontradoException.class)
    public ResponseEntity<ErrorResponse> handleProdutoNaoEncontradoException(ProdutoNaoEcontradoException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(
                        ex.getCampo(),
                        ex.getMensagem(),
                        LocalDateTime.now()
                ));
    }

    @ExceptionHandler(FalhaAoSalvarImagemException.class)
    public ResponseEntity<ErrorResponse> handleFalhaAoSalvarImagemException(FalhaAoSalvarImagemException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(
                        ex.getCampo(),
                        ex.getMensagem(),
                        LocalDateTime.now()
                ));
    }

    @ExceptionHandler(EstoqueInvalidoException.class)
    public ResponseEntity<ErrorResponse> handleEstoqueInvalidoException(EstoqueInvalidoException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(
                        ex.getCampo(),
                        ex.getMensagem(),
                        LocalDateTime.now()
                ));
    }

    @ExceptionHandler(ProdutoUtilizadoException.class)
    public ResponseEntity<ErrorResponse> handleProdutoUtilizadoException(ProdutoUtilizadoException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(
                        ex.getCampo(),
                        ex.getMensagem(),
                        LocalDateTime.now()
                ));
    }
}
