package com.cepein.sdd.domain.repository;

import com.cepein.sdd.domain.model.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FornecedorRepository extends JpaRepository<Fornecedor, Long> {
    boolean existsByCnpj(String cnpj);
    boolean existsByCnpjAndIdFornecedorNot(String cnpj, Long idFornecedor);

    @Query("SELECT f FROM Fornecedor f WHERE " +
           "LOWER(f.razaoSocial) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "f.cnpj LIKE CONCAT('%', :query, '%')")
    List<Fornecedor> searchByQuery(@Param("query") String query);
}
