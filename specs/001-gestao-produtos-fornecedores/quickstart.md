# Quickstart Validation Guide: Gestão de Produtos e Fornecedores

**Feature**: [spec.md](file:///c:/Users/conta/Downloads/Ferreira/TesteSddComCepein/SDDcomCepein/specs/001-gestao-produtos-fornecedores/spec.md) | **Date**: 2026-08-26

Este guia orienta a validação ponta a ponta dos Requisitos e da Constituição do projeto.

---

## 🛠️ Pré-requisitos & Configuração

1. **Ambiente Backend**: Java 21 SDK e Maven configurados.
2. **Banco de Dados**: PostgreSQL em execução (ou H2 em modo de teste relacional).
3. **Frontend**: Node.js instalado (React.js/Vite ou Vue.js).

---

## 🧪 Cenários de Validação Ponta a Ponta

### Cenário 1: Cadastro Base de Fornecedor e Produto
**Objetivo**: Validar a criação e as regras de unicidade de CNPJ e SKU.

1. **Criar Fornecedor**:
   - Enviar requisição POST para `/api/fornecedores` conforme contrato em [`contracts/api-contracts.md`](file:///c:/Users/conta/Downloads/Ferreira/TesteSddComCepein/SDDcomCepein/specs/001-gestao-produtos-fornecedores/contracts/api-contracts.md).
   - **Resultado Esperado**: Retorno HTTP `201 Created` e ID gerado.
2. **Testar CNPJ Duplicado**:
   - Reenviar a mesma requisição POST com o mesmo CNPJ.
   - **Resultado Esperado**: Retorno HTTP `400 Bad Request` informando duplicidade de CNPJ.

### Cenário 2: Associação N:N com Atributos de Custo e Prazo
**Objetivo**: Validar a criação do vínculo na tabela associativa e regras de validação.

1. **Vincular Produto e Fornecedor**:
   - Executar requisição POST em `/api/produtos/1/fornecedores/1` com body `{"precoCusto": 150.00, "prazoEntregaDias": 5}`.
   - **Resultado Esperado**: Retorno HTTP `201 Created` / `200 OK` persistindo na tabela associativa.
2. **Validar Preço Inválido**:
   - Executar requisição POST em `/api/produtos/1/fornecedores/1` com body `{"precoCusto": -10.00, "prazoEntregaDias": 5}`.
   - **Resultado Esperado**: Retorno HTTP `400 Bad Request` rejeitado pela validação.

### Cenário 3: Consulta de Relatórios Expansíveis e Otimização N+1
**Objetivo**: Garantir que as visões A e B respondem rapidamente sem N+1 queries.

1. **Consultar Fornecedor com Produtos (Visão A)**:
   - Executar requisição GET em `/api/fornecedores/1/produtos`.
   - **Resultado Esperado**: Retorno em menos de 3 segundos com a estrutura hierárquica do fornecedor e seus produtos associados.

### Cenário 4: Segurança RBAC e Remoção Transacional
**Objetivo**: Testar perfil de acesso e deleção segura.

1. **Tentativa de Deleção por Usuário Comum**:
   - Enviar requisição DELETE em `/api/produtos/1/fornecedores/1` sem header de Administrador.
   - **Resultado Esperado**: Retorno HTTP `403 Forbidden`.
2. **Deleção por Administrador**:
   - Enviar requisição DELETE com header de Administrador.
   - **Resultado Esperado**: Retorno HTTP `204 No Content` desfazendo o vínculo associativo sem afetar a entidade principal.
