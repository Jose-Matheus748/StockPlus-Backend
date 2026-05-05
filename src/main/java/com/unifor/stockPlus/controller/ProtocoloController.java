package com.unifor.stockPlus.controller;

import com.unifor.stockPlus.dto.ItemProtocoloDTO;
import com.unifor.stockPlus.dto.ProtocoloDTO;
import com.unifor.stockPlus.repository.ItemProtocoloRepository;
import com.unifor.stockPlus.service.ProtocoloService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/protocolos")
@RequiredArgsConstructor
public class ProtocoloController {

    private final ProtocoloService protocoloService;
    private final ItemProtocoloRepository itemProtocoloRepository;

    @GetMapping
    public List<ProtocoloDTO> listar() {
        return protocoloService.listar();
    }

    @GetMapping("/{id}")
    public ProtocoloDTO buscarPorId(@PathVariable Long id) {
        return protocoloService.buscarPorId(id);
    }

    @GetMapping("/loja/{lojaId}")
    public List<ProtocoloDTO> listarPorLoja(@PathVariable Long lojaId) {
        return protocoloService.buscarPorLoja(lojaId);
    }

    @PostMapping
    public ResponseEntity<ProtocoloDTO> criar(@RequestBody ProtocoloDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(protocoloService.criar(dto));
    }

    @PutMapping("/{id}")
    public ProtocoloDTO editar(@PathVariable Long id, @RequestBody ProtocoloDTO dto) {
        return protocoloService.editar(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        protocoloService.deletar(id);
    }

    @DeleteMapping("/itens/{id}")
    public void removerItem(@PathVariable Long id) {
        itemProtocoloRepository.deleteById(id);
    }

    @PostMapping("/{id}/itens")
    public ProtocoloDTO adicionarItem(
            @PathVariable Long id,
            @RequestBody ItemProtocoloDTO itemDto
    ) {
        return protocoloService.adicionarItem(id, itemDto);
    }
}