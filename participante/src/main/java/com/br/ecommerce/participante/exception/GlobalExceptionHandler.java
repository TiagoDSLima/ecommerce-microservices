package com.br.ecommerce.participante.exception;

import com.br.ecommerce.participante.dto.ErrorResponse;
import com.br.ecommerce.participante.exception.exceptions.ParticipanteNaoEcontradoExcpetion;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ParticipanteNaoEcontradoExcpetion.class)
    public ResponseEntity<ErrorResponse> handleParticipanteNaoEncontradoException(ParticipanteNaoEcontradoExcpetion ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(
                        ex.getCampo(),
                        ex.getMensagem(),
                        LocalDateTime.now()
                ));
    }
}
