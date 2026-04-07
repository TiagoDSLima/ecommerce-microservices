package com.ecommerce.pedido.publisher;

import com.ecommerce.pedido.publisher.representation.ProdutoRepresentation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class ProdutoPublisher {

    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;
    @Value("${ecommerce.config.kafka.topics.produtos-vendidos}")
    private String topico;

    public void publicar(List<ProdutoRepresentation> representations){
        try {
            var json = objectMapper.writeValueAsString(representations);
            kafkaTemplate.send(topico, json);
        } catch (JsonProcessingException e) {
            log.error("Erro ao processar JSON: ", e.getMessage());
        }
    }
}
