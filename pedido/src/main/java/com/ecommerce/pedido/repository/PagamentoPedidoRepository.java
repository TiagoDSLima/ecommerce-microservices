package com.ecommerce.pedido.repository;

import com.ecommerce.pedido.model.PagamentoPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PagamentoPedidoRepository extends JpaRepository<PagamentoPedido, Long> {

    PagamentoPedido findByPedido_Id(Long idPedido);

    Optional<PagamentoPedido> findByMercadoPagoId(Long id);
}
