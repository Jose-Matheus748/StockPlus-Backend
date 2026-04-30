package com.unifor.stockPlus.controller;

import com.unifor.stockPlus.dto.ProdutoDTO;
import com.unifor.stockPlus.entity.Loja;
import com.unifor.stockPlus.service.LojaService;
import com.unifor.stockPlus.service.ProdutoService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;
    private final LojaService lojaService;

    public ProdutoController(ProdutoService produtoService, LojaService lojaService) {
        this.produtoService = produtoService;
        this.lojaService = lojaService;
    }

    @PostMapping
    public ResponseEntity<ProdutoDTO> create(
            @RequestBody ProdutoDTO dto,
            @RequestParam Long lojaId
    ) {
        Loja loja = lojaService.getEntityById(lojaId);

        ProdutoDTO novo = produtoService.create(dto, loja);
        return ResponseEntity.status(HttpStatus.CREATED).body(novo);
    }

    @GetMapping
    public List<ProdutoDTO> getAll() {
        return produtoService.getAll();
    }

    @GetMapping("/{id}")
    public ProdutoDTO get(@PathVariable Long id) {
        return produtoService.getById(id);
    }

    @PutMapping("/{id}")
    public ProdutoDTO update(
            @PathVariable Long id,
            @RequestBody ProdutoDTO dto,
            @RequestParam Long lojaId
    ) {
        Loja loja = lojaService.getEntityById(lojaId);

        return produtoService.update(id, dto, loja);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @RequestParam Long lojaId
    ) {
        Loja loja = lojaService.getEntityById(lojaId);

        produtoService.delete(id, loja);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/add")
    public ProdutoDTO add(
            @PathVariable Long id,
            @RequestParam int quantidade,
            @RequestParam Long lojaId
    ) {
        Loja loja = lojaService.getEntityById(lojaId);

        return produtoService.addQuantity(id, quantidade, loja);
    }

    @PostMapping("/{id}/remove")
    public ProdutoDTO remove(
            @PathVariable Long id,
            @RequestParam int quantidade,
            @RequestParam Long lojaId
    ) {
        Loja loja = lojaService.getEntityById(lojaId);

        return produtoService.removeQuantity(id, quantidade, loja);
    }

    @GetMapping("/estoque/{estoqueId}")
    public List<ProdutoDTO> listarPorEstoque(@PathVariable Long estoqueId) {
        return produtoService.listarPorEstoque(estoqueId);
    }

    @GetMapping("/estoque/{estoqueId}/valor-total")
    public Double valorTotal(@PathVariable Long estoqueId) {
        return produtoService.calcularValorTotalEstoque(estoqueId);
    }
}