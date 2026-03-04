package com.br.ecommerce.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;


public record RegisterRequest(

        @Email
        @NotBlank
        String email,
        @NotBlank
        String senha,
        @NotBlank
        String nome,
        @NotBlank
        String cpf,
        LocalDate dataNascimento



) {
}
