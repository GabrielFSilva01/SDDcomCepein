package com.cepein.sdd.service;

import com.cepein.sdd.domain.model.Fornecedor;
import com.cepein.sdd.domain.model.Produto;
import com.cepein.sdd.domain.model.ProdutoFornecedor;
import com.cepein.sdd.domain.repository.FornecedorRepository;
import com.cepein.sdd.domain.repository.ProdutoFornecedorRepository;
import com.cepein.sdd.domain.repository.ProdutoRepository;
import com.cepein.sdd.web.dto.RelatorioFornecedorDTO;
import com.cepein.sdd.web.dto.RelatorioProdutoDTO;
import com.cepein.sdd.web.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RelatorioService {

    private final FornecedorRepository fornecedorRepository;
    private final ProdutoRepository produtoRepository;
    private final ProdutoFornecedorRepository produtoFornecedorRepository;

    @Transactional(readOnly = true)
    public RelatorioFornecedorDTO relatorioPorFornecedor(Long idFornecedor) {
        Fornecedor fornecedor = fornecedorRepository.findById(idFornecedor)
                .orElseThrow(() -> new ResourceNotFoundException("Fornecedor não encontrado com ID: " + idFornecedor));

        List<ProdutoFornecedor> vinculos = produtoFornecedorRepository.findByFornecedorIdWithProduto(idFornecedor);

        List<RelatorioFornecedorDTO.ProdutoItemDTO> produtos = vinculos.stream()
                .map(pf -> RelatorioFornecedorDTO.ProdutoItemDTO.builder()
                        .idProduto(pf.getProduto().getIdProduto())
                        .skuCodigo(pf.getProduto().getSkuCodigo())
                        .nome(pf.getProduto().getNome())
                        .categoria(pf.getProduto().getCategoria())
                        .precoCusto(pf.getPrecoCusto())
                        .prazoEntregaDias(pf.getPrazoEntregaDias())
                        .build())
                .collect(Collectors.toList());

        return RelatorioFornecedorDTO.builder()
                .idFornecedor(fornecedor.getIdFornecedor())
                .razaoSocial(fornecedor.getRazaoSocial())
                .cnpj(fornecedor.getCnpj())
                .produtos(produtos)
                .build();
    }

    @Transactional(readOnly = true)
    public RelatorioProdutoDTO relatorioPorProduto(Long idProduto) {
        Produto produto = produtoRepository.findById(idProduto)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com ID: " + idProduto));

        List<ProdutoFornecedor> vinculos = produtoFornecedorRepository.findByProdutoIdWithFornecedor(idProduto);

        List<RelatorioProdutoDTO.FornecedorItemDTO> fornecedores = vinculos.stream()
                .map(pf -> RelatorioProdutoDTO.FornecedorItemDTO.builder()
                        .idFornecedor(pf.getFornecedor().getIdFornecedor())
                        .razaoSocial(pf.getFornecedor().getRazaoSocial())
                        .cnpj(pf.getFornecedor().getCnpj())
                        .precoCusto(pf.getPrecoCusto())
                        .prazoEntregaDias(pf.getPrazoEntregaDias())
                        .build())
                .collect(Collectors.toList());

        return RelatorioProdutoDTO.builder()
                .idProduto(produto.getIdProduto())
                .skuCodigo(produto.getSkuCodigo())
                .nome(produto.getNome())
                .categoria(produto.getCategoria())
                .fornecedores(fornecedores)
                .build();
    }
}
