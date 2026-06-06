package com.unifor.stockPlus.service;

import com.unifor.stockPlus.dto.ClienteDTO;
import com.unifor.stockPlus.entity.Cliente;
import com.unifor.stockPlus.repository.ClienteRepository;
import com.unifor.stockPlus.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public ClienteDTO create(ClienteDTO dto) {
        if (clienteRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email já cadastrado");
        }

        Cliente cliente = dto.toEntity();
        Cliente salvo = clienteRepository.save(cliente);

        return ClienteDTO.fromEntity(salvo);
    }

    public ClienteDTO getById(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        return ClienteDTO.fromEntity(cliente);
    }

    public List<ClienteDTO> getAll() {
        return clienteRepository.findAll()
                .stream()
                .map(ClienteDTO::fromEntity)
                .toList();
    }

    public ClienteDTO update(Long id, ClienteDTO dto) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        cliente.setNome(dto.getNome());
        cliente.setEmail(dto.getEmail());
        cliente.setFotoPerfil(dto.getFotoPerfil());

        clienteRepository.save(cliente);

        return ClienteDTO.fromEntity(cliente);
    }

    public void delete(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cliente não encontrado");
        }

        clienteRepository.deleteById(id);
    }

    public ClienteDTO login(String email, String senha) {
        Cliente cliente = clienteRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        if (!cliente.getSenha().equals(senha)) {
            throw new RuntimeException("Senha incorreta");
        }

        return ClienteDTO.fromEntity(cliente);
    }
}