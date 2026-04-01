package com.ecommerce.pedido.service;

import com.ecommerce.pedido.dto.DadosPagamentoDto;
import com.ecommerce.pedido.strategy.pagamento.factory.PagamentoStrategyFactory;
import com.ecommerce.pedido.strategy.pagamento.strategy.PagamentoStrategy;
import com.mercadopago.client.payment.PaymentCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PagamentoService {

    private final PagamentoStrategyFactory factory;

    public void geraPagamento(DadosPagamentoDto dadosPagamento){

        PagamentoStrategy strategy = factory.getStrategy(dadosPagamento.tipo());
        PaymentCreateRequest request = strategy.criar(dadosPagamento);
    }
}
