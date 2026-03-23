package com.br.ecommerce.auth.publisher.representation;

import java.time.LocalDate;

public record ParticipanteRepresentation(String nome, String cpf, LocalDate dataNascimento, Long idUsuario) {
}
