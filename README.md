# Sistema de Gestão de Produtos e Fornecedores (Modelo N:N & SDD com CEPEIN)

[![Java Version](https://img.shields.io/badge/Java-21-orange.svg)](https://jdk.java.net/21/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.3-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Frontend](https://img.shields.io/badge/Frontend-React%2018%20%2B%20Vite-blue.svg)](https://react.dev/)
[![Design](https://img.shields.io/badge/Design-Hallmark%20Anti--AI--Slop-purple.svg)](#)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
[![Constitution Version](https://img.shields.io/badge/Constitution-v1.1.0-blue.svg)](file:///.specify/memory/constitution.md)

Aplicação desenvolvida para gerenciar **Produtos**, **Fornecedores** e o relacionamento **Muitos-para-Muitos (N:N)** com atributos adicionais de **Preço de Custo** e **Prazo de Entrega (dias)**, seguindo a metodologia **Spec Kit** e a **Constituição do Projeto**.

---

## 🔑 Credenciais para Testes Rápido

Para facilitar os testes de homologação rápida e validação de permissões RBAC:

- **Usuário**: `admin`
- **Senha**: `admin123`
- **Perfil**: `ADMIN` (acesso liberado para cadastros, associações e exclusões transacionais)

---

## 🍴 Como Fazer Fork e Executar o Projeto

Qualquer pessoa pode fazer o **fork** deste repositório e executar a aplicação localmente de forma simples:

1. **Faça o Fork** clicando no botão `Fork` no topo superior direito desta página no GitHub.
2. **Clone seu repositório forkado**:
   ```bash
   git clone https://github.com/SEU_USUARIO/SDDcomCepein.git
   cd SDDcomCepein
   ```
3. **Execute o Backend (Spring Boot / Java 21)**:
   ```bash
   cd backend
   ./mvnw spring-boot:run
   ```
4. **Execute o Frontend (React / Vite)**:
   ```bash
   cd frontend
   npm install
   npm run dev
   ```
Para mais detalhes sobre contribuição, leia o guia em [`CONTRIBUTING.md`](CONTRIBUTING.md).

---

## 🏛️ Arquitetura do Sistema

O sistema adota uma arquitetura Cliente-Servidor desacoplada:

1. **Backend (Java 21 / Spring Boot 3.2.3)**:
   - **API RESTful Stateless**: Exposições de endpoints REST padronizados (`/api/fornecedores`, `/api/produtos`, `/api/produtos/{id_produto}/fornecedores/{id_fornecedor}`).
   - **Mapeamento JPA Relacional N:N**: Mapeamento explícito de 3 entidades JPA (`Fornecedor`, `Produto`, `ProdutoFornecedor`) utilizando chave primária composta `@EmbeddedId` (`ProdutoFornecedorId`).
   - **Banco de Dados Embarcado (SQLite / H2 File)**: Banco de dados embarcado local (`sdd_database`) permitindo testes rápidos instantâneos sem necessidade de subir um container PostgreSQL externo.
   - **Otimização de Consultas (Anti-N+1 Queries)**: Consultas em repositório otimizadas com `JOIN FETCH` e DTO Projections para tempo de resposta < 3 segundos (RNF02).
   - **Segurança & RBAC**: Interceptor [`RbacInterceptor`](file:///c:/Users/conta/Downloads/Ferreira/TesteSddComCepein/SDDcomCepein/backend/src/main/java/com/cepein/sdd/config/RbacInterceptor.java) bloqueia operações `DELETE` sem perfil `ADMIN`.

2. **Frontend SPA (React.js + Vite + Design Hallmark Anti-AI-Slop)**:
   - **Design System Polish**: Estilização Glassmorphism em Dark Mode com feedback interativo nos 8 estados (`default`, `hover`, `:focus-visible`, `:active`, `disabled`, `loading`, `error`, `success`).
   - **Módulo de Autenticação**: Tela de login integrada com credenciais de teste padrão.
   - **Módulo de Cadastro Base**: CRUD completo de Produtos e Fornecedores com filtros de busca dinâmica por CNPJ/SKU/Nome.
   - **Módulo de Associação (Wizard)**: Assistente visual com dropdown de busca para registrar ou atualizar o vínculo N:N registrando preço e prazo.
   - **Dashboards de Relatórios Expansíveis**: Tabela expansível em árvore para a Visão A (Fornecedor -> Produtos) e Visão B (Produto -> Fornecedores).

---

## 🔗 Endpoints Principais da API REST

| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/api/fornecedores?query={termo}` | Lista fornecedores com filtro por Razão Social ou CNPJ. |
| `POST` | `/api/fornecedores` | Cadastra novo fornecedor (valida CNPJ único e e-mail). |
| `GET` | `/api/fornecedores/{id}/produtos` | **Relatório Visão A**: Retorna fornecedor com produtos fornecidos. |
| `DELETE` | `/api/fornecedores/{id}` | Exclui fornecedor e desassocia vínculos de forma atômica (Requer Header `X-User-Role: ADMIN`). |
| `GET` | `/api/produtos?query={termo}` | Lista produtos com filtro por Nome ou SKU. |
| `POST` | `/api/produtos` | Cadastra novo produto (valida SKU único). |
| `GET` | `/api/produtos/{id}/fornecedores` | **Relatório Visão B**: Retorna produto com fornecedores e comparativo de preços. |
| `DELETE` | `/api/produtos/{id}` | Exclui produto e desassocia vínculos de forma atômica (Requer Header `X-User-Role: ADMIN`). |
| `POST` | `/api/produtos/{idProduto}/fornecedores/{idFornecedor}` | Cria/atualiza o vínculo N:N com `precoCusto` (> 0) e `prazoEntregaDias` (>= 0). |
| `DELETE` | `/api/produtos/{idProduto}/fornecedores/{idFornecedor}` | Remove um vínculo associativo específico. |

---

## 📄 Licença

Este projeto está licenciado sob a licença MIT - consulte o arquivo [`LICENSE`](LICENSE) para mais detalhes.
