package com.unifor.stockPlus.repository;

import com.unifor.stockPlus.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    List<Produto> findByEstoqueId(Long estoqueId);
}