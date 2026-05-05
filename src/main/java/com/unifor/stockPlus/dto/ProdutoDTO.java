package com.unifor.stockPlus.dto;

import com.unifor.stockPlus.entity.Produto;
import com.unifor.stockPlus.entity.Estoque;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ProdutoDTO {

    private Long id;
    private String nome;
    private String descricao;
    private String fornecedor;
    private String marca;
    private Integer quantidade;
    private Double precoUnitario;
    private List<Long> estoqueIds;

    public static ProdutoDTO fromEntity(Produto produto) {
        if (produto == null) return null;

        ProdutoDTO dto = new ProdutoDTO();
        dto.setId(produto.getId());
        dto.setNome(produto.getNome());
        dto.setDescricao(produto.getDescricao());
        dto.setFornecedor(produto.getFornecedor());
        dto.setMarca(produto.getMarca());
        dto.setQuantidade(produto.getQuantidade());
        dto.setPrecoUnitario(produto.getPrecoUnitario());
        dto.setEstoqueIds(
                produto.getEstoques().stream()
                        .map(Estoque::getId)
                        .toList()
        );
        return dto;
    }

    public Produto toEntity(List<Estoque> estoques) {
        Produto produto = new Produto();
        produto.setId(this.id);
        produto.setNome(this.nome);
        produto.setDescricao(this.descricao);
        produto.setFornecedor(this.fornecedor);
        produto.setMarca(this.marca);
        produto.setQuantidade(this.quantidade);
        produto.setPrecoUnitario(this.precoUnitario);
        produto.setEstoques(new ArrayList<>(estoques)); // ← cópia mutável
        return produto;
    }
}