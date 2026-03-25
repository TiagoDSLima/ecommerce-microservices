package com.br.ecommerce.produto.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "produtoVariacao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoVariacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tamanho;
    private String cor;
    private Integer quantidade;
    @ManyToOne
    @JoinColumn(name = "produto_id")
    private Produto produto;
}
