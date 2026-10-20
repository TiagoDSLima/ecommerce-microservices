package com.ecommerce.pedido.dto;


import java.util.List;

public record PedidoRequest(
        List<ItemPedidoRequest> itensPedido,
        EnderecoDto endereco,
        PagamentoPedidoRequest pagamento
){
}
