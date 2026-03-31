package com.ecommerce.pedido.mapper;

import com.ecommerce.pedido.dto.PedidoRequest;
import com.ecommerce.pedido.dto.PedidoResponse;
import com.ecommerce.pedido.model.*;
import org.mapstruct.*;

import java.math.BigDecimal;

@Mapper(
        componentModel = "spring",
        uses = {
                ItemPedidoMapper.class,
                EnderecoMapper.class,
                PagamentoMapper.class
        }
)
public interface PedidoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "valorTotal", ignore = true)
    @Mapping(target = "itensPedido", source = "itensPedido")
    @Mapping(target = "enderecoPedido", source = "endereco")
    @Mapping(target = "pagamentoPedido", source = "pagamento")
    Pedido toEntity(PedidoRequest request);

    @Mapping(target = "nomeCliente", source = "nomeCliente")
    @Mapping(target = "cpf", source = "cpf")
    PedidoResponse toResponse(Pedido pedido, String nomeCliente, String cpf);

    @AfterMapping
    default void afterMapping(@MappingTarget Pedido pedido) {

        if (pedido.getItensPedido() != null) {
            pedido.getItensPedido().forEach(item -> item.setPedido(pedido));
        }

        if (pedido.getEnderecoPedido() != null) {
            pedido.getEnderecoPedido().setPedido(pedido);
        }

        if (pedido.getPagamentoPedido() != null) {
            pedido.getPagamentoPedido().setPedido(pedido);
        }

        BigDecimal total = pedido.getItensPedido()
                .stream()
                .map(ItemPedido::getValorTotalItem)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        pedido.setValorTotal(total);
    }
}
