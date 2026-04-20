package com.ecommerce.pedido.service;

import com.ecommerce.pedido.client.ProdutoClient;
import com.ecommerce.pedido.client.representation.ProdutoRepresentation;
import com.ecommerce.pedido.client.representation.ProdutoVariacaoRepresentation;
import com.ecommerce.pedido.dto.ItemPedidoRequest;
import com.ecommerce.pedido.exception.exceptions.EstoqueInsuficienteException;
import com.ecommerce.pedido.exception.exceptions.ProdutoNaoEncontradoException;
import com.ecommerce.pedido.exception.exceptions.VariacaoProdutoNaoEncontradaException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProdutoValidacaoService {

    private final ProdutoClient produtoClient;

    public void validar(List<ItemPedidoRequest> itens) {
        Map<Long, ProdutoRepresentation> produtos = buscarProdutos(itens);

        itens.forEach(item -> {
            ProdutoRepresentation produto = verificarProdutoExiste(item.idProduto(), produtos);

            if (item.idProdutoVariacao() != null) {
                ProdutoVariacaoRepresentation variacao = verificarVariacaoExiste(item.idProdutoVariacao(), produto);
                verificarEstoqueVariacao(item.quantidade(), variacao, produto.id());
            } else {
                verificarEstoqueProduto(item.quantidade(), produto);
            }
        });
    }

    private Map<Long, ProdutoRepresentation> buscarProdutos(List<ItemPedidoRequest> itens) {
        List<Long> ids = itens.stream()
                .map(ItemPedidoRequest::idProduto)
                .distinct()
                .toList();

        return produtoClient.buscarPorIds(ids)
                .stream()
                .collect(Collectors.toMap(ProdutoRepresentation::id, p -> p));
    }

    private ProdutoRepresentation verificarProdutoExiste(Long idProduto, Map<Long, ProdutoRepresentation> produtos) {
        ProdutoRepresentation produto = produtos.get(idProduto);
        if (produto == null) {
            throw new ProdutoNaoEncontradoException("idProduto", String.format("Produto código %s não encontrado", idProduto.toString()));
        }
        return produto;
    }

    private ProdutoVariacaoRepresentation verificarVariacaoExiste(Long idVariacao, ProdutoRepresentation produto) {
        return produto.variacoes().stream()
                .filter(v -> v.id().equals(idVariacao))
                .findFirst()
                .orElseThrow(() -> new VariacaoProdutoNaoEncontradaException("idProdutoVariacao", String.format("Variação informada não existe! Código %s", idVariacao.toString())));
    }

    private void verificarEstoqueProduto(int quantidadeSolicitada, ProdutoRepresentation produto) {
        if (quantidadeSolicitada > produto.quantidade()) {
            throw new EstoqueInsuficienteException("quantidade", String.format("Estoque insuficiente para o produto %s", produto.id().toString()));
        }
    }

    private void verificarEstoqueVariacao(int quantidadeSolicitada, ProdutoVariacaoRepresentation variacao, Long idProduto) {
        if (quantidadeSolicitada > variacao.quantidade()) {
            throw new EstoqueInsuficienteException("quantidade", String.format("Estoque insuficiente para o produto %s", idProduto.toString()));
        }
    }
}
