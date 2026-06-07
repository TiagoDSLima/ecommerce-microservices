package com.ecommerce.pedido.repository;

import com.ecommerce.pedido.model.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {

    List<ItemPedido> findByPedido_Id(Long idPedido);
}
