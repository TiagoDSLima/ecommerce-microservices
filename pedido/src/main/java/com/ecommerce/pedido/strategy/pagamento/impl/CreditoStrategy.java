package com.ecommerce.pedido.strategy.pagamento.impl;

import com.ecommerce.pedido.dto.DadosPagamentoDto;
import com.ecommerce.pedido.strategy.pagamento.strategy.PagamentoStrategy;
import com.mercadopago.client.common.IdentificationRequest;
import com.mercadopago.client.payment.PaymentCreateRequest;
import com.mercadopago.client.payment.PaymentPayerRequest;
import org.springframework.stereotype.Component;

@Component
public class CreditoStrategy implements PagamentoStrategy {

    @Override
    public PaymentCreateRequest criar(DadosPagamentoDto dadosPagamento) {
        return PaymentCreateRequest.builder()
                .transactionAmount(dadosPagamento.valor())
                .token(dadosPagamento.token())
                .description("Pedido #" + dadosPagamento.pedidoId())
                .installments(dadosPagamento.parcelas())
                .paymentMethodId(dadosPagamento.bandeira().name().toLowerCase())
                .externalReference(String.valueOf(dadosPagamento.pedidoId()))
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
