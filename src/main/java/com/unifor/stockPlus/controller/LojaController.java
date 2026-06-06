package com.unifor.stockPlus.controller;

import com.unifor.stockPlus.dto.LojaDTO;
import com.unifor.stockPlus.dto.LoginRequest;
import com.unifor.stockPlus.service.LojaService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/lojas")
public class LojaController {

    private final LojaService lojaService;

    public LojaController(LojaService lojaService) {
        this.lojaService = lojaService;
    }

    @PostMapping
    public LojaDTO create(@RequestBody LojaDTO dto) {
        return lojaService.create(dto);
    }

    @PostMapping("/login")
    public ResponseEntity<LojaDTO> login(@RequestBody LoginRequest request) {
        LojaDTO loja = lojaService.login(request.email(), request.senha());
        return ResponseEntity.ok(loja);
    }

    @GetMapping("/{id}")
    public LojaDTO get(@PathVariable Long id) {
        return lojaService.getById(id);
    }

    @GetMapping
    public List<LojaDTO> getAll() {
        return lojaService.getAll();
    }

    @PutMapping("/{id}")
    public LojaDTO update(@PathVariable Long id, @RequestBody LojaDTO dto) {
        return lojaService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        lojaService.delete(id);
    }

    @PatchMapping("/{id}/foto")
    public ResponseEntity<LojaDTO> atualizarFoto(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        LojaDTO dto = lojaService.getById(id);
        dto.setFotoPerfil(body.get("fotoPerfil"));
        LojaDTO atualizado = lojaService.update(id, dto);
        return ResponseEntity.ok(atualizado);
    }
}