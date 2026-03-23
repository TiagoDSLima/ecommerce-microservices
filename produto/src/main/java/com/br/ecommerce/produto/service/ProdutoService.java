package com.br.ecommerce.produto.service;

import com.br.ecommerce.produto.dto.ProdutoDto;
import com.br.ecommerce.produto.mapper.ProdutoMapper;
import com.br.ecommerce.produto.model.Produto;
import com.br.ecommerce.produto.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final ProdutoMapper produtoMapper;

    public ProdutoDto criaProduto(ProdutoDto produtoDto){
        Produto produto = produtoRepository.save(produtoMapper.map(produtoDto));
        return produtoMapper.map(produto);
    }

    public List<ProdutoDto> buscaProdutos(){
        List<ProdutoDto> produtosDto = produtoRepository.findAll()
                .stream()
                .map(produtoMapper::map)
                .toList();

        return produtosDto;
    }
}
