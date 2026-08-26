package com.cepein.sdd.web.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RelatorioProdutoDTO {

    private Long idProduto;
    private String skuCodigo;
    private String nome;
    private String categoria;
    private List<FornecedorItemDTO> fornecedores;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FornecedorItemDTO {
        private Long idFornecedor;
        private String razaoSocial;
        private String cnpj;
        private BigDecimal precoCusto;
        private Integer prazoEntregaDias;
    }
}
