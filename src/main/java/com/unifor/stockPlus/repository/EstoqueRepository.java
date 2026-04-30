package com.unifor.stockPlus.repository;

import com.unifor.stockPlus.entity.Estoque;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EstoqueRepository extends JpaRepository<Estoque, Long> {

    List<Estoque> findByLojaId(Long lojaId);

    boolean existsByIdAndLojaId(Long estoqueId, Long lojaId);
}