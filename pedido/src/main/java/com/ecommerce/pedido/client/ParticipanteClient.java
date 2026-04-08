package com.ecommerce.pedido.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "participante", url = "${ecommerce.config.clients.participante.url}")
public interface ParticipanteClient {
}
