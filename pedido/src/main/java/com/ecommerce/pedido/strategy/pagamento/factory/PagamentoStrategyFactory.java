package com.ecommerce.pedido.strategy.pagamento.factory;

import com.ecommerce.pedido.enums.TipoPagamento;
import com.ecommerce.pedido.strategy.pagamento.strategy.PagamentoStrategy;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PagamentoStrategyFactory {

    private final Map<String, PagamentoStrategy> strategies;

    public PagamentoStrategyFactory(Map<String, PagamentoStrategy> strategies) {
        this.strategies = strategies;
    }

    public PagamentoStrategy getStrategy(TipoPagamento tipo) {
        return switch (tipo) {
            case CREDITO -> strategies.get("creditoStrategy");
            case DEBITO -> strategies.get("debitoStrategy");
            case PIX -> strategies.get("pixStrategy");
        };
    }
}
