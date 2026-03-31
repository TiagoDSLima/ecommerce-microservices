package com.ecommerce.pedido.dto;

import java.math.BigDecimal;

public record PedidoResponse(
        Long id,
        String nomeCliente,
        String cpf,
        BigDecimal valorTotal
) {
}
