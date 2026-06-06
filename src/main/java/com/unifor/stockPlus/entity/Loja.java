package com.unifor.stockPlus.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "lojas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Loja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Column(unique = true)
    private String email;

    private String senha;

    @Column(unique = true)
    private String cnpj;

    @Column(columnDefinition = "LONGTEXT")
    private String fotoPerfil;
}