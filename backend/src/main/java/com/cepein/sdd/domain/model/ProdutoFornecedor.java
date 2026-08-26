package com.cepein.sdd.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "produtos_fornecedores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProdutoFornecedor {

    @EmbeddedId
    private ProdutoFornecedorId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idFornecedor")
    @JoinColumn(name = "id_fornecedor")
    private Fornecedor fornecedor;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idProduto")
    @JoinColumn(name = "id_produto")
    private Produto produto;

    @Column(name = "preco_custo", nullable = false, precision = 12, scale = 2)
    private BigDecimal precoCusto;

    @Column(name = "prazo_entrega_dias", nullable = false)
    private Integer prazoEntregaDias;
}
