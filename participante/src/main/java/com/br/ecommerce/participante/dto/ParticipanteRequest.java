package com.br.ecommerce.participante.dto;

import java.time.LocalDate;

public record ParticipanteRequest(String nome, String cpf, LocalDate dataNascimento, Long idUsuario) {
}
