package com.ecommerce.pedido.client;

import com.ecommerce.pedido.client.representation.ProdutoRepresentation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "produto", url = "${ecommerce.config.clients.produto.url}")
public interface ProdutoClient {

    @GetMapping("/buscaIds")
    List<ProdutoRepresentation> buscarPorIds(@RequestParam("ids") List<Long> ids);
}
