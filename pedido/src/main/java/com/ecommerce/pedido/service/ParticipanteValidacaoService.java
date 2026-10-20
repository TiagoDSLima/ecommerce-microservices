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

    public Long buscaIdParticipante(Long idUsuario){
        ParticipanteRepresentation participante;
        try {
            participante = participanteClient.buscaParticipantePorIdUsuario(idUsuario);
        } catch (Exception exception) {
            participante = null;
        }
        if(participante == null) throw new ParticipanteNaoEncontradoException("idUsuario", String.format("Participante do usuário %d não encontrado!", idUsuario));
        return participante.id();
    }
}
