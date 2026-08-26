package com.cepein.sdd.service;

import com.cepein.sdd.domain.model.Fornecedor;
import com.cepein.sdd.domain.repository.FornecedorRepository;
import com.cepein.sdd.domain.repository.ProdutoFornecedorRepository;
import com.cepein.sdd.web.dto.FornecedorDTO;
import com.cepein.sdd.web.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FornecedorService {

    private final FornecedorRepository fornecedorRepository;
    private final ProdutoFornecedorRepository produtoFornecedorRepository;

    @Transactional(readOnly = true)
    public List<FornecedorDTO> listarTodos(String query) {
        List<Fornecedor> lista = (query != null && !query.isBlank())
                ? fornecedorRepository.searchByQuery(query.trim())
                : fornecedorRepository.findAll();

        return lista.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public FornecedorDTO buscarPorId(Long id) {
        Fornecedor fornecedor = fornecedorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fornecedor não encontrado com ID: " + id));
        return toDTO(fornecedor);
    }

    @Transactional
    public FornecedorDTO salvar(FornecedorDTO dto) {
        if (fornecedorRepository.existsByCnpj(dto.getCnpj())) {
            throw new IllegalArgumentException("Já existe um fornecedor cadastrado com o CNPJ: " + dto.getCnpj());
        }

        Fornecedor entity = Fornecedor.builder()
                .razaoSocial(dto.getRazaoSocial())
                .cnpj(dto.getCnpj())
                .telefone(dto.getTelefone())
                .email(dto.getEmail())
                .build();

        Fornecedor salvo = fornecedorRepository.save(entity);
        return toDTO(salvo);
    }

    @Transactional
    public FornecedorDTO atualizar(Long id, FornecedorDTO dto) {
        Fornecedor fornecedor = fornecedorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fornecedor não encontrado com ID: " + id));

        if (fornecedorRepository.existsByCnpjAndIdFornecedorNot(dto.getCnpj(), id)) {
            throw new IllegalArgumentException("O CNPJ " + dto.getCnpj() + " já pertence a outro fornecedor");
        }

        fornecedor.setRazaoSocial(dto.getRazaoSocial());
        fornecedor.setCnpj(dto.getCnpj());
        fornecedor.setTelefone(dto.getTelefone());
        fornecedor.setEmail(dto.getEmail());

        return toDTO(fornecedorRepository.save(fornecedor));
    }

    @Transactional
    public void excluir(Long id) {
        if (!fornecedorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Fornecedor não encontrado para exclusão com ID: " + id);
        }
        produtoFornecedorRepository.deleteById_IdFornecedor(id);
        fornecedorRepository.deleteById(id);
    }

    public FornecedorDTO toDTO(Fornecedor entity) {
        return FornecedorDTO.builder()
                .idFornecedor(entity.getIdFornecedor())
                .razaoSocial(entity.getRazaoSocial())
                .cnpj(entity.getCnpj())
                .telefone(entity.getTelefone())
                .email(entity.getEmail())
                .dataCadastro(entity.getDataCadastro())
                .build();
    }
}
