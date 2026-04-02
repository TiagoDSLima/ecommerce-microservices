package com.ecommerce.pedido.dto;

import com.ecommerce.pedido.enums.StatusPagamento;

import java.math.BigDecimal;

public record PedidoCriadoResponse(
        Long id,
        BigDecimal valorTotal,
        StatusPagamento statusPagamento,
        String qrCodePix,
        String qrCodePixBase64
) {
}
