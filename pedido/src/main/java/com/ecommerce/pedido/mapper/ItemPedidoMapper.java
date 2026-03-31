package com.ecommerce.pedido.mapper;

import com.ecommerce.pedido.dto.ItemPedidoRequest;
import com.ecommerce.pedido.dto.ItemPedidoResponse;
import com.ecommerce.pedido.model.ItemPedido;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ItemPedidoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pedido", ignore = true)
    @Mapping(target = "valorTotalItem", expression = "java(request.valorTotalItem())")
    ItemPedido map(ItemPedidoRequest request);

    ItemPedidoResponse map(ItemPedido entity);
}
