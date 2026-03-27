package com.ecommerce.pedido.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "item_pedido")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String descricao;
    private BigDecimal valorUnitario;
    private Integer quantidade;
    private BigDecimal valorTotalProduto;
    private Long idProduto;
    @ManyToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;
}
