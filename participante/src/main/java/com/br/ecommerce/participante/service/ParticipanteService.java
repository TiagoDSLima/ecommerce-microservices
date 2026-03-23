package com.br.ecommerce.participante.service;

import com.br.ecommerce.participante.dto.ParticipanteRequest;
import com.br.ecommerce.participante.dto.ParticipanteResponse;
import com.br.ecommerce.participante.model.Participante;
import com.br.ecommerce.participante.repository.ParticipanteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ParticipanteService {

    private final ParticipanteRepository participanteRepository;

    public ParticipanteResponse criaParticipante(ParticipanteRequest participanteRequest){
        Participante participante = Participante.builder()
                .nome(participanteRequest.nome())
                .cpf(participanteRequest.cpf())
                .dataNascimento(participanteRequest.dataNascimento())
                .idUsuario(participanteRequest.idUsuario())
                .build();

        participanteRepository.save(participante);

        return new ParticipanteResponse(participante.getId(), participante.getNome(), participante.getCpf(), participante.getDataNascimento(), participante.getIdUsuario());
    }

}
