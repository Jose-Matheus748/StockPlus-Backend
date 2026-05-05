package com.unifor.stockPlus.repository;

import com.unifor.stockPlus.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    @Query("SELECT p FROM Produto p JOIN p.estoques e WHERE e.id = :estoqueId")
    List<Produto> findByEstoqueId(@Param("estoqueId") Long estoqueId);
}