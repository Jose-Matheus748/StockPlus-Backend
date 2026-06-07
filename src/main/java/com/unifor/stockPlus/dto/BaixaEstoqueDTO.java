package com.unifor.stockPlus.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class BaixaEstoqueDTO {
    private List<Long> protocoloIds;
}