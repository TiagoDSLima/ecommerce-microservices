package com.ecommerce.pedido.dto;

import com.ecommerce.pedido.enums.BandeiraCartao;
import com.ecommerce.pedido.enums.TipoPagamento;

import java.math.BigDecimal;

public record DadosPagamentoDto(
        Long pedidoId,
        BigDecimal valor,
        String token,
        TipoPagamento tipo,
        BandeiraCartao bandeira,
        Integer parcelas,
        String email,
        String cpfCnpj,
        String primeiroNome,
        String segundoNome
) {
}
