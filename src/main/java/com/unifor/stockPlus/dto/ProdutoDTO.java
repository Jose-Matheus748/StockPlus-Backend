package com.unifor.stockPlus.dto;

import com.unifor.stockPlus.entity.Produto;
import com.unifor.stockPlus.entity.Estoque;
import lombok.*;

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

    private Long estoqueId;

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

        if (produto.getEstoque() != null) {
            dto.setEstoqueId(produto.getEstoque().getId());
        }

        return dto;
    }

    public Produto toEntity(Estoque estoque) {
        Produto produto = new Produto();
        produto.setId(this.id);
        produto.setNome(this.nome);
        produto.setDescricao(this.descricao);
        produto.setFornecedor(this.fornecedor);
        produto.setMarca(this.marca);
        produto.setQuantidade(this.quantidade);
        produto.setPrecoUnitario(this.precoUnitario);
        produto.setEstoque(estoque);
        return produto;
    }
}