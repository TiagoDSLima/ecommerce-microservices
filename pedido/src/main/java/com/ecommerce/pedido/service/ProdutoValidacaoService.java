package com.ecommerce.pedido.service;

import com.ecommerce.pedido.client.ProdutoClient;
import com.ecommerce.pedido.client.representation.ProdutoRepresentation;
import com.ecommerce.pedido.dto.ItemPedidoRequest;
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
        List<Long> ids = itens.stream()
                .map(ItemPedidoRequest::idProduto)
                .distinct()
                .toList();

        Map<Long, ProdutoRepresentation> produtos = produtoClient.buscarPorIds(ids)
                .stream()
                .collect(Collectors.toMap(ProdutoRepresentation::id, p -> p));

        ids.forEach(id -> {
            if (!produtos.containsKey(id)) {
                throw new ProdutoNaoEncontradoException("idProduto", String.format("Produto %d não encontrado!", id));
            }
        });

        itens.forEach(item -> {
            if (item.idProdutoVariacao() == null) return;

            ProdutoRepresentation produto = produtos.get(item.idProduto());
            boolean variacaoExiste = produto.variacoes().stream()
                    .anyMatch(v -> v.id().equals(item.idProdutoVariacao()));

            if (!variacaoExiste) {
                throw new VariacaoProdutoNaoEncontradaException("idProdutoVariacao", String.format("Variação do produto %d não encontrada!", item.idProduto()));
            }
        });
    }
}
