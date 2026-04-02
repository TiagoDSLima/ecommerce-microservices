package com.ecommerce.pedido.dto;

import java.math.BigDecimal;

public record ResumoPedidoResponse(
        BigDecimal totalVenda,
        Integer qtdPedidos,
        Integer compradoresAtivos,
        BigDecimal ticketMedio
) {
}
