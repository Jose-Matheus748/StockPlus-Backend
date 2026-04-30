package com.unifor.stockPlus.controller;

import com.unifor.stockPlus.dto.LoginRequest;
import com.unifor.stockPlus.dto.ClienteDTO;
import com.unifor.stockPlus.dto.LojaDTO;
import com.unifor.stockPlus.service.ClienteService;
import com.unifor.stockPlus.service.LojaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final ClienteService clienteService;
    private final LojaService lojaService;

    public AuthController(ClienteService clienteService, LojaService lojaService) {
        this.clienteService = clienteService;
        this.lojaService = lojaService;
    }

    @PostMapping("/clientes/login")
    public ResponseEntity<ClienteDTO> loginCliente(@RequestBody LoginRequest request) {
        ClienteDTO cliente = clienteService.login(request.email(), request.senha());
        return ResponseEntity.ok(cliente);
    }

    @PostMapping("/lojas/login")
    public ResponseEntity<LojaDTO> loginLoja(@RequestBody LoginRequest request) {
        LojaDTO loja = lojaService.login(request.email(), request.senha());
        return ResponseEntity.ok(loja);
    }
}