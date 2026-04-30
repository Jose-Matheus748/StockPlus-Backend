package com.unifor.stockPlus.repository;

import com.unifor.stockPlus.entity.Protocolo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProtocoloRepository extends JpaRepository<Protocolo, Long> {

    List<Protocolo> findByLojaId(Long lojaId);
}