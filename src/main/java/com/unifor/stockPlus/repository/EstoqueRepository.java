package com.unifor.stockPlus.repository;

import com.unifor.stockPlus.entity.Estoque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EstoqueRepository extends JpaRepository<Estoque, Long> {

    List<Estoque> findByLojaId(Long lojaId);

    boolean existsByIdAndLojaId(Long estoqueId, Long lojaId);

    @Query("SELECT e FROM Estoque e JOIN FETCH e.loja WHERE e.id = :id")
    Optional<Estoque> findByIdWithLoja(@Param("id") Long id);
}