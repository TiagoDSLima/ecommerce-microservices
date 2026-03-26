package com.br.ecommerce.produto.repository;

import com.br.ecommerce.produto.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    @Query("""
        SELECT DISTINCT p FROM Produto p
        JOIN FETCH p.variacoes v
        WHERE p.quantidade > 0
        AND v.quantidade > 0
    """)
    List<Produto> findProdutosComEstoqueDisponivel();
}
