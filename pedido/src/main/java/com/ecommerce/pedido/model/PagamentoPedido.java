package com.ecommerce.pedido.model;

import com.ecommerce.pedido.enums.StatusPagamento;
import com.ecommerce.pedido.enums.TipoPagamento;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "pagamento_pedido")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PagamentoPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private StatusPagamento statusPagamento;
    private TipoPagamento tipoPagamento;
    private BigDecimal valorPagamento;
    @ManyToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

}

