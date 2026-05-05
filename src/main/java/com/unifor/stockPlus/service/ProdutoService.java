package com.unifor.stockPlus.service;

import com.unifor.stockPlus.dto.ProdutoDTO;
import com.unifor.stockPlus.entity.*;
import com.unifor.stockPlus.exceptions.ResourceNotFoundException;
import com.unifor.stockPlus.repository.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final EstoqueRepository estoqueRepository;

    public ProdutoService(ProdutoRepository produtoRepo, EstoqueRepository estoqueRepo) {
        this.produtoRepository = produtoRepo;
        this.estoqueRepository = estoqueRepo;
    }

    public ProdutoDTO create(ProdutoDTO dto, Loja loja) {
        validarDados(dto);

        List<Estoque> estoques = (dto.getEstoqueIds() != null && !dto.getEstoqueIds().isEmpty())
                ? buscarEstoques(dto.getEstoqueIds())
                : new ArrayList<>(); // ← era List.of()

        validarLoja(estoques, loja);
        Produto produto = dto.toEntity(estoques);
        return ProdutoDTO.fromEntity(produtoRepository.save(produto));
    }

    public ProdutoDTO getById(Long id) {
        return ProdutoDTO.fromEntity(buscarProduto(id));
    }

    public List<ProdutoDTO> getAll() {
        return produtoRepository.findAll()
                .stream()
                .map(ProdutoDTO::fromEntity)
                .toList();
    }

    public ProdutoDTO update(Long id, ProdutoDTO dto, Loja loja) {
        validarDados(dto);

        Produto produto = buscarProduto(id);

        List<Estoque> estoques = (dto.getEstoqueIds() != null && !dto.getEstoqueIds().isEmpty())
                ? buscarEstoques(dto.getEstoqueIds())
                : new ArrayList<>(produto.getEstoques()); // ← cópia mutável

        validarLoja(estoques, loja);

        produto.setNome(dto.getNome());
        produto.setDescricao(dto.getDescricao());
        produto.setFornecedor(dto.getFornecedor());
        produto.setMarca(dto.getMarca());
        produto.setQuantidade(dto.getQuantidade());
        produto.setPrecoUnitario(dto.getPrecoUnitario());
        produto.setEstoques(estoques);

        return ProdutoDTO.fromEntity(produtoRepository.save(produto));
    }

    public void delete(Long id, Loja loja) {
        Produto produto = buscarProduto(id);
        validarLoja(produto.getEstoques(), loja); // ← lista
        produtoRepository.delete(produto);
    }

    public ProdutoDTO addQuantity(Long id, int quantity, Loja loja) {
        if (quantity <= 0) throw new IllegalArgumentException("Quantidade deve ser maior que zero");

        Produto produto = buscarProduto(id);
        validarLoja(produto.getEstoques(), loja); // ← lista
        produto.setQuantidade(produto.getQuantidade() + quantity);
        return ProdutoDTO.fromEntity(produtoRepository.save(produto));
    }

    public ProdutoDTO removeQuantity(Long id, int quantity, Loja loja) {
        if (quantity <= 0) throw new IllegalArgumentException("Quantidade deve ser maior que zero");

        Produto produto = buscarProduto(id);
        validarLoja(produto.getEstoques(), loja); // ← lista

        if (produto.getQuantidade() < quantity) {
            throw new IllegalArgumentException("Estoque insuficiente");
        }

        produto.setQuantidade(produto.getQuantidade() - quantity);
        return ProdutoDTO.fromEntity(produtoRepository.save(produto));
    }

    public Double calcularValorTotalEstoque(Long estoqueId) {

        buscarEstoque(estoqueId);

        return produtoRepository.findByEstoqueId(estoqueId)
                .stream()
                .mapToDouble(p -> p.getPrecoUnitario() * p.getQuantidade())
                .sum();
    }

    public List<ProdutoDTO> listarPorEstoque(Long estoqueId) {

        buscarEstoque(estoqueId);

        return produtoRepository.findByEstoqueId(estoqueId)
                .stream()
                .map(ProdutoDTO::fromEntity)
                .toList();
    }

    private Produto buscarProduto(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));
    }

    private Estoque buscarEstoque(Long id) {
        return estoqueRepository.findByIdWithLoja(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estoque não encontrado"));
    }

    private List<Estoque> buscarEstoques(List<Long> ids) {
        return ids.stream()
                .map(this::buscarEstoque)
                .collect(Collectors.toCollection(ArrayList::new)); // ← mutável
    }

    private void validarLoja(List<Estoque> estoques, Loja loja) {
        if (estoques == null || estoques.isEmpty()) return; // ← produto sem estoque ainda

        boolean algumInvalido = estoques.stream()
                .anyMatch(e -> !e.getLoja().getId().equals(loja.getId()));

        if (algumInvalido) {
            throw new RuntimeException("Acesso negado: estoque não pertence à loja");
        }
    }

    private void validarDados(ProdutoDTO dto) {
        if (dto.getPrecoUnitario() == null || dto.getPrecoUnitario() < 0) {
            throw new IllegalArgumentException("Preço inválido");
        }

        if (dto.getQuantidade() == null || dto.getQuantidade() < 0) {
            throw new IllegalArgumentException("Quantidade inválida");
        }

        if (dto.getNome() == null || dto.getNome().isBlank()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }
    }
}