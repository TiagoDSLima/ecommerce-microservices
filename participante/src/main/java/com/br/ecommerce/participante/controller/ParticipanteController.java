package com.br.ecommerce.participante.controller;

import com.br.ecommerce.participante.dto.ParticipanteResponse;
import com.br.ecommerce.participante.service.ParticipanteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/participante")
@RequiredArgsConstructor
public class ParticipanteController {

    private final ParticipanteService participanteService;

    @GetMapping("usuario/{idUsuario}")
    public ResponseEntity<ParticipanteResponse> buscaParticipantePorIdUsuario(@PathVariable("idUsuario") Long idUsuario){
        ParticipanteResponse participanteResponse = participanteService.buscaParticipantePorIdUsuario(idUsuario);
        return ResponseEntity.ok(participanteResponse);
    }

    @GetMapping("{id}")
    public ResponseEntity<ParticipanteResponse> buscaParticipantePorId(@PathVariable("id") Long id){
        ParticipanteResponse participanteResponse = participanteService.buscaParticipantePorId(id);
        return ResponseEntity.ok(participanteResponse);
    }

    @GetMapping
    public ResponseEntity<List<ParticipanteResponse>> buscaTodosParticipantes(){
        List<ParticipanteResponse> participantesResponse = participanteService.buscaTodosParticipantes();
        return ResponseEntity.ok(participantesResponse);
    }
}
