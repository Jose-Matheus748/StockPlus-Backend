package com.unifor.stockPlus.dto;

import com.unifor.stockPlus.entity.Estoque;
import com.unifor.stockPlus.entity.Loja;
import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstoqueDTO {

    private Long id;
    private String nome;
    private String descricao;

    private Long lojaId;

    private List<ProdutoDTO> produtos;

    public static EstoqueDTO fromEntity(Estoque estoque) {
        EstoqueDTO dto = new EstoqueDTO();
        dto.setId(estoque.getId());
        dto.setNome(estoque.getNome());
        dto.setDescricao(estoque.getDescricao());

        dto.setLojaId(estoque.getLoja() != null ? estoque.getLoja().getId() : null);

        if (estoque.getProdutos() != null) {
            dto.setProdutos(
                    estoque.getProdutos().stream()
                            .map(ProdutoDTO::fromEntity)
                            .toList()
            );
        }

        return dto;
    }

    public Estoque toEntity(Loja loja) {
        Estoque estoque = new Estoque();
        estoque.setId(this.id);
        estoque.setNome(this.nome);
        estoque.setDescricao(this.descricao);
        estoque.setLoja(loja);
        return estoque;
    }
}