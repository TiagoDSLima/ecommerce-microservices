package com.ecommerce.pedido.client;

import com.ecommerce.pedido.client.config.ServiceTokenFeignConfig;
import com.ecommerce.pedido.client.representation.MercadoPagoTokenRepresentation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "loja", url = "${ecommerce.config.clients.loja.url}", configuration = ServiceTokenFeignConfig.class)
public interface LojaClient {

    @GetMapping("/mercadopago-token")
    MercadoPagoTokenRepresentation buscaToken();
}
