package com.br.ecommerce.participante.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "participante")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Participante {
    @Id
    private Long id;
    private String nome;
    private String cpf;
    private LocalDate dataNascimento;
    private Long idUsuario;
}
