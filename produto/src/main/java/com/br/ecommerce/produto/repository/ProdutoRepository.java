package com.br.ecommerce.produto.repository;

import com.br.ecommerce.produto.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
