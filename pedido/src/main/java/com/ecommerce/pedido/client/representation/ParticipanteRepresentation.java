package com.ecommerce.pedido.client.representation;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record ParticipanteRepresentation(
        Long id,
        String name,
        String cpf
        ) {
}
