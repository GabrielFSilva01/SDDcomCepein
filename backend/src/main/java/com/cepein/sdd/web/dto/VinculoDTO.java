package com.cepein.sdd.web.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VinculoDTO {

    private Long idFornecedor;
    private Long idProduto;

    @NotNull(message = "Preço de custo é obrigatório")
    @DecimalMin(value = "0.01", message = "Preço de custo deve ser maior que zero")
    private BigDecimal precoCusto;

    @NotNull(message = "Prazo de entrega é obrigatório")
    @Min(value = 0, message = "Prazo de entrega não pode ser negativo")
    private Integer prazoEntregaDias;

    private String mensagem;
}
