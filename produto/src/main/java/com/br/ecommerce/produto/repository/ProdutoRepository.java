package com.br.ecommerce.produto.repository;

import com.br.ecommerce.produto.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    @Query("""
        SELECT DISTINCT p FROM Produto p
        LEFT JOIN FETCH p.variacoes
        WHERE p.quantidade > 0
        AND (
            NOT EXISTS (SELECT v FROM ProdutoVariacao v WHERE v.produto = p)
            OR EXISTS (SELECT v FROM ProdutoVariacao v WHERE v.produto = p AND v.quantidade > 0)
        )
    """)
    List<Produto> findProdutosComEstoqueDisponivel();
}
