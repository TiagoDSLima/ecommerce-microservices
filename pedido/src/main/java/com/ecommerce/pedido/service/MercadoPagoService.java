package com.ecommerce.pedido.service;

import com.ecommerce.pedido.client.LojaClient;
import com.ecommerce.pedido.client.representation.MercadoPagoTokenRepresentation;
import com.ecommerce.pedido.exception.exceptions.PagamentoException;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.payment.PaymentCreateRequest;
import com.mercadopago.core.MPRequestOptions;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Encapsula o SDK do Mercado Pago. O access token não fica no serviço de pedido:
 * é buscado no serviço Loja a cada chamada e enviado por requisição (MPRequestOptions),
 * sem alterar a configuração global/estática do SDK.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MercadoPagoService {

    private final PaymentClient paymentClient;
    private final LojaClient lojaClient;

    public Payment criarPagamento(PaymentCreateRequest request) {
        try {
            return paymentClient.create(request, requestOptions());
        } catch (MPApiException e) {
            log.error("Mercado Pago recusou a criação do pagamento. Status {}: {}", e.getStatusCode(), e.getApiResponse().getContent());
            throw new PagamentoException("pagamento", "Mercado Pago recusou o pagamento: " + e.getApiResponse().getContent(), e);
        } catch (MPException e) {
            throw new PagamentoException("pagamento", "Erro ao comunicar com o Mercado Pago", e);
        }
    }

    public Payment buscarPagamento(Long paymentId) {
        try {
            return paymentClient.get(paymentId, requestOptions());
        } catch (MPApiException e) {
            log.error("Erro ao buscar pagamento {} no Mercado Pago. Status {}: {}", paymentId, e.getStatusCode(), e.getApiResponse().getContent());
            throw new PagamentoException("pagamento", "Erro ao buscar pagamento " + paymentId + " no Mercado Pago", e);
        } catch (MPException e) {
            throw new PagamentoException("pagamento", "Erro ao comunicar com o Mercado Pago", e);
        }
    }

    private MPRequestOptions requestOptions() {
        return MPRequestOptions.builder()
                .accessToken(getAccessToken())
                .build();
    }

    private String getAccessToken() {
        MercadoPagoTokenRepresentation representation;
        try {
            representation = lojaClient.buscaToken();
        } catch (Exception e) {
            throw new PagamentoException("pagamento", "Não foi possível obter o token do Mercado Pago no serviço Loja", e);
        }

        if (representation == null || representation.token() == null || representation.token().isBlank()) {
            throw new PagamentoException("pagamento", "Token do Mercado Pago não configurado na Loja");
        }
        return representation.token();
    }
}
