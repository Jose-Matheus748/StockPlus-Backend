package com.unifor.stockPlus.dto;

public record ValorTotalEstoqueDTO(
        Double valorTotal,
        Integer quantidadeTotal
) {

    public static ValorTotalEstoqueDTO vazio() {
        return new ValorTotalEstoqueDTO(0.0, 0);
    }
}