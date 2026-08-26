package com.cepein.sdd.service;

import com.cepein.sdd.domain.model.Produto;
import com.cepein.sdd.domain.repository.ProdutoFornecedorRepository;
import com.cepein.sdd.domain.repository.ProdutoRepository;
import com.cepein.sdd.web.dto.ProdutoDTO;
import com.cepein.sdd.web.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final ProdutoFornecedorRepository produtoFornecedorRepository;

    @Transactional(readOnly = true)
    public List<ProdutoDTO> listarTodos(String query) {
        List<Produto> lista = (query != null && !query.isBlank())
                ? produtoRepository.searchByQuery(query.trim())
                : produtoRepository.findAll();

        return lista.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProdutoDTO buscarPorId(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com ID: " + id));
        return toDTO(produto);
    }

    @Transactional
    public ProdutoDTO salvar(ProdutoDTO dto) {
        if (produtoRepository.existsBySkuCodigo(dto.getSkuCodigo())) {
            throw new IllegalArgumentException("Já existe um produto cadastrado com o SKU: " + dto.getSkuCodigo());
        }

        Produto entity = Produto.builder()
                .skuCodigo(dto.getSkuCodigo())
                .nome(dto.getNome())
                .descricao(dto.getDescricao())
                .categoria(dto.getCategoria())
                .build();

        Produto salvo = produtoRepository.save(entity);
        return toDTO(salvo);
    }

    @Transactional
    public ProdutoDTO atualizar(Long id, ProdutoDTO dto) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com ID: " + id));

        if (produtoRepository.existsBySkuCodigoAndIdProdutoNot(dto.getSkuCodigo(), id)) {
            throw new IllegalArgumentException("O SKU " + dto.getSkuCodigo() + " já pertence a outro produto");
        }

        produto.setSkuCodigo(dto.getSkuCodigo());
        produto.setNome(dto.getNome());
        produto.setDescricao(dto.getDescricao());
        produto.setCategoria(dto.getCategoria());

        return toDTO(produtoRepository.save(produto));
    }

    @Transactional
    public void excluir(Long id) {
        if (!produtoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Produto não encontrado para exclusão com ID: " + id);
        }
        produtoFornecedorRepository.deleteById_IdProduto(id);
        produtoRepository.deleteById(id);
    }

    public ProdutoDTO toDTO(Produto entity) {
        return ProdutoDTO.builder()
                .idProduto(entity.getIdProduto())
                .skuCodigo(entity.getSkuCodigo())
                .nome(entity.getNome())
                .descricao(entity.getDescricao())
                .categoria(entity.getCategoria())
                .dataCadastro(entity.getDataCadastro())
                .build();
    }
}
