package com.unifor.stockPlus.controller;

import com.unifor.stockPlus.dto.ClienteDTO;
import com.unifor.stockPlus.dto.LoginRequest;
import com.unifor.stockPlus.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    public ResponseEntity<ClienteDTO> create(@Valid @RequestBody ClienteDTO dto) {

        ClienteDTO clienteCriado = clienteService.create(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(clienteCriado);
    }

    @PostMapping("/login")
    public ResponseEntity<ClienteDTO> login(@RequestBody LoginRequest request) {
        ClienteDTO cliente = clienteService.login(request.email(), request.senha());
        return ResponseEntity.ok(cliente);
    }

    @GetMapping("/{id}")
    public ClienteDTO get(@PathVariable Long id) {
        return clienteService.getById(id);
    }

    @GetMapping
    public List<ClienteDTO> getAll() {
        return clienteService.getAll();
    }

    @PutMapping("/{id}")
    public ClienteDTO update(@PathVariable Long id, @RequestBody ClienteDTO dto) {
        return clienteService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        clienteService.delete(id);
    }
}