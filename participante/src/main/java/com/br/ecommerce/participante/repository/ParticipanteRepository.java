package com.br.ecommerce.participante.repository;

import com.br.ecommerce.participante.model.Participante;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipanteRepository extends JpaRepository<Participante, Long> {
}
