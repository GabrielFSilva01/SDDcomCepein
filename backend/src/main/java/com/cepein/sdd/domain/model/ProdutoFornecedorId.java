package com.cepein.sdd.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class ProdutoFornecedorId implements Serializable {

    @Column(name = "id_fornecedor")
    private Long idFornecedor;

    @Column(name = "id_produto")
    private Long idProduto;
}
