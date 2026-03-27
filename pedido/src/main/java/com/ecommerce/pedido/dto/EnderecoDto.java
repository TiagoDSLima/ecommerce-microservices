package com.ecommerce.pedido.dto;

public record EnderecoDto(
        String cep,
        String rua,
        String bairro,
        String numero,
        String cidade,
        String uf
) {
}
