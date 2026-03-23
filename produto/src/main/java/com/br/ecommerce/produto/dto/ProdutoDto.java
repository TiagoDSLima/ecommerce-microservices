package com.br.ecommerce.produto.dto;

import java.math.BigDecimal;
import java.util.List;

public record ProdutoDto(Long id, String descricao, BigDecimal valorUnitario, Integer quantidade, List<ProdutoVariacaoDto> variacoes) {
}
