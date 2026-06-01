package com.unifor.stockPlus.controller;

import com.unifor.stockPlus.dto.*;
import com.unifor.stockPlus.entity.Loja;
import com.unifor.stockPlus.service.EstoqueService;
import com.unifor.stockPlus.service.LojaService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

import java.util.List;

@RestController
@RequestMapping("/estoques")
public class EstoqueController {

    private final EstoqueService estoqueService;
    private final LojaService lojaService;

    public EstoqueController(EstoqueService estoqueService, LojaService lojaService) {
        this.estoqueService = estoqueService;
        this.lojaService = lojaService;
    }

    @PostMapping
    public ResponseEntity<EstoqueDTO> create(@RequestBody EstoqueDTO dto) {
        Loja loja = lojaService.getEntityById(dto.getLojaId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(estoqueService.create(dto, loja));
    }

    @PostMapping("/baixa")
    public void baixarEstoque(@RequestBody BaixaEstoqueDTO dto) {
        estoqueService.baixarPorProtocolo(dto.getProtocoloId());
    }

    @GetMapping("/{id}")
    public EstoqueDTO get(@PathVariable Long id) {
        return estoqueService.getById(id);
    }

    @GetMapping("/loja/{lojaId}")
    public List<EstoqueDTO> listarPorLoja(@PathVariable Long lojaId) {
        return estoqueService.listarPorLoja(lojaId);
    }

    @PutMapping("/{id}")
    public EstoqueDTO update(
            @PathVariable Long id,
            @RequestBody EstoqueDTO dto,
            @RequestParam Long lojaId
    ) {
        Loja loja = lojaService.getEntityById(lojaId);
        return estoqueService.update(id, dto, loja);
    }

    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable Long id,
            @RequestParam Long lojaId
    ) {
        Loja loja = lojaService.getEntityById(lojaId);
        estoqueService.delete(id, loja);
    }

    @GetMapping("/{id}/produtos")
    public List<ProdutoDTO> listarProdutos(@PathVariable Long id) {
        return estoqueService.listarProdutosDoEstoque(id);
    }

    @GetMapping("/{id}/valor-total")
    public ValorTotalEstoqueDTO calcularValorTotal(@PathVariable Long id) {
        return estoqueService.calcularValorTotal(id);
    }
}