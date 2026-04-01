package com.ecommerce.pedido.strategy.pagamento.strategy;

import com.ecommerce.pedido.dto.DadosPagamentoDto;
import com.mercadopago.client.payment.PaymentCreateRequest;

public interface PagamentoStrategy {
    PaymentCreateRequest criar(DadosPagamentoDto dadosPagamento);
}
