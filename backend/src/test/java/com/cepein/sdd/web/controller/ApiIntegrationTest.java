package com.cepein.sdd.web.controller;

import com.cepein.sdd.web.dto.FornecedorDTO;
import com.cepein.sdd.web.dto.ProdutoDTO;
import com.cepein.sdd.web.dto.VinculoDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Deve cadastrar Fornecedor e Produto e realizar associação N:N com sucesso")
    void testFluxoCompletoVinculoNN() throws Exception {
        // 1. Cadastrar Fornecedor
        FornecedorDTO fornDTO = FornecedorDTO.builder()
                .razaoSocial("Tech Supplies Ltda")
                .cnpj("11.222.333/0001-44")
                .email("contato@techsupplies.com")
                .telefone("(11) 98888-7777")
                .build();

        String fornJson = mockMvc.perform(post("/api/fornecedores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fornDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idFornecedor").exists())
                .andReturn().getResponse().getContentAsString();

        Long idFornecedor = objectMapper.readTree(fornJson).get("idFornecedor").asLong();

        // 2. Cadastrar Produto
        ProdutoDTO prodDTO = ProdutoDTO.builder()
                .skuCodigo("TECL-RGB-01")
                .nome("Teclado Mecânico RGB")
                .categoria("Periféricos")
                .descricao("Teclado mecânico switch azul")
                .build();

        String prodJson = mockMvc.perform(post("/api/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(prodDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idProduto").exists())
                .andReturn().getResponse().getContentAsString();

        Long idProduto = objectMapper.readTree(prodJson).get("idProduto").asLong();

        // 3. Vincular Produto e Fornecedor (N:N)
        VinculoDTO vinculoDTO = VinculoDTO.builder()
                .precoCusto(new BigDecimal("250.00"))
                .prazoEntregaDias(3)
                .build();

        mockMvc.perform(post("/api/produtos/" + idProduto + "/fornecedores/" + idFornecedor)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vinculoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.precoCusto").value(250.00))
                .andExpect(jsonPath("$.prazoEntregaDias").value(3));

        // 4. Verificar Relatório Visão A (Fornecedor -> Produtos)
        mockMvc.perform(get("/api/fornecedores/" + idFornecedor + "/produtos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.produtos[0].skuCodigo").value("TECL-RGB-01"))
                .andExpect(jsonPath("$.produtos[0].precoCusto").value(250.00));

        // 5. Verificar Relatório Visão B (Produto -> Fornecedores)
        mockMvc.perform(get("/api/produtos/" + idProduto + "/fornecedores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fornecedores[0].cnpj").value("11.222.333/0001-44"));
    }

    @Test
    @DisplayName("Deve negar exclusão quando usuário não for ADMIN (RBAC)")
    void testNegarExclusaoSemRoleAdmin() throws Exception {
        mockMvc.perform(delete("/api/fornecedores/999"))
                .andExpect(status().isForbidden());
    }
}
