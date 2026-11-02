package com.ecommerce.pedido.service;

import com.ecommerce.pedido.client.ProdutoClient;
import com.ecommerce.pedido.client.representation.ProdutoRepresentation;
import com.ecommerce.pedido.dto.ItemPedidoRequest;
import com.ecommerce.pedido.exception.exceptions.ItemPedidoInvalidoException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProdutoValidacaoServiceTest {

    @Mock
    private ProdutoClient produtoClient;

    @InjectMocks
    private ProdutoValidacaoService produtoValidacaoService;

    @Test
    void validarDeveRetornarProdutosDoCatalogo() {
        ProdutoRepresentation produto = new ProdutoRepresentation(1L, "Camiseta", null, new BigDecimal("59.90"), 10, List.of());
        when(produtoClient.buscarPorIds(List.of(1L))).thenReturn(List.of(produto));

        Map<Long, ProdutoRepresentation> produtos = produtoValidacaoService.validar(
                List.of(new ItemPedidoRequest(null, null, 2, 1L, null)));

        assertThat(produtos).containsEntry(1L, produto);
    }

    @Test
    void quantidadeNegativaOuZeroDeveSerRejeitada() {
        assertThatThrownBy(() -> produtoValidacaoService.validar(List.of(new ItemPedidoRequest(null, null, -5, 1L, null))))
                .isInstanceOf(ItemPedidoInvalidoException.class);
        assertThatThrownBy(() -> produtoValidacaoService.validar(List.of(new ItemPedidoRequest(null, null, 0, 1L, null))))
                .isInstanceOf(ItemPedidoInvalidoException.class);
        verifyNoInteractions(produtoClient);
    }

    @Test
    void pedidoSemItensDeveSerRejeitado() {
        assertThatThrownBy(() -> produtoValidacaoService.validar(List.of()))
                .isInstanceOf(ItemPedidoInvalidoException.class);
    }
}
