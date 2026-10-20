package com.br.ecommerce.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record ServiceTokenRequest(
        @NotBlank
        String clientId,
        @NotBlank
        String clientSecret) {
}
