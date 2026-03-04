package com.br.ecommerce.auth.publisher;

import com.br.ecommerce.auth.publisher.representation.ParticipanteRepresentation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class UsuarioPublisher {

    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;
    @Value("${ecommerce.config.kafka.topics.usuarios-criados}")
    private String topico;

    public void publicar(ParticipanteRepresentation representation){
        try {
            var json = objectMapper.writeValueAsString(representation);
            kafkaTemplate.send(topico, json);
        } catch (JsonProcessingException e) {
            log.error("Erro ao processar JSON: ", e.getMessage());
        }
    }
}
