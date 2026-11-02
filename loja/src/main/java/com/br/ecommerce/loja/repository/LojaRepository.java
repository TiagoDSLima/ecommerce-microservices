package com.br.ecommerce.loja.repository;

import com.br.ecommerce.loja.model.Loja;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LojaRepository extends JpaRepository<Loja, String> {
    Loja findByid(String id);
}
