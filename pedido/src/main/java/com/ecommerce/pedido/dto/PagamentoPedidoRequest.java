package com.ecommerce.pedido.dto;

import com.ecommerce.pedido.enums.BandeiraCartao;
import com.ecommerce.pedido.enums.TipoPagamento;

import java.math.BigDecimal;

public record PagamentoPedidoRequest(
        TipoPagamento tipoPagamento,
        BandeiraCartao bandeiraCartao,
        String tokenCartao,
        Integer parcelas,
        BigDecimal valorAPagar,
        String emailPagador,
        String primeiroNomePagador,
        String segundoNomePagador,
        String cpfCnpjPagador
) {
}
