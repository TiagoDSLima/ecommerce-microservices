package com.ecommerce.pedido.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "endereco_pedido")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EnderecoPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cep;
    private String rua;
    private String bairro;
    private String numero;
    private String cidade;
    private String uf;
    @OneToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;
}
