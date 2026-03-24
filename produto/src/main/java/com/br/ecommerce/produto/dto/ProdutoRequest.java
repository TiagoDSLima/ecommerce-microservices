package com.br.ecommerce.produto.dto;

import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

public record ProdutoRequest(Long id, MultipartFile urlImagem,  String descricao, BigDecimal valorUnitario, Integer quantidade, List<ProdutoVariacaoDto> variacoes) {
}
