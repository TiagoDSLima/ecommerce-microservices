package com.ecommerce.pedido.dto;


import java.util.List;

public record PedidoRequest(
        Long idParticipante,
        List<ItemPedidoRequest> itensPedido,
        EnderecoDto endereco,
        PagamentoPedidoRequest pagamento
){
}
