package com.ecommerce.pedido.mapper;

import com.ecommerce.pedido.dto.ItemPedidoRequest;
import com.ecommerce.pedido.dto.PagamentoPedidoRequest;
import com.ecommerce.pedido.dto.PedidoRequest;
import com.ecommerce.pedido.enums.TipoPagamento;
import com.ecommerce.pedido.model.Pedido;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PedidoMapperTest {

    private PedidoMapper pedidoMapper() {
        PedidoMapperImpl mapper = new PedidoMapperImpl();
        ReflectionTestUtils.setField(mapper, "itemPedidoMapper", new ItemPedidoMapperImpl());
        ReflectionTestUtils.setField(mapper, "enderecoMapper", new EnderecoMapperImpl());
        ReflectionTestUtils.setField(mapper, "pagamentoMapper", new PagamentoMapperImpl());
        return mapper;
    }

    @Test
    void valorDoPagamentoDeveSerOTotalCalculadoENaoOEnviadoPeloCliente() {
        PedidoRequest request = new PedidoRequest(
                List.of(new ItemPedidoRequest("Camiseta", new BigDecimal("59.90"), 2, 1L, null),
                        new ItemPedidoRequest("Boné", new BigDecimal("30.00"), 1, 2L, null)),
                null,
                new PagamentoPedidoRequest(TipoPagamento.PIX, null, null, null, new BigDecimal("0.01"),
                        "cliente@email.com", "Maria", "Silva", "12345678901"));

        Pedido pedido = pedidoMapper().map(request);

        assertThat(pedido.getValorTotal()).isEqualByComparingTo("149.80");
        assertThat(pedido.getPagamentoPedido().getValorPagamento()).isEqualByComparingTo("149.80");
        assertThat(pedido.getPagamentoPedido().getPedido()).isSameAs(pedido);
    }
}
