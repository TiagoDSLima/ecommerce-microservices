package com.ecommerce.pedido.service;

import com.ecommerce.pedido.client.representation.ProdutoRepresentation;
import com.ecommerce.pedido.dto.*;
import com.ecommerce.pedido.enums.StatusPagamento;
import com.ecommerce.pedido.enums.TipoPagamento;
import com.ecommerce.pedido.mapper.PedidoMapper;
import com.ecommerce.pedido.model.PagamentoPedido;
import com.ecommerce.pedido.model.Pedido;
import com.ecommerce.pedido.repository.PedidoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Garante que o pedido é montado com o preço do catálogo (serviço Produto),
 * e não com o valor enviado pelo cliente.
 */
@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private PedidoMapper pedidoMapper;
    @Mock
    private PedidoRepository pedidoRepository;
    @Mock
    private PagamentoService pagamentoService;
    @Mock
    private ProdutoValidacaoService produtoValidacaoService;
    @Mock
    private ParticipanteValidacaoService participanteValidacaoService;

    @InjectMocks
    private PedidoService pedidoService;

    @Test
    void criaPedidoDeveUsarPrecoEDescricaoDoCatalogo() {
        ItemPedidoRequest itemDoCliente = new ItemPedidoRequest("Qualquer coisa", new BigDecimal("0.01"), 2, 1L, null);
        PagamentoPedidoRequest pagamento = new PagamentoPedidoRequest(TipoPagamento.PIX, null, null, null,
                new BigDecimal("0.01"), "cliente@email.com", "Maria", "Silva", "12345678901");
        PedidoRequest request = new PedidoRequest(List.of(itemDoCliente), null, pagamento);

        ProdutoRepresentation produto = new ProdutoRepresentation(1L, "Camiseta", null, new BigDecimal("59.90"), 10, List.of());
        when(produtoValidacaoService.validar(request.itensPedido())).thenReturn(Map.of(1L, produto));
        when(participanteValidacaoService.buscaIdParticipante(7L)).thenReturn(3L);

        Pedido pedido = new Pedido();
        PagamentoPedido pagamentoPedido = new PagamentoPedido();
        pagamentoPedido.setTipoPagamento(TipoPagamento.PIX);
        pedido.setPagamentoPedido(pagamentoPedido);
        when(pedidoMapper.map(any(PedidoRequest.class))).thenReturn(pedido);
        when(pagamentoService.geraPagamento(any())).thenReturn(new PagamentoResponse(1L, TipoPagamento.PIX, null,
                StatusPagamento.PENDENTE, null, new BigDecimal("119.80"), "qr", "qr64"));

        pedidoService.criaPedido(request, 7L);

        ArgumentCaptor<PedidoRequest> captor = ArgumentCaptor.forClass(PedidoRequest.class);
        verify(pedidoMapper).map(captor.capture());
        ItemPedidoRequest itemMapeado = captor.getValue().itensPedido().get(0);

        assertThat(itemMapeado.valorUnitario()).isEqualByComparingTo("59.90");
        assertThat(itemMapeado.descricao()).isEqualTo("Camiseta");
        assertThat(itemMapeado.quantidade()).isEqualTo(2);
        assertThat(itemMapeado.valorTotalItem()).isEqualByComparingTo("119.80");
        assertThat(pedido.getIdParticipante()).isEqualTo(3L);
    }
}
