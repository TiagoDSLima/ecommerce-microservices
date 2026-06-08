package com.ecommerce.pedido.strategy.pagamento.factory;

import com.ecommerce.pedido.enums.TipoPagamento;
import com.ecommerce.pedido.strategy.pagamento.impl.CreditoStrategy;
import com.ecommerce.pedido.strategy.pagamento.impl.DebitoStrategy;
import com.ecommerce.pedido.strategy.pagamento.impl.PixStrategy;
import com.ecommerce.pedido.strategy.pagamento.strategy.PagamentoStrategy;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Teste unitario puro da fabrica de estrategias: garante que cada {@link TipoPagamento}
 * resolve para a implementacao correta. O mapa de estrategias eh injetado manualmente,
 * sem subir o contexto do Spring.
 */
class PagamentoStrategyFactoryTest {

    @Test
    void getStrategyDeveRetornarImplementacaoCorretaParaCadaTipo() {
        CreditoStrategy credito = new CreditoStrategy();
        DebitoStrategy debito = new DebitoStrategy();
        PixStrategy pix = new PixStrategy();

        Map<String, PagamentoStrategy> strategies = Map.of(
                "creditoStrategy", credito,
                "debitoStrategy", debito,
                "pixStrategy", pix
        );

        PagamentoStrategyFactory factory = new PagamentoStrategyFactory(strategies);

        assertThat(factory.getStrategy(TipoPagamento.CREDITO)).isSameAs(credito);
        assertThat(factory.getStrategy(TipoPagamento.DEBITO)).isSameAs(debito);
        assertThat(factory.getStrategy(TipoPagamento.PIX)).isSameAs(pix);
    }
}
