package com.br.ecommerce.produto.service;

import com.br.ecommerce.produto.dto.BucketFile;
import com.br.ecommerce.produto.dto.ProdutoRequest;
import com.br.ecommerce.produto.dto.ProdutoResponse;
import com.br.ecommerce.produto.exception.exceptions.FalhaAoSalvarImagemException;
import com.br.ecommerce.produto.exception.exceptions.ProdutoNaoEcontradoException;
import com.br.ecommerce.produto.mapper.ProdutoMapper;
import com.br.ecommerce.produto.model.Produto;
import com.br.ecommerce.produto.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final ProdutoMapper produtoMapper;
    private final BucketService bucketService;

    public ProdutoResponse criaProduto(ProdutoRequest produtoRequest){
        Produto produto = produtoRepository.save(produtoMapper.map(produtoRequest));
        try{
            var file = new BucketFile(bucketService.retornaNomeProduto(produto.getId()), produtoRequest.imagem().getInputStream(), MediaType.APPLICATION_PDF, produtoRequest.imagem().getSize());
            bucketService.upload(file);
        } catch(Exception e){
            produtoRepository.delete(produto);
            throw new FalhaAoSalvarImagemException("imagem", "Falha ao cadastrar produto: imagem inválida!");
        }
        produto.setUrlImagem(bucketService.getUrl(produto.getId()));
        return produtoMapper.map(produto);
    }

    public List<ProdutoResponse> buscaProdutos(){
        List<Produto> produtos = produtoRepository.findAll();

        produtos.forEach(produto ->
                produto.setUrlImagem(bucketService.getUrl(produto.getId()))
        );

        return produtos.stream()
                .map(produtoMapper::map)
                .toList();
    }

    public ProdutoResponse buscaProduto(Long id){
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ProdutoNaoEcontradoException("id", String.format("Produto não encontrado com o código informado! Código: %s", id.toString())));

        produto.setUrlImagem(bucketService.getUrl(produto.getId()));

        return produtoMapper.map(produto);
    }
}
