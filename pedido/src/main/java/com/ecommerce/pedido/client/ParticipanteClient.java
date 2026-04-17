package com.ecommerce.pedido.client;

import com.ecommerce.pedido.client.representation.ParticipanteRepresentation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "participante", url = "${ecommerce.config.clients.participante.url}")
public interface ParticipanteClient {

    @GetMapping("{id}")
    ParticipanteRepresentation buscaParticipantePorId(@PathVariable("id") Long id);
}
