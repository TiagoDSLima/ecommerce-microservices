package com.ecommerce.pedido.dto;

import java.math.BigDecimal;

public record ItemPedidoRequest(
        String descricao,
        BigDecimal valorUnitario,
        Integer quantidade,
        Long idProduto,
        Long idProdutoVariacao
) {
    public BigDecimal valorTotalItem() {
        return valorUnitario.multiply(BigDecimal.valueOf(quantidade));
    }

}
