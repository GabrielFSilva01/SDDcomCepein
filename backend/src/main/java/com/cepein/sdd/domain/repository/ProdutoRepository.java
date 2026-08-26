package com.cepein.sdd.domain.repository;

import com.cepein.sdd.domain.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    boolean existsBySkuCodigo(String skuCodigo);
    boolean existsBySkuCodigoAndIdProdutoNot(String skuCodigo, Long idProduto);

    @Query("SELECT p FROM Produto p WHERE " +
           "LOWER(p.nome) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(p.skuCodigo) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Produto> searchByQuery(@Param("query") String query);
}
