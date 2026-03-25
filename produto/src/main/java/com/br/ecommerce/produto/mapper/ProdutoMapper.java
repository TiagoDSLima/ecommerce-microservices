package com.br.ecommerce.produto.mapper;

import com.br.ecommerce.produto.dto.ProdutoRequest;
import com.br.ecommerce.produto.dto.ProdutoResponse;
import com.br.ecommerce.produto.model.Produto;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = ProdutoVariacaoMapper.class)
public abstract class ProdutoMapper {

    public abstract ProdutoResponse map(Produto entity);

    public abstract Produto map(ProdutoRequest dto);

    @AfterMapping
    protected void linkVariacoes(@MappingTarget Produto produto) {
        if (produto.getVariacoes() != null) {
            produto.getVariacoes()
                    .forEach(v -> v.setProduto(produto));
        }
    }
}