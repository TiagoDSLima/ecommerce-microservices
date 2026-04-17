package com.ecommerce.pedido.client.representation;

import java.math.BigDecimal;
import java.util.List;

public record ProdutoRepresentation(Long id, String descricao, String urlImagem, BigDecimal valorUnitario, Integer quantidade, List<ProdutoVariacaoRepresentation> variacoes) {
}
