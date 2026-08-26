# Implementation Plan: Gestão de Produtos e Fornecedores

**Branch**: `001-gestao-produtos-fornecedores` | **Date**: 2026-08-26 | **Spec**: [spec.md](file:///c:/Users/conta/Downloads/Ferreira/TesteSddComCepein/SDDcomCepein/specs/001-gestao-produtos-fornecedores/spec.md)

**Input**: Feature specification from [`specs/001-gestao-produtos-fornecedores/spec.md`](file:///c:/Users/conta/Downloads/Ferreira/TesteSddComCepein/SDDcomCepein/specs/001-gestao-produtos-fornecedores/spec.md)

## Summary

Implementar a arquitetura completa do sistema de **Gestão de Produtos e Fornecedores**, contemplando o cadastro base (CRUD de Produtos e Fornecedores), a associação Muitos-para-Muitos (N:N) com campos adicionais de Preço de Custo e Prazo de Entrega, relatórios cruzados otimizados (Visões A e B) e controle de exclusão transacional com RBAC. A solução adota uma arquitetura Cliente-Servidor desacoplada com backend Java 21 / Spring Boot 3.x, banco de dados PostgreSQL e frontend Single Page Application (SPA).

## Technical Context

**Language/Version**: Java 21 (Backend) e HTML5/JavaScript (Frontend)

**Primary Dependencies**: Spring Boot 3.x (Spring Web, Spring Data JPA, Spring Boot Validation, Lombok, Driver PostgreSQL)

**Storage**: Banco de Dados Relacional PostgreSQL (Tabelas `fornecedores`, `produtos` e `produtos_fornecedores`)

**Testing**: JUnit 5, Mockito, Spring Boot Test (`@SpringBootTest`, `@WebMvcTest`)

**Target Platform**: Navegadores Web Modernos (Desktop e Dispositivos Móveis)

**Project Type**: Web Application (Backend API RESTful + Frontend SPA)

**Performance Goals**: Tempo de resposta < 3 segundos para relatórios N:N com até 10.000 registros (RNF02)

**Constraints**: Disponibilidade 99,9% (RNF05), eliminação de N+1 queries, obrigatoriedade de transações ACID em escritas/deleções

**Scale/Scope**: Catálogo N:N extensível, formulários de associação dinâmica em formato Wizard e relatórios com grades expansíveis

## Constitution Check

*GATE: Passed post-design evaluation.*

1. **Modelagem N:N Explícita**: ✅ Aprovado. A relação N:N é mapeada por 3 Entidades JPA dedicadas (`Fornecedor`, `Produto`, `ProdutoFornecedor`) utilizando `@EmbeddedId` (`ProdutoFornecedorId`). Proibido `@ManyToMany` simples sem atributos de vínculo.
2. **Arquitetura Cliente-Servidor Stateless**: ✅ Aprovado. Backend expõe API RESTful em `/api/fornecedores` e `/api/produtos` com status HTTP padronizados (200/201, 400, 404, 500).
3. **Validação & Segurança RBAC**: ✅ Aprovado. DTOs validados via Spring Validation (`@NotNull`, `@NotBlank`, `@DecimalMin`). Exclusões restritas ao perfil Administrador.
4. **Desempenho & N+1 Queries**: ✅ Aprovado. Consultas de relatórios usam `JOIN FETCH` e DTO Projections para resposta < 3s.
5. **Usabilidade & Módulos SPA**: ✅ Aprovado. Frontend estruturado nos 3 módulos: Cadastro Base, Wizard de Associação e Dashboards Expansíveis.

## Project Structure

### Documentation (this feature)

```text
specs/001-gestao-produtos-fornecedores/
├── plan.md              # Este plano de implementação
├── research.md          # Pesquisa de arquitetura JPA N:N e otimizações
├── data-model.md        # Schema SQL e entidades JPA
├── quickstart.md        # Guia de validação e execução dos cenários
└── contracts/           # Contratos RESTful JSON
    └── api-contracts.md
```

### Source Code (repository root)

```text
backend/
├── src/
│   ├── main/
│   │   ├── java/com/cepein/sdd/
│   │   │   ├── domain/
│   │   │   │   ├── model/ (Fornecedor, Produto, ProdutoFornecedor, ProdutoFornecedorId)
│   │   │   │   └── repository/ (FornecedorRepository, ProdutoRepository, ProdutoFornecedorRepository)
│   │   │   ├── service/ (FornecedorService, ProdutoService, VinculoService)
│   │   │   ├── web/
│   │   │   │   ├── controller/ (FornecedorController, ProdutoController, VinculoController)
│   │   │   │   ├── dto/ (DTOs de Entrada e Saída para REST)
│   │   │   │   └── exception/ (GlobalExceptionHandler)
│   │   │   └── config/ (Database & Security Config)
│   │   └── resources/
│   │       ├── application.yml
│   │       └── db/migration/ (Flyway/Liquibase SQL Migrations)
│   └── test/
└── frontend/
    ├── src/
    │   ├── components/ (CadastroBase, WizardAssoc, RelatorioExpansivel)
    │   ├── services/ (api.js)
    │   └── App.js
    └── index.html
```

**Structure Decision**: Selecionada a Estrutura de Aplicação Web (Opção 2 - `backend/` e `frontend/`) para manter o desacoplamento estrito entre a API REST e a interface SPA, conforme exigido pelo SDD e pela Constituição do Projeto.

## Complexity Tracking

> **Sem violações da Constituição. Toda a complexidade proposta é necessária para o atendimento dos requisitos N:N.**
