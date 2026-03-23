package com.br.ecommerce.participante.dto;

import java.time.LocalDateTime;

public record ErrorResponse(String campo, String mensagem, LocalDateTime dataHora) {
}
