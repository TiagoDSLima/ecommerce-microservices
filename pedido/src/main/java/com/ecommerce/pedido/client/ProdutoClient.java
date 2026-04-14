package com.ecommerce.pedido.client;

import com.ecommerce.pedido.dto.ProdutoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "produto", url = "${ecommerce.config.clients.produto.url}")
public interface ProdutoClient {

    @GetMapping(value = "/busca/{idProduto}")
    ProdutoResponse buscar(@PathVariable("idProduto") Long idProduto);
}
