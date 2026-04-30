package com.unifor.stockPlus.service;

import com.unifor.stockPlus.dto.*;
import com.unifor.stockPlus.entity.*;
import com.unifor.stockPlus.exceptions.ResourceNotFoundException;
import com.unifor.stockPlus.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProtocoloService {

    private final ProtocoloRepository protocoloRepository;
    private final ProdutoRepository produtoRepository;
    private final ItemProtocoloRepository itemProtocoloRepository;
    private final LojaService lojaService;

    public List<ProtocoloDTO> listar() {
        return protocoloRepository.findAll()
                .stream()
                .map(this::mapComItens)
                .toList();
    }

    public List<ProtocoloDTO> buscarPorLoja(Long lojaId) {
        return protocoloRepository.findByLojaId(lojaId)
                .stream()
                .map(this::mapComItens)
                .toList();
    }

    public ProtocoloDTO buscarPorId(Long id) {
        Protocolo protocolo = getEntity(id);
        return mapComItens(protocolo);
    }

    public ProtocoloDTO criar(ProtocoloDTO dto) {

        Loja loja = lojaService.getEntityById(dto.getLojaId());

        Protocolo protocolo = new Protocolo();
        protocolo.setNome(dto.getNome());
        protocolo.setPreco(dto.getPreco());
        protocolo.setLoja(loja);

        protocoloRepository.save(protocolo);

        salvarItens(protocolo, dto.getItens());

        return mapComItens(protocolo);
    }

    @Transactional
    public ProtocoloDTO editar(Long id, ProtocoloDTO dto) {

        Protocolo protocolo = getEntity(id);

        protocolo.setNome(dto.getNome());
        protocolo.setPreco(dto.getPreco());

        itemProtocoloRepository.deleteByProtocoloId(id);

        salvarItens(protocolo, dto.getItens());

        return mapComItens(protocolo);
    }

    public void deletar(Long id) {
        protocoloRepository.deleteById(id);
    }

    public ProtocoloDTO adicionarItem(Long protocoloId, ItemProtocoloDTO itemDto) {

        Protocolo protocolo = getEntity(protocoloId);

        Produto produto = produtoRepository.findById(itemDto.getProdutoId())
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

        validarProdutoDaLoja(protocolo, produto);

        ItemProtocolo item = new ItemProtocolo();
        item.setProduto(produto);
        item.setQuantidade(itemDto.getQuantidade());
        item.setProtocolo(protocolo);

        itemProtocoloRepository.save(item);

        return mapComItens(protocolo);
    }

    // 🔥 MÉTODOS AUXILIARES

    private void salvarItens(Protocolo protocolo, List<ItemProtocoloDTO> itensDto) {

        if (itensDto == null) return;

        for (ItemProtocoloDTO itemDto : itensDto) {

            Produto produto = produtoRepository.findById(itemDto.getProdutoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

            validarProdutoDaLoja(protocolo, produto);

            ItemProtocolo item = new ItemProtocolo();
            item.setProduto(produto);
            item.setQuantidade(itemDto.getQuantidade());
            item.setProtocolo(protocolo);

            itemProtocoloRepository.save(item);
        }
    }

    // 🔥 REGRA CRÍTICA
    private void validarProdutoDaLoja(Protocolo protocolo, Produto produto) {

        if (!produto.getEstoque().getLoja().getId()
                .equals(protocolo.getLoja().getId())) {

            throw new RuntimeException("Produto não pertence à mesma loja do protocolo");
        }
    }

    private Protocolo getEntity(Long id) {
        return protocoloRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Protocolo não encontrado"));
    }

    private ProtocoloDTO mapComItens(Protocolo protocolo) {

        List<ItemProtocolo> itens =
                itemProtocoloRepository.findByProtocoloIdWithProduto(protocolo.getId());

        ProtocoloDTO dto = new ProtocoloDTO();

        dto.setId(protocolo.getId());
        dto.setNome(protocolo.getNome());
        dto.setPreco(protocolo.getPreco());
        dto.setLojaId(protocolo.getLoja().getId());

        double total = itens.stream()
                .mapToDouble(i -> i.getProduto().getPrecoUnitario() * i.getQuantidade())
                .sum();

        dto.setValorTotal(total);

        dto.setItens(
                itens.stream()
                        .map(this::toItemDTO)
                        .collect(Collectors.toList())
        );

        return dto;
    }

    private ItemProtocoloDTO toItemDTO(ItemProtocolo item) {

        ItemProtocoloDTO dto = new ItemProtocoloDTO();

        dto.setId(item.getId());
        dto.setProdutoId(item.getProduto().getId());
        dto.setProdutoNome(item.getProduto().getNome());
        dto.setQuantidade(item.getQuantidade());
        dto.setValorItem(item.getProduto().getPrecoUnitario() * item.getQuantidade());

        return dto;
    }
}