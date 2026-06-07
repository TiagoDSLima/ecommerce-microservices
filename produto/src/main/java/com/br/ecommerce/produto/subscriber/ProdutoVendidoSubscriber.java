package com.br.ecommerce.produto.subscriber;

import com.br.ecommerce.produto.dto.ProdutoVendidoRepresentation;
import com.br.ecommerce.produto.service.ProdutoService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProdutoVendidoSubscriber {

    private final ObjectMapper objectMapper;
    private final ProdutoService produtoService;

    @KafkaListener(groupId = "${ecommerce.config.kafka.group-id}", topics = "${ecommerce.config.kafka.topics.produtos-vendidos}")
    public void listen(String json){
        try {
            log.info("Recebendo produtos vendidos para baixa de estoque {}", json);
            List<ProdutoVendidoRepresentation> produtosVendidos = objectMapper.readValue(json, new TypeReference<List<ProdutoVendidoRepresentation>>() {});
            produtoService.abaterEstoque(produtosVendidos);
        } catch (Exception e) {
            log.error("Erro ao abater estoque dos produtos vendidos: ", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
