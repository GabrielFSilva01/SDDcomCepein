package com.cepein.sdd.config;

import com.cepein.sdd.domain.model.Fornecedor;
import com.cepein.sdd.domain.model.Produto;
import com.cepein.sdd.domain.model.ProdutoFornecedor;
import com.cepein.sdd.domain.model.ProdutoFornecedorId;
import com.cepein.sdd.domain.repository.FornecedorRepository;
import com.cepein.sdd.domain.repository.ProdutoFornecedorRepository;
import com.cepein.sdd.domain.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final FornecedorRepository fornecedorRepository;
    private final ProdutoRepository produtoRepository;
    private final ProdutoFornecedorRepository produtoFornecedorRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (fornecedorRepository.count() == 0 && produtoRepository.count() == 0) {
            // Seed Fornecedores
            Fornecedor f1 = new Fornecedor();
            f1.setRazaoSocial("TechSupply Eletrônicos Ltda");
            f1.setCnpj("12345678000195");
            f1.setEmail("contato@techsupply.com");
            f1 = fornecedorRepository.save(f1);

            Fornecedor f2 = new Fornecedor();
            f2.setRazaoSocial("Global Componentes SA");
            f2.setCnpj("98765432000110");
            f2.setEmail("vendas@globalcomponentes.com");
            f2 = fornecedorRepository.save(f2);

            // Seed Produtos
            Produto p1 = new Produto();
            p1.setNome("Monitor UltraWide 29 IPS");
            p1.setSku("MON-UW29-IPS");
            p1 = produtoRepository.save(p1);

            Produto p2 = new Produto();
            p2.setNome("Teclado Mecânico RGB Switch Blue");
            p2.setSku("TEC-MEC-RGB");
            p2 = produtoRepository.save(p2);

            // Seed N:N Vínculos
            ProdutoFornecedor pf1 = new ProdutoFornecedor();
            pf1.setId(new ProdutoFornecedorId(f1.getId(), p1.getId()));
            pf1.setFornecedor(f1);
            pf1.setProduto(p1);
            pf1.setPrecoCusto(new BigDecimal("1250.00"));
            pf1.setPrazoEntregaDias(5);
            produtoFornecedorRepository.save(pf1);

            ProdutoFornecedor pf2 = new ProdutoFornecedor();
            pf2.setId(new ProdutoFornecedorId(f2.getId(), p1.getId()));
            pf2.setFornecedor(f2);
            pf2.setProduto(p1);
            pf2.setPrecoCusto(new BigDecimal("1190.50"));
            pf2.setPrazoEntregaDias(7);
            produtoFornecedorRepository.save(pf2);

            ProdutoFornecedor pf3 = new ProdutoFornecedor();
            pf3.setId(new ProdutoFornecedorId(f1.getId(), p2.getId()));
            pf3.setFornecedor(f1);
            pf3.setProduto(p2);
            pf3.setPrecoCusto(new BigDecimal("299.90"));
            pf3.setPrazoEntregaDias(3);
            produtoFornecedorRepository.save(pf3);
        }
    }
}
