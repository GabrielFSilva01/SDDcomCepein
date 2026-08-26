package com.cepein.sdd.service;

import com.cepein.sdd.domain.model.Fornecedor;
import com.cepein.sdd.domain.model.Produto;
import com.cepein.sdd.domain.model.ProdutoFornecedor;
import com.cepein.sdd.domain.model.ProdutoFornecedorId;
import com.cepein.sdd.domain.repository.FornecedorRepository;
import com.cepein.sdd.domain.repository.ProdutoFornecedorRepository;
import com.cepein.sdd.domain.repository.ProdutoRepository;
import com.cepein.sdd.web.dto.VinculoDTO;
import com.cepein.sdd.web.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VinculoService {

    private final FornecedorRepository fornecedorRepository;
    private final ProdutoRepository produtoRepository;
    private final ProdutoFornecedorRepository produtoFornecedorRepository;

    @Transactional
    public VinculoDTO salvarVinculo(Long idProduto, Long idFornecedor, VinculoDTO dto) {
        Fornecedor fornecedor = fornecedorRepository.findById(idFornecedor)
                .orElseThrow(() -> new ResourceNotFoundException("Fornecedor não encontrado com ID: " + idFornecedor));

        Produto produto = produtoRepository.findById(idProduto)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com ID: " + idProduto));

        ProdutoFornecedorId id = new ProdutoFornecedorId(idFornecedor, idProduto);

        ProdutoFornecedor pf = produtoFornecedorRepository.findById(id)
                .orElseGet(() -> ProdutoFornecedor.builder()
                        .id(id)
                        .fornecedor(fornecedor)
                        .produto(produto)
                        .build());

        pf.setPrecoCusto(dto.getPrecoCusto());
        pf.setPrazoEntregaDias(dto.getPrazoEntregaDias());

        ProdutoFornecedor salvo = produtoFornecedorRepository.save(pf);

        return VinculoDTO.builder()
                .idFornecedor(salvo.getId().getIdFornecedor())
                .idProduto(salvo.getId().getIdProduto())
                .precoCusto(salvo.getPrecoCusto())
                .prazoEntregaDias(salvo.getPrazoEntregaDias())
                .mensagem("Vínculo salvo com sucesso")
                .build();
    }

    @Transactional
    public void removerVinculo(Long idProduto, Long idFornecedor) {
        ProdutoFornecedorId id = new ProdutoFornecedorId(idFornecedor, idProduto);
        if (!produtoFornecedorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Vínculo não encontrado para o Fornecedor ID " + idFornecedor + " e Produto ID " + idProduto);
        }
        produtoFornecedorRepository.deleteById(id);
    }
}
