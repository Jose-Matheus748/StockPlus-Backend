package com.unifor.stockPlus.repository;

import com.unifor.stockPlus.entity.ItemProtocolo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemProtocoloRepository extends JpaRepository<ItemProtocolo, Long> {
    @Query("SELECT i FROM ItemProtocolo i JOIN FETCH i.produto WHERE i.protocolo.id = :protocoloId")
    List<ItemProtocolo> findByProtocoloIdWithProduto(@Param("protocoloId") Long protocoloId);
    void deleteByProtocoloId(Long protocoloId);
}
