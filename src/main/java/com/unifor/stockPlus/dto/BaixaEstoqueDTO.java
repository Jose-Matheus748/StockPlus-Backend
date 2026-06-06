package com.unifor.stockPlus.dto;

import java.util.List;

public class BaixaEstoqueDTO {
    private Long protocoloId;
    private List<Long> protocoloIds;

    // atributo para não quebrar a aplicação, caso chegue apenas 1 protocoloId no formato antigo
    public Long getProtocoloId() {
        return protocoloId;
    }

    public void setProtocoloId(Long protocoloId) {
        this.protocoloId = protocoloId;
    }

    public List<Long> getProtocoloIds() {
        if (protocoloIds != null && !protocoloIds.isEmpty()) {
            return protocoloIds;
        }

        if (protocoloId != null) {
            return List.of(protocoloId);
        }

        return List.of();
    }

    public void setProtocoloIds(List<Long> protocoloIds) {
        this.protocoloIds = protocoloIds;
    }
}
