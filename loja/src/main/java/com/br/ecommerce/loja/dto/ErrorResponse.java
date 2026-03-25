package com.br.ecommerce.loja.dto;

import java.time.LocalDateTime;

public record ErrorResponse(String campo, String mensagem, LocalDateTime dataHora) {
}
