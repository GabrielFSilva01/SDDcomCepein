package com.cepein.sdd.web.controller;

import com.cepein.sdd.service.FornecedorService;
import com.cepein.sdd.service.RelatorioService;
import com.cepein.sdd.web.dto.FornecedorDTO;
import com.cepein.sdd.web.dto.RelatorioFornecedorDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fornecedores")
@RequiredArgsConstructor
public class FornecedorController {

    private final FornecedorService fornecedorService;
    private final RelatorioService relatorioService;

    @GetMapping
    public ResponseEntity<List<FornecedorDTO>> listarTodos(@RequestParam(required = false) String query) {
        return ResponseEntity.ok(fornecedorService.listarTodos(query));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FornecedorDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(fornecedorService.buscarPorId(id));
    }

    @GetMapping("/{id}/produtos")
    public ResponseEntity<RelatorioFornecedorDTO> relatorioProdutos(@PathVariable Long id) {
        return ResponseEntity.ok(relatorioService.relatorioPorFornecedor(id));
    }

    @PostMapping
    public ResponseEntity<FornecedorDTO> criar(@Valid @RequestBody FornecedorDTO dto) {
        FornecedorDTO criado = fornecedorService.salvar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FornecedorDTO> atualizar(@PathVariable Long id, @Valid @RequestBody FornecedorDTO dto) {
        return ResponseEntity.ok(fornecedorService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        fornecedorService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
