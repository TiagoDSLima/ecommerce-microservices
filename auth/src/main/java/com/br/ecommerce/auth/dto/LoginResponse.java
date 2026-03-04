package com.br.ecommerce.auth.dto;

public record LoginResponse(
        Long id,
        String email,
        String token
) {
}
