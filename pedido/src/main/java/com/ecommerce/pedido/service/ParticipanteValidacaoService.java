package com.ecommerce.pedido.service;

import com.ecommerce.pedido.client.ParticipanteClient;
import com.ecommerce.pedido.client.representation.ParticipanteRepresentation;
import com.ecommerce.pedido.exception.exceptions.ParticipanteNaoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ParticipanteValidacaoService {

    private final ParticipanteClient participanteClient;

    public void validar(Long idParticipante){
        try {
            ParticipanteRepresentation participante = participanteClient.buscaParticipantePorId(idParticipante);
            if(participante == null) throw new ParticipanteNaoEncontradoException("idParticipante", String.format("Participante %d não encontrado!", idParticipante));
        } catch (Exception exception) {
            throw new ParticipanteNaoEncontradoException("idParticipante", String.format("Participante %d não encontrado!", idParticipante));
        }
    }
}
