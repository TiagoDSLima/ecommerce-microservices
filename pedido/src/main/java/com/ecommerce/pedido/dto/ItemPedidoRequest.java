package com.ecommerce.pedido.dto;

import java.math.BigDecimal;

public record ItemPedidoRequest(
        String descricao,
        BigDecimal valorUnitario,
        Integer quantidade,
        Long idProduto
) {
    public BigDecimal valorTotalItem() {
        return valorUnitario.multiply(BigDecimal.valueOf(quantidade));
    }

}
