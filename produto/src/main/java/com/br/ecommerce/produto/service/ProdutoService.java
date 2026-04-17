package com.br.ecommerce.produto.service;

import com.br.ecommerce.produto.dto.BucketFile;
import com.br.ecommerce.produto.dto.ProdutoRequest;
import com.br.ecommerce.produto.dto.ProdutoResponse;
import com.br.ecommerce.produto.dto.ProdutoVariacaoDto;
import com.br.ecommerce.produto.exception.exceptions.EstoqueInvalidoException;
import com.br.ecommerce.produto.exception.exceptions.FalhaAoSalvarImagemException;
import com.br.ecommerce.produto.exception.exceptions.ProdutoNaoEcontradoException;
import com.br.ecommerce.produto.exception.exceptions.ProdutoUtilizadoException;
import com.br.ecommerce.produto.mapper.ProdutoMapper;
import com.br.ecommerce.produto.mapper.ProdutoVariacaoMapper;
import com.br.ecommerce.produto.model.Produto;
import com.br.ecommerce.produto.model.ProdutoVariacao;
import com.br.ecommerce.produto.repository.ProdutoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final ProdutoMapper produtoMapper;
    private final ProdutoVariacaoMapper produtoVariacaoMapper;
    private final BucketService bucketService;

    public ProdutoResponse criaProduto(ProdutoRequest produtoRequest){
        verificaEstoqueProdutoXVariacao(produtoRequest);
        Produto produto = produtoRepository.save(produtoMapper.map(produtoRequest));
        try{
            var file = new BucketFile(bucketService.retornaNomeProduto(produto.getId()), produtoRequest.imagem().getInputStream(), produtoRequest.imagem().getContentType(), produtoRequest.imagem().getSize());
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

    @Transactional
    public ProdutoResponse alteraProduto(ProdutoRequest produtoRequest){
        verificaEstoqueProdutoXVariacao(produtoRequest);
        Produto produto = produtoRepository.findById(produtoRequest.id())
                .orElseThrow(() -> new ProdutoNaoEcontradoException("id", String.format("Produto não encontrado com o código informado! Código: %s", produtoRequest.id().toString())));

        if(produtoRequest.imagem() != null){
            if(!verificaSeArquivoEImagem(produtoRequest.imagem())) throw new FalhaAoSalvarImagemException("imagem", "Falha ao cadastrar produto: imagem inválida!");
            try{
                bucketService.delete(produto.getId());
                var file = new BucketFile(bucketService.retornaNomeProduto(produto.getId()), produtoRequest.imagem().getInputStream(), produtoRequest.imagem().getContentType(), produtoRequest.imagem().getSize());
                produto.setUrlImagem(bucketService.getUrl(produto.getId()));
                bucketService.upload(file);
            } catch (Exception e){
                throw new FalhaAoSalvarImagemException("imagem", "Falha ao cadastrar produto: imagem inválida!");
            }
        }
        if(produtoRequest.descricao() != null) {
            produto.setDescricao(produtoRequest.descricao());
        }
        if(produtoRequest.quantidade() != null) {
            produto.setQuantidade(produtoRequest.quantidade());
        }
        if(produtoRequest.valorUnitario() != null) {
            produto.setValorUnitario(produtoRequest.valorUnitario());
        }
        if(produtoRequest.variacoes() != null) {
            produto.getVariacoes().clear();
            List<ProdutoVariacao> variacoes = produtoRequest.variacoes()
                    .stream().map(produtoVariacaoMapper::map).toList();
            variacoes.forEach(variacao -> {
                variacao.setProduto(produto);
                produto.getVariacoes().add(variacao);
            });
        }

        return produtoMapper.map(produto);
    }

    public void deletaProduto(Long id){
        if(!verificaSeProdutoExiste(id)) throw new ProdutoNaoEcontradoException("id", String.format("Produto do código %s não encontrado!", id.toString()));
        if(verificaProdutoUtilizado(id)) throw new ProdutoUtilizadoException("id", String.format("Produto %s já está utilizado, não pode ser excluso!", id.toString()));
        produtoRepository.deleteById(id);
    }

    public List<ProdutoResponse> buscaProdutosPorIds(List<Long> ids){
        List<Produto> produtos = produtoRepository.findAllById(ids);

        return produtos
                .stream()
                .map(produtoMapper::map)
                .toList();
    }

    public List<ProdutoResponse> buscaTodosComEstoque(){
        List<Produto> produtosComEstoquePositivo = produtoRepository.findProdutosComEstoqueDisponivel();

        return produtosComEstoquePositivo
                .stream()
                .map(produtoMapper::map)
                .toList();
    }

    private boolean verificaProdutoUtilizado(Long id){
        boolean produtoUtilizado = false;
        //Enviar para o serviço de pedidos uma requisição procurando se o produto está sendo utilizando
        return produtoUtilizado;
    }

    private boolean verificaSeProdutoExiste(Long id){
        return produtoRepository.existsById(id);
    }

    private boolean verificaSeArquivoEImagem(MultipartFile arquivo){
        if (arquivo != null && arquivo.getContentType().startsWith("image/")) {
            return true;
        }

        return false;
    }

    private void verificaEstoqueProdutoXVariacao(ProdutoRequest produtoRequest){
        Integer qtdTotalVariacao = produtoRequest.variacoes().stream()
                .mapToInt(ProdutoVariacaoDto::quantidade)
                .sum();

        if(produtoRequest.quantidade() < qtdTotalVariacao){
            throw new EstoqueInvalidoException("quantidade", "Quantidade informada para as variações é maior que o estoque total do produto!");
        }
    }
}
