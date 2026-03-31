package com.ecommerce.pedido.dto;

import java.math.BigDecimal;

public record ItemPedidoResponse(
        String descricao,
        BigDecimal valorUnitario,
        Integer quantidade,
        BigDecimal valorTotalItem
        ) {
}
