package com.br.ecommerce.produto.service;

import com.br.ecommerce.produto.dto.ProdutoResponse;
import com.br.ecommerce.produto.dto.ProdutoVariacaoDto;
import com.br.ecommerce.produto.mapper.ProdutoMapper;
import com.br.ecommerce.produto.model.Produto;
import com.br.ecommerce.produto.repository.ProdutoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;
    @Mock
    private ProdutoMapper produtoMapper;

    @InjectMocks
    private ProdutoService produtoService;

    @Test
    void buscaTodosComEstoqueIncluiProdutoSemVariacoes() {
        Produto caneca = new Produto();
        when(produtoRepository.findProdutosComEstoqueDisponivel()).thenReturn(List.of(caneca));
        when(produtoMapper.map(caneca)).thenReturn(new ProdutoResponse(1L, "Caneca", null, new BigDecimal("39.90"), 50, List.of()));

        List<ProdutoResponse> produtos = produtoService.buscaTodosComEstoque();

        assertThat(produtos).extracting(ProdutoResponse::id).containsExactly(1L);
        assertThat(produtos.get(0).variacoes()).isEmpty();
    }

    @Test
    void buscaTodosComEstoqueRetornaApenasVariacoesComEstoque() {
        Produto camiseta = new Produto();
        when(produtoRepository.findProdutosComEstoqueDisponivel()).thenReturn(List.of(camiseta));
        when(produtoMapper.map(camiseta)).thenReturn(new ProdutoResponse(2L, "Camiseta", null, new BigDecimal("59.90"), 20, List.of(
                new ProdutoVariacaoDto(1L, "P", "Preta", 0),
                new ProdutoVariacaoDto(2L, "M", "Preta", 10),
                new ProdutoVariacaoDto(3L, "G", "Branca", 10))));

        List<ProdutoResponse> produtos = produtoService.buscaTodosComEstoque();

        assertThat(produtos.get(0).variacoes()).extracting(ProdutoVariacaoDto::id).containsExactly(2L, 3L);
    }
}
