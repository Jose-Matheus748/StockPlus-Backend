package com.unifor.stockPlus.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProtocoloDTO {

    private Long id;
    private String nome;
    private Double preco;
    private Double valorTotal;

    private Long lojaId;

    private List<ItemProtocoloDTO> itens;
}