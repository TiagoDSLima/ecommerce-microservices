package com.ecommerce.pedido.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "produto", url = "${ecommerce.config.clients.produto.url}")
public interface ProdutoClient {
}
