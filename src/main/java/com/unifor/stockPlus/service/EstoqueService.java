package com.unifor.stockPlus.service;

import com.unifor.stockPlus.dto.*;
import com.unifor.stockPlus.entity.*;
import com.unifor.stockPlus.exceptions.ResourceNotFoundException;
import com.unifor.stockPlus.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstoqueService {

    private final ProtocoloRepository protocoloRepository;
    private final ItemProtocoloRepository itemProtocoloRepository;
    private final EstoqueRepository estoqueRepository;
    private final ProdutoRepository produtoRepository;

    public EstoqueService(EstoqueRepository estoqueRepository, ProdutoRepository produtoRepository, ProtocoloRepository protocoloRepository, ItemProtocoloRepository itemProtocoloRepository) {
        this.estoqueRepository = estoqueRepository;
        this.produtoRepository = produtoRepository;
        this.protocoloRepository = protocoloRepository;
        this.itemProtocoloRepository = itemProtocoloRepository;
    }

    public EstoqueDTO create(EstoqueDTO dto, Loja loja) {
        Estoque estoque = dto.toEntity(loja);
        Estoque salvo = estoqueRepository.save(estoque);
        return EstoqueDTO.fromEntity(salvo);
    }

    public EstoqueDTO getById(Long id) {
        Estoque estoque = estoqueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estoque não encontrado"));

        return EstoqueDTO.fromEntity(estoque);
    }

    public List<EstoqueDTO> getAll() {
        return estoqueRepository.findAll()
                .stream()
                .map(EstoqueDTO::fromEntity)
                .toList();
    }

    public List<EstoqueDTO> listarPorLoja(Long lojaId) {
        return estoqueRepository.findByLojaId(lojaId)
                .stream()
                .map(EstoqueDTO::fromEntity)
                .toList();
    }

    public boolean pertenceALoja(Long estoqueId, Long lojaId) {
        return estoqueRepository.existsByIdAndLojaId(estoqueId, lojaId);
    }

    public EstoqueDTO update(Long id, EstoqueDTO dto, Loja loja) {

        if (!pertenceALoja(id, loja.getId())) {
            throw new ResourceNotFoundException("Estoque não pertence à loja");
        }

        Estoque estoque = estoqueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estoque não encontrado"));

        estoque.setNome(dto.getNome());
        estoque.setDescricao(dto.getDescricao());

        return EstoqueDTO.fromEntity(estoqueRepository.save(estoque));
    }


    public void delete(Long id, Loja loja) {
        if (!pertenceALoja(id, loja.getId())) {
            throw new ResourceNotFoundException("Estoque não pertence à loja");
        }

        estoqueRepository.deleteById(id);
    }

    public List<ProdutoDTO> listarProdutosDoEstoque(Long estoqueId) {
        estoqueRepository.findById(estoqueId)
                .orElseThrow(() -> new ResourceNotFoundException("Estoque não encontrado"));

        return produtoRepository.findByEstoqueId(estoqueId)
                .stream()
                .map(ProdutoDTO::fromEntity)
                .toList();
    }

    public ValorTotalEstoqueDTO calcularValorTotal(Long estoqueId) {
        Estoque estoque = estoqueRepository.findById(estoqueId)
                .orElseThrow(() -> new ResourceNotFoundException("Estoque não encontrado"));

        double valorTotal = estoque.getProdutos().stream()
                .mapToDouble(p -> p.getPrecoUnitario() * p.getQuantidade())
                .sum();

        int quantidadeTotal = estoque.getProdutos().stream()
                .mapToInt(Produto::getQuantidade)
                .sum();

        return new ValorTotalEstoqueDTO(valorTotal, quantidadeTotal);
    }

    public EstoqueDTO criarEstoquePadraoLoja(Loja loja) {
        Estoque estoque = new Estoque();
        estoque.setNome("Estoque Padrão");
        estoque.setDescricao("Estoque inicial da loja");
        estoque.setLoja(loja);

        return EstoqueDTO.fromEntity(estoqueRepository.save(estoque));
    }

    public void baixarPorProtocolo(Long protocoloId) {

        Protocolo protocolo = protocoloRepository.findById(protocoloId)
                .orElseThrow(() -> new ResourceNotFoundException("Protocolo não encontrado"));

        List<ItemProtocolo> itens = itemProtocoloRepository
                .findByProtocoloIdWithProduto(protocoloId);

        for (ItemProtocolo item : itens) {

            Produto produto = item.getProduto();

            int quantidadeAtual = produto.getQuantidade();
            int quantidadeBaixa = item.getQuantidade();

            if (quantidadeAtual < quantidadeBaixa) {
                throw new RuntimeException("Estoque insuficiente para o produto: " + produto.getNome());
            }

            produto.setQuantidade(quantidadeAtual - quantidadeBaixa);

            produtoRepository.save(produto);
        }
    }
}