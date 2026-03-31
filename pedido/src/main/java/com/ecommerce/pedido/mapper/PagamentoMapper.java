package com.ecommerce.pedido.mapper;

import com.ecommerce.pedido.dto.PagamentoPedidoRequest;
import com.ecommerce.pedido.dto.PagamentoResponse;
import com.ecommerce.pedido.enums.StatusPagamento;
import com.ecommerce.pedido.model.PagamentoPedido;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PagamentoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pedido", ignore = true)
    @Mapping(target = "valorPagamento", source = "valorAPagar")
    @Mapping(target = "statusPagamento", expression = "java(StatusPagamento.PENDENTE)")
    PagamentoPedido map(PagamentoPedidoRequest request);

    PagamentoResponse map(PagamentoPedido entity);
}
