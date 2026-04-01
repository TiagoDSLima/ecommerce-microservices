package com.ecommerce.pedido.strategy.pagamento.impl;

import com.ecommerce.pedido.dto.DadosPagamentoDto;
import com.ecommerce.pedido.strategy.pagamento.strategy.PagamentoStrategy;
import com.mercadopago.client.common.IdentificationRequest;
import com.mercadopago.client.payment.PaymentCreateRequest;
import com.mercadopago.client.payment.PaymentPayerRequest;

public class PixStrategy implements PagamentoStrategy {

    @Override
    public PaymentCreateRequest criar(DadosPagamentoDto dadosPagamento) {
        return PaymentCreateRequest.builder()
                .transactionAmount(dadosPagamento.valor())
                .description("Pedido #" + dadosPagamento.pedidoId())
                .externalReference(String.valueOf(dadosPagamento.pedidoId()))
                .paymentMethodId("pix")
                .payer(
                        PaymentPayerRequest.builder()
                                .email(dadosPagamento.email())
                                .firstName(dadosPagamento.primeiroNome())
                                .lastName(dadosPagamento.segundoNome())
                                .identification(
                                        IdentificationRequest.builder()
                                                .type(dadosPagamento.cpfCnpj().length() > 11 ?
                                                        "CNPJ" : "CPF")
                                                .number(dadosPagamento.cpfCnpj())
                                                .build()
                                )
                                .build()
                )
                .build();
    }
}
