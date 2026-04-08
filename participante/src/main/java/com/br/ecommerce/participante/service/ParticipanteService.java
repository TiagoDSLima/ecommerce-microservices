package com.br.ecommerce.participante.service;

import com.br.ecommerce.participante.dto.ParticipanteRequest;
import com.br.ecommerce.participante.dto.ParticipanteResponse;
import com.br.ecommerce.participante.exception.exceptions.ParticipanteNaoEcontradoExcpetion;
import com.br.ecommerce.participante.mapper.ParticipanteMapper;
import com.br.ecommerce.participante.model.Participante;
import com.br.ecommerce.participante.repository.ParticipanteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ParticipanteService {

    private final ParticipanteRepository participanteRepository;
    private final ParticipanteMapper participanteMapper;

    public ParticipanteResponse criaParticipante(ParticipanteRequest participanteRequest){
        Participante participante = participanteMapper.map(participanteRequest);
        participanteRepository.save(participante);

        return participanteMapper.map(participante);
    }

    public ParticipanteResponse buscaParticipantePorIdUsuario(Long idUsuario){
        Participante participante = participanteRepository.findParticipanteByIdUsuario(idUsuario)
                .orElseThrow(() -> new ParticipanteNaoEcontradoExcpetion("idUsuario"));

        return participanteMapper.map(participante);
    }

    public ParticipanteResponse buscaParticipantePorId(Long id){
        Participante participante = participanteRepository.findById(id)
                .orElseThrow(() -> new ParticipanteNaoEcontradoExcpetion("id"));

        return participanteMapper.map(participante);
    }

    public List<ParticipanteResponse> buscaTodosParticipantes(){
        List<Participante> participantes = participanteRepository.findAll();
        return participantes.stream()
                .map(participanteMapper::map)
                .toList();
    }

}
