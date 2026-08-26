package com.cepein.sdd.domain.repository;

import com.cepein.sdd.domain.model.ProdutoFornecedor;
import com.cepein.sdd.domain.model.ProdutoFornecedorId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoFornecedorRepository extends JpaRepository<ProdutoFornecedor, ProdutoFornecedorId> {

    @Query("SELECT pf FROM ProdutoFornecedor pf JOIN FETCH pf.produto WHERE pf.id.idFornecedor = :idFornecedor")
    List<ProdutoFornecedor> findByFornecedorIdWithProduto(@Param("idFornecedor") Long idFornecedor);

    @Query("SELECT pf FROM ProdutoFornecedor pf JOIN FETCH pf.fornecedor WHERE pf.id.idProduto = :idProduto")
    List<ProdutoFornecedor> findByProdutoIdWithFornecedor(@Param("idProduto") Long idProduto);

    boolean existsById_IdFornecedor(Long idFornecedor);
    boolean existsById_IdProduto(Long idProduto);

    void deleteById_IdFornecedor(Long idFornecedor);
    void deleteById_IdProduto(Long idProduto);
}
