package com.unifor.stockPlus.service;

import com.unifor.stockPlus.dto.LojaDTO;
import com.unifor.stockPlus.entity.Loja;
import com.unifor.stockPlus.exceptions.ResourceNotFoundException;
import com.unifor.stockPlus.repository.LojaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LojaService {

    private final LojaRepository lojaRepository;
    private final EstoqueService estoqueService;

    public LojaService(LojaRepository lojaRepository, EstoqueService estoqueService) {
        this.lojaRepository = lojaRepository;
        this.estoqueService = estoqueService;
    }

    public LojaDTO create(LojaDTO dto) {
        if (lojaRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email já cadastrado");
        }

        if (lojaRepository.existsByCnpj(dto.getCnpj())) {
            throw new RuntimeException("CNPJ já cadastrado");
        }

        Loja loja = dto.toEntity();
        Loja salva = lojaRepository.save(loja);

        estoqueService.criarEstoquePadraoLoja(salva);

        return LojaDTO.fromEntity(salva);
    }

    public LojaDTO getById(Long id) {
        Loja loja = lojaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loja não encontrada"));

        return LojaDTO.fromEntity(loja);
    }

    public Loja getEntityById(Long id) {
        return lojaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loja não encontrada"));
    }

    public List<LojaDTO> getAll() {
        return lojaRepository.findAll()
                .stream()
                .map(LojaDTO::fromEntity)
                .toList();
    }

    public LojaDTO update(Long id, LojaDTO dto) {
        Loja loja = lojaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loja não encontrada"));

        loja.setNome(dto.getNome());
        loja.setEmail(dto.getEmail());
        loja.setCnpj(dto.getCnpj());

        lojaRepository.save(loja);

        return LojaDTO.fromEntity(loja);
    }

    public void delete(Long id) {
        if (!lojaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Loja não encontrada");
        }

        lojaRepository.deleteById(id);
    }

    public LojaDTO login(String email, String senha) {
        Loja loja = lojaRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Loja não encontrada"));

        if (!loja.getSenha().equals(senha)) {
            throw new RuntimeException("Senha incorreta");
        }

        return LojaDTO.fromEntity(loja);
    }
}