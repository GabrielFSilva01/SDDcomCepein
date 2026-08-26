package com.cepein.sdd.web.controller;

import com.cepein.sdd.service.ProdutoService;
import com.cepein.sdd.service.RelatorioService;
import com.cepein.sdd.web.dto.ProdutoDTO;
import com.cepein.sdd.web.dto.RelatorioProdutoDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService produtoService;
    private final RelatorioService relatorioService;

    @GetMapping
    public ResponseEntity<List<ProdutoDTO>> listarTodos(@RequestParam(required = false) String query) {
        return ResponseEntity.ok(produtoService.listarTodos(query));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(produtoService.buscarPorId(id));
    }

    @GetMapping("/{id}/fornecedores")
    public ResponseEntity<RelatorioProdutoDTO> relatorioFornecedores(@PathVariable Long id) {
        return ResponseEntity.ok(relatorioService.relatorioPorProduto(id));
    }

    @PostMapping
    public ResponseEntity<ProdutoDTO> criar(@Valid @RequestBody ProdutoDTO dto) {
        ProdutoDTO criado = produtoService.salvar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoDTO> atualizar(@PathVariable Long id, @Valid @RequestBody ProdutoDTO dto) {
        return ResponseEntity.ok(produtoService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        produtoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
