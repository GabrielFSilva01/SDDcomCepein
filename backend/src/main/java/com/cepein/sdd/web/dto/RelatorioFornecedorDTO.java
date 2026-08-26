package com.cepein.sdd.web.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RelatorioFornecedorDTO {

    private Long idFornecedor;
    private String razaoSocial;
    private String cnpj;
    private List<ProdutoItemDTO> produtos;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProdutoItemDTO {
        private Long idProduto;
        private String skuCodigo;
        private String nome;
        private String categoria;
        private BigDecimal precoCusto;
        private Integer prazoEntregaDias;
    }
}
