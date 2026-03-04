package com.br.ecommerce.auth.exception;

import com.br.ecommerce.auth.dto.ErrorResponse;
import com.br.ecommerce.auth.exception.exceptions.CredenciaisInvalidasException;
import com.br.ecommerce.auth.exception.exceptions.EmailJaEmUsoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailJaEmUsoException.class)
    public ResponseEntity<ErrorResponse> handleEmailJaEmUsoException(EmailJaEmUsoException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(
                        ex.getCampo(),
                        ex.getMensagem(),
                        LocalDateTime.now()
                ));
    }

    @ExceptionHandler(CredenciaisInvalidasException.class)
    public ResponseEntity<ErrorResponse> handleLoginIncorretoException(CredenciaisInvalidasException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse(
                        ex.getCampo(),
                        ex.getMensagem(),
                        LocalDateTime.now()
                ));
    }

}
