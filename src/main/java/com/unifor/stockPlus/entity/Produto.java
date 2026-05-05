package com.unifor.stockPlus.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "produtos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String descricao;
    private String fornecedor;
    private String marca;

    private int quantidade;
    private Double precoUnitario;

    @ManyToMany
    @JoinTable(
            name = "produto_estoque",
            joinColumns = @JoinColumn(name = "produto_id"),
            inverseJoinColumns = @JoinColumn(name = "estoque_id")
    )
    private List<Estoque> estoques = new ArrayList<>();
}