package com.ecommerce.pedido.dto;

import com.ecommerce.pedido.enums.BandeiraCartao;
import com.ecommerce.pedido.enums.StatusPagamento;
import com.ecommerce.pedido.enums.TipoPagamento;

import java.math.BigDecimal;

public record PagamentoResponse(
        Long mercadoPagoId,
        TipoPagamento tipoPagamento,
        BandeiraCartao bandeiraCartao,
        StatusPagamento statusPagamento,
        String observacaoStatus,
        BigDecimal valorPagamento,
        String qrCodePix,
        String qrCodeBase64Pix
) {
}
