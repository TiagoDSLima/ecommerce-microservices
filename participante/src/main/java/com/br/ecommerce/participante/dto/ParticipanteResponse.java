package com.br.ecommerce.participante.dto;

import java.time.LocalDate;

public record ParticipanteResponse(Long id, String name, String cpf, LocalDate dataNascimento, Long idUsuario) {
}
