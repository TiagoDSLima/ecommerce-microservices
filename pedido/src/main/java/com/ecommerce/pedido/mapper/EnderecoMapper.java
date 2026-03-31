package com.ecommerce.pedido.mapper;

import com.ecommerce.pedido.dto.EnderecoDto;
import com.ecommerce.pedido.model.EnderecoPedido;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface EnderecoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pedido", ignore = true)
    EnderecoPedido map(EnderecoDto dto);
}