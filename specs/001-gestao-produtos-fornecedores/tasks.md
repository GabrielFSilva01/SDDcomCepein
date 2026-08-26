# Tasks: Gestão de Produtos e Fornecedores

**Input**: Design documents from `/specs/001-gestao-produtos-fornecedores/`

**Prerequisites**: [plan.md](file:///c:/Users/conta/Downloads/Ferreira/TesteSddComCepein/SDDcomCepein/specs/001-gestao-produtos-fornecedores/plan.md), [spec.md](file:///c:/Users/conta/Downloads/Ferreira/TesteSddComCepein/SDDcomCepein/specs/001-gestao-produtos-fornecedores/spec.md), [research.md](file:///c:/Users/conta/Downloads/Ferreira/TesteSddComCepein/SDDcomCepein/specs/001-gestao-produtos-fornecedores/research.md), [data-model.md](file:///c:/Users/conta/Downloads/Ferreira/TesteSddComCepein/SDDcomCepein/specs/001-gestao-produtos-fornecedores/data-model.md), [contracts/api-contracts.md](file:///c:/Users/conta/Downloads/Ferreira/TesteSddComCepein/SDDcomCepein/specs/001-gestao-produtos-fornecedores/contracts/api-contracts.md)

## Format: `[ID] [P?] [Story?] Description with file path`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (e.g., US1, US2, US3, US4)

---

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Project initialization and basic directory structure

- [x] T001 Create project directory structure for backend and frontend in `backend/` and `frontend/`
- [x] T002 Initialize Maven Spring Boot 3.x dependencies (Spring Web, Data JPA, PostgreSQL, Validation, Lombok) in `backend/pom.xml`
- [x] T003 [P] Initialize SPA frontend structure with Vite/React in `frontend/package.json` and `frontend/index.html`

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Core infrastructure that MUST be complete before ANY user story can be implemented

- [x] T004 Create PostgreSQL migration script for tables `fornecedores`, `produtos` and `produtos_fornecedores` in `backend/src/main/resources/db/migration/V1__initial_schema.sql`
- [x] T005 [P] Configure PostgreSQL connection properties in `backend/src/main/resources/application.yml`
- [x] T006 [P] Implement global REST exception handler for validation and problem details in `backend/src/main/java/com/cepein/sdd/web/exception/GlobalExceptionHandler.java`
- [x] T007 [P] Setup CORS and security baseline filter in `backend/src/main/java/com/cepein/sdd/config/SecurityConfig.java`

**Checkpoint**: Foundation ready - user story implementation can now begin in parallel

---

## Phase 3: User Story 1 - Gestão do Cadastro Base de Produtos e Fornecedores (Priority: P1) 🎯 MVP

**Goal**: Permitir o cadastro, edição, consulta e exclusão (CRUD) de Produtos e Fornecedores com validação de CNPJ e SKU.

**Independent Test**: Executar requisições de CRUD para Fornecedores e Produtos via REST API e verificar persistência no banco de dados.

### Implementation for User Story 1

- [x] T008 [P] [US1] Create Fornecedor JPA entity in `backend/src/main/java/com/cepein/sdd/domain/model/Fornecedor.java`
- [x] T009 [P] [US1] Create Produto JPA entity in `backend/src/main/java/com/cepein/sdd/domain/model/Produto.java`
- [x] T010 [P] [US1] Create DTOs (FornecedorDTO, ProdutoDTO) with Bean Validation in `backend/src/main/java/com/cepein/sdd/web/dto/FornecedorDTO.java` and `backend/src/main/java/com/cepein/sdd/web/dto/ProdutoDTO.java`
- [x] T011 [P] [US1] Create FornecedorRepository interface in `backend/src/main/java/com/cepein/sdd/domain/repository/FornecedorRepository.java`
- [x] T012 [P] [US1] Create ProdutoRepository interface in `backend/src/main/java/com/cepein/sdd/domain/repository/ProdutoRepository.java`
- [x] T013 [US1] Implement FornecedorService with CRUD and CNPJ validation in `backend/src/main/java/com/cepein/sdd/service/FornecedorService.java`
- [x] T014 [US1] Implement ProdutoService with CRUD and SKU validation in `backend/src/main/java/com/cepein/sdd/service/ProdutoService.java`
- [x] T015 [US1] Create FornecedorController REST endpoints (/api/fornecedores) in `backend/src/main/java/com/cepein/sdd/web/controller/FornecedorController.java`
- [x] T016 [US1] Create ProdutoController REST endpoints (/api/produtos) in `backend/src/main/java/com/cepein/sdd/web/controller/ProdutoController.java`
- [x] T017 [P] [US1] Build Frontend Cadastro Base UI component in `frontend/src/components/CadastroBase.jsx`

**Checkpoint**: User Story 1 is fully functional as MVP.

---

## Phase 4: User Story 2 - Associação de Produtos e Fornecedores N:N (Priority: P1)

**Goal**: Associar Produtos e Fornecedores registrando o Preço de Custo e o Prazo de Entrega em dias na entidade associativa.

**Independent Test**: Selecionar um produto e fornecedor, vincular registrando preço e prazo, e verificar gravação na tabela associativa.

### Implementation for User Story 2

- [x] T018 [P] [US2] Create composite key Embeddable ProdutoFornecedorId in `backend/src/main/java/com/cepein/sdd/domain/model/ProdutoFornecedorId.java`
- [x] T019 [P] [US2] Create associative Entity ProdutoFornecedor in `backend/src/main/java/com/cepein/sdd/domain/model/ProdutoFornecedor.java`
- [x] T020 [P] [US2] Create VinculoDTO for association requests in `backend/src/main/java/com/cepein/sdd/web/dto/VinculoDTO.java`
- [x] T021 [P] [US2] Create ProdutoFornecedorRepository interface in `backend/src/main/java/com/cepein/sdd/domain/repository/ProdutoFornecedorRepository.java`
- [x] T022 [US2] Implement VinculoService for create/update N:N associations with Transactional support in `backend/src/main/java/com/cepein/sdd/service/VinculoService.java`
- [x] T023 [US2] Implement VinculoController endpoints (/api/produtos/{id_produto}/fornecedores/{id_fornecedor}) in `backend/src/main/java/com/cepein/sdd/web/controller/VinculoController.java`
- [x] T024 [P] [US2] Build Frontend Wizard Associações UI component with dropdown search in `frontend/src/components/WizardAssoc.jsx`

**Checkpoint**: User Story 1 AND User Story 2 are functional independently.

---

## Phase 5: User Story 3 - Consulta e Relatórios Cruzados Expansíveis (Priority: P2)

**Goal**: Exibir relatórios consolidados por Fornecedor (Visão A) e por Produto (Visão B) em menos de 3 segundos sem N+1 queries.

**Independent Test**: Consultar os endpoints de relatórios e verificar o retorno estruturado de itens expandidos.

### Implementation for User Story 3

- [x] T025 [P] [US3] Create report projection DTOs (RelatorioFornecedorDTO, RelatorioProdutoDTO) in `backend/src/main/java/com/cepein/sdd/web/dto/RelatorioFornecedorDTO.java` and `backend/src/main/java/com/cepein/sdd/web/dto/RelatorioProdutoDTO.java`
- [x] T026 [US3] Implement JOIN FETCH repository queries to eliminate N+1 queries in `backend/src/main/java/com/cepein/sdd/domain/repository/FornecedorRepository.java` and `backend/src/main/java/com/cepein/sdd/domain/repository/ProdutoRepository.java`
- [x] T027 [US3] Add report endpoints GET /api/fornecedores/{id}/produtos and GET /api/produtos/{id}/fornecedores in `backend/src/main/java/com/cepein/sdd/web/controller/FornecedorController.java` and `backend/src/main/java/com/cepein/sdd/web/controller/ProdutoController.java`
- [x] T028 [P] [US3] Build Frontend Relatório Expansível UI component with collapsible rows in `frontend/src/components/RelatorioExpansivel.jsx`

**Checkpoint**: User Stories 1, 2 e 3 fully functional.

---

## Phase 6: User Story 4 - Segurança RBAC e Remoção Transacional (Priority: P2)

**Goal**: Exigir perfil de Administrador para exclusões e tratar a remoção de vínculos em transação atômica.

**Independent Test**: Simular tentativa de exclusão sem perfil Administrador (403 Forbidden) vs Administrador autorizado (204 No Content com desassociação atômica).

### Implementation for User Story 4

- [x] T029 [US4] Implement RBAC authorization interceptor enforcing ADMIN role on DELETE methods in `backend/src/main/java/com/cepein/sdd/config/RbacInterceptor.java`
- [x] T030 [US4] Add transactional cascade deletion and RN03 validation checks in `backend/src/main/java/com/cepein/sdd/service/FornecedorService.java`
- [x] T031 [P] [US4] Update Frontend UI components to reflect role-based action buttons in `frontend/src/components/CadastroBase.jsx`

---

## Phase 7: Polish & Cross-Cutting Concerns

**Purpose**: Validação final e testes de integração

- [x] T032 [P] Implement REST API controller integration tests in `backend/src/test/java/com/cepein/sdd/web/controller/ApiIntegrationTest.java`
- [x] T033 [P] Execute end-to-end quickstart validation scenarios per `specs/001-gestao-produtos-fornecedores/quickstart.md`

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: Completed
- **Foundational (Phase 2)**: Completed
- **User Stories (Phase 3+)**: Completed
- **Polish (Phase 7)**: Completed
