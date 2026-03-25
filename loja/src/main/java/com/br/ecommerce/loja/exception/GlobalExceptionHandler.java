package com.br.ecommerce.loja.exception;

import com.br.ecommerce.loja.dto.ErrorResponse;
import com.br.ecommerce.loja.exception.exception.CorInvalidaException;
import com.br.ecommerce.loja.exception.exception.LogoInvalidaException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LogoInvalidaException.class)
    public ResponseEntity<ErrorResponse> handleLogoInvalidaException(LogoInvalidaException ex){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(
                        ex.getCampo(),
                        ex.getMensagem(),
                        LocalDateTime.now()
                ));
    }

    @ExceptionHandler(CorInvalidaException.class)
    public ResponseEntity<ErrorResponse> handleCorInvalidaException(CorInvalidaException ex){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(
                        ex.getCampo(),
                        ex.getMensagem(),
                        LocalDateTime.now()
                ));
    }
}
