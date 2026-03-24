package com.br.ecommerce.produto.service;

import com.br.ecommerce.produto.dto.ProdutoRequest;
import com.br.ecommerce.produto.dto.ProdutoResponse;
import com.br.ecommerce.produto.exception.exceptions.ProdutoNaoEcontradoException;
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

    public ProdutoResponse criaProduto(ProdutoRequest produtoRequest){
        Produto produto = produtoRepository.save(produtoMapper.map(produtoRequest));
        return produtoMapper.map(produto);
    }

    public List<ProdutoResponse> buscaProdutos(){
        List<ProdutoResponse> produtosResponse = produtoRepository.findAll()
                .stream()
                .map(produtoMapper::map)
                .toList();

        return produtosResponse;
    }

    public ProdutoResponse buscaProduto(Long id){
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ProdutoNaoEcontradoException("Produto não encontrado com o código informado!", id.toString()));

        return produtoMapper.map(produto);
    }
}
