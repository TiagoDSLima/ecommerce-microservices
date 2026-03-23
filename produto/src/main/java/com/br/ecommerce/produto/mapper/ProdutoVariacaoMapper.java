package com.br.ecommerce.produto.mapper;

import com.br.ecommerce.produto.dto.ProdutoVariacaoDto;
import com.br.ecommerce.produto.model.ProdutoVariacao;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProdutoVariacaoMapper {

    ProdutoVariacaoDto map(ProdutoVariacao entity);

    ProdutoVariacao map(ProdutoVariacaoDto dto);
}