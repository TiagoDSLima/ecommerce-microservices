package com.br.ecommerce.produto.dto;

import java.math.BigDecimal;
import java.util.List;

public record ProdutoResponse(Long id, String descricao, String urlImagem, BigDecimal valorUnitario, Integer quantidade, List<ProdutoVariacaoDto> variacoes) {
}
