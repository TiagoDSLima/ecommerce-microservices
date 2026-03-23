package com.br.ecommerce.participante.repository;

import com.br.ecommerce.participante.model.Participante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParticipanteRepository extends JpaRepository<Participante, Long> {
    Optional<Participante> findParticipanteByIdUsuario(Long idUsuario);
}
