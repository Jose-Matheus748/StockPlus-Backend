package com.unifor.stockPlus.repository;

import com.unifor.stockPlus.entity.Loja;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LojaRepository extends JpaRepository<Loja, Long> {
    boolean existsByEmail(String email);
    boolean existsByCnpj(String cnpj);
    Optional<Loja> findByEmail(String email);
}