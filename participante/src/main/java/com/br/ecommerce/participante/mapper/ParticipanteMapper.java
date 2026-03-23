package com.br.ecommerce.participante.mapper;

import com.br.ecommerce.participante.dto.ParticipanteRequest;
import com.br.ecommerce.participante.dto.ParticipanteResponse;
import com.br.ecommerce.participante.model.Participante;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ParticipanteMapper {

    ParticipanteResponse map(Participante participante);
    Participante map(ParticipanteRequest participanteRequest);
}
