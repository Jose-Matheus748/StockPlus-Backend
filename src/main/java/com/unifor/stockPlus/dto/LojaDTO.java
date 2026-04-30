package com.unifor.stockPlus.dto;

import com.unifor.stockPlus.entity.Loja;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LojaDTO {

    private Long id;
    private String nome;
    private String email;
    private String senha;
    private String cnpj;

    public static LojaDTO fromEntity(Loja loja) {
        if (loja == null) return null;

        LojaDTO dto = new LojaDTO();
        dto.setId(loja.getId());
        dto.setNome(loja.getNome());
        dto.setEmail(loja.getEmail());
        dto.setCnpj(loja.getCnpj());

        dto.setSenha(null);

        return dto;
    }

    public Loja toEntity() {
        Loja loja = new Loja();
        loja.setId(this.id);
        loja.setNome(this.nome);
        loja.setEmail(this.email);
        loja.setSenha(this.senha);
        loja.setCnpj(this.cnpj);
        return loja;
    }
}