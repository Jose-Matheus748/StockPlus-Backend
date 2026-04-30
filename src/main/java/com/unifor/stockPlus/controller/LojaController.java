package com.unifor.stockPlus.controller;

import com.unifor.stockPlus.dto.LojaDTO;
import com.unifor.stockPlus.dto.LoginRequest;
import com.unifor.stockPlus.service.LojaService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

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
}