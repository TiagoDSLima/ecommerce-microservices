package com.br.ecommerce.auth.dto;

public record ServiceTokenResponse(
        String token,
        long expiresIn
) {
}
