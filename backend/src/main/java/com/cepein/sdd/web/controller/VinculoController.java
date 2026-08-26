package com.cepein.sdd.web.controller;

import com.cepein.sdd.service.VinculoService;
import com.cepein.sdd.web.dto.VinculoDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/produtos/{idProduto}/fornecedores/{idFornecedor}")
@RequiredArgsConstructor
public class VinculoController {

    private final VinculoService vinculoService;

    @PostMapping
    public ResponseEntity<VinculoDTO> salvarVinculo(
            @PathVariable Long idProduto,
            @PathVariable Long idFornecedor,
            @Valid @RequestBody VinculoDTO dto) {
        return ResponseEntity.ok(vinculoService.salvarVinculo(idProduto, idFornecedor, dto));
    }

    @DeleteMapping
    public ResponseEntity<Void> removerVinculo(
            @PathVariable Long idProduto,
            @PathVariable Long idFornecedor) {
        vinculoService.removerVinculo(idProduto, idFornecedor);
        return ResponseEntity.noContent().build();
    }
}
