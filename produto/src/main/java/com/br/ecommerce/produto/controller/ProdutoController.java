package com.br.ecommerce.produto.controller;


import com.br.ecommerce.produto.dto.ProdutoRequest;
import com.br.ecommerce.produto.dto.ProdutoResponse;
import com.br.ecommerce.produto.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produto")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService produtoService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProdutoResponse> criarProduto(@ModelAttribute ProdutoRequest produtoRequest){
        ProdutoResponse produtoResponse = produtoService.criaProduto(produtoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoResponse);
    }

    @GetMapping(value = "/buscaTodos")
    public ResponseEntity<List<ProdutoResponse>> buscaTodos(){
        return ResponseEntity.status(HttpStatus.OK).body(produtoService.buscaProdutos());
    }

    @GetMapping(value = "/busca/{idProduto}")
    public ResponseEntity<ProdutoResponse> buscar(@PathVariable("idProduto") Long idProduto){
        return ResponseEntity.status(HttpStatus.OK).body(produtoService.buscaProduto(idProduto));
    }

    @GetMapping("/buscaIds")
    public ResponseEntity<List<ProdutoResponse>> buscarPorIds(@RequestParam List<Long> ids) {
        return ResponseEntity.status(HttpStatus.OK).body(produtoService.buscaProdutosPorIds(ids));
}

        @GetMapping(value = "/buscaTodosComEstoque")
    public ResponseEntity<List<ProdutoResponse>> buscaTodosComEstoque(){
        return ResponseEntity.status(HttpStatus.OK).body(produtoService.buscaTodosComEstoque());
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProdutoResponse> alteraProduto(@ModelAttribute ProdutoRequest produtoRequest){
        return ResponseEntity.status(HttpStatus.OK).body(produtoService.alteraProduto(produtoRequest));
    }

    @DeleteMapping(value = "/{idProduto}")
    public ResponseEntity<Void> deletaProduto(@PathVariable("idProduto") Long idProduto){
        produtoService.deletaProduto(idProduto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
