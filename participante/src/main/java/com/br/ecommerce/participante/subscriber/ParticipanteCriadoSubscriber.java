package com.br.ecommerce.participante.subscriber;


import com.br.ecommerce.participante.dto.ParticipanteRequest;
import com.br.ecommerce.participante.service.ParticipanteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ParticipanteCriadoSubscriber {

    private final ObjectMapper objectMapper;
    private final ParticipanteService participanteService;

    @KafkaListener(groupId = "${ecommerce.config.kafka.group-id}", topics = "${ecommerce.config.kafka.topics.usuarios-criados}")
    public void listen(String json){
        try{
            log.info("Recebendo participante {}", json);
            var representation = objectMapper.readValue(json, ParticipanteRequest.class);
            ParticipanteRequest participanteRequest = new ParticipanteRequest(representation.nome(), representation.cpf(), representation.dataNascimento(), representation.idUsuario());
            participanteService.criaParticipante(participanteRequest);
        }catch (Exception e){
            log.error("Erro ao criar participante: ", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
