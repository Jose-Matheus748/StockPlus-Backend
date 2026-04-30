package com.unifor.stockPlus.service;

import com.unifor.stockPlus.dto.ProdutoDTO;
import com.unifor.stockPlus.entity.*;
import com.unifor.stockPlus.exceptions.ResourceNotFoundException;
import com.unifor.stockPlus.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;

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

        Estoque estoque = buscarEstoque(dto.getEstoqueId());

        validarLoja(estoque, loja);

        Produto produto = dto.toEntity(estoque);

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
        Estoque estoque = buscarEstoque(dto.getEstoqueId());

        validarLoja(estoque, loja);

        produto.setNome(dto.getNome());
        produto.setDescricao(dto.getDescricao());
        produto.setFornecedor(dto.getFornecedor());
        produto.setMarca(dto.getMarca());
        produto.setQuantidade(dto.getQuantidade());
        produto.setPrecoUnitario(dto.getPrecoUnitario());
        produto.setEstoque(estoque);

        return ProdutoDTO.fromEntity(produtoRepository.save(produto));
    }

    public void delete(Long id, Loja loja) {

        Produto produto = buscarProduto(id);

        validarLoja(produto.getEstoque(), loja);

        produtoRepository.delete(produto);
    }

    public ProdutoDTO addQuantity(Long id, int quantity, Loja loja) {

        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero");
        }

        Produto produto = buscarProduto(id);

        validarLoja(produto.getEstoque(), loja);

        produto.setQuantidade(produto.getQuantidade() + quantity);

        return ProdutoDTO.fromEntity(produtoRepository.save(produto));
    }

    public ProdutoDTO removeQuantity(Long id, int quantity, Loja loja) {

        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero");
        }

        Produto produto = buscarProduto(id);

        validarLoja(produto.getEstoque(), loja);

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
        return estoqueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estoque não encontrado"));
    }

    private void validarLoja(Estoque estoque, Loja loja) {
        if (!estoque.getLoja().getId().equals(loja.getId())) {
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