package com.br.ecommerce.participante.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record ParticipanteResponse(
        Long id,
        String name,
        String cpf,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate dataNascimento,
        Long idUsuario) {
}
