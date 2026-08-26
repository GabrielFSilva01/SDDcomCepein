# Feature Specification: Gestão de Produtos e Fornecedores

**Feature Branch**: `001-gestao-produtos-fornecedores`

**Created**: 2026-08-26

**Status**: Draft

**Input**: Especificação funcional derivada dos requisitos e Software Design Document (SDD) para Gestão de Produtos, Fornecedores e Vínculos N:N.

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Gestão do Cadastro Base de Produtos e Fornecedores (Priority: P1)

Como Administrador do sistema, desejo cadastrar, visualizar, editar e remover produtos e fornecedores de forma independente, para manter os catálogos operacionais da empresa atualizados.

**Why this priority**: É o requisito fundamental sem o qual nenhum relacionamento N:N ou relatório pode ser construído.

**Independent Test**: Pode ser testado executando o CRUD de Produtos e Fornecedores isoladamente, garantindo que registros são persistidos com validações de CNPJ e SKU.

**Acceptance Scenarios**:

1. **Given** que estou na tela de cadastro de fornecedores, **When** insiro Razão Social, CNPJ válido e dados de contato e confirmo, **Then** o fornecedor é cadastrado com sucesso e exibido na listagem.
2. **Given** que já existe um produto com o SKU "CAD-001", **When** tento cadastrar outro produto com o mesmo SKU "CAD-001", **Then** o sistema recusa a inserção e exibe uma mensagem de erro de duplicidade.

---

### User Story 2 - Associação de Produtos e Fornecedores com Dados de Custo (Priority: P1)

Como Gestor de Compras, desejo associar um produto a um ou mais fornecedores registrando o preço de custo e o prazo de entrega específico de cada fornecedor, para que possamos comparar e controlar os custos de suprimentos.

**Why this priority**: Representa a regra de negócio principal (relacionamento N:N com atributos adicionais de preço e prazo).

**Independent Test**: Pode ser testado selecionando um produto existente e um fornecedor existente no assistente de associação, informando preço de custo e prazo de entrega e verificando a gravação do vínculo na tabela associativa.

**Acceptance Scenarios**:

1. **Given** um produto X e um fornecedor Y sem vínculo prévio, **When** associo o fornecedor Y ao produto X informando o preço de custo R$ 150,00 e prazo de 5 dias, **Then** o vínculo é estabelecido e os atributos de custo e prazo são gravados com sucesso.
2. **Given** um produto X e fornecedor Y que já estão vinculados, **When** tento realizar a associação do mesmo par novamente informando um novo preço de R$ 140,00, **Then** o sistema atualiza o vínculo existente em vez de gerar um registro duplicado.

---

### User Story 3 - Consulta e Relatórios Cruzados Expansíveis (Priority: P2)

Como Analista de Suprimentos, desejo visualizar relatórios consolidados por Fornecedor (listando seus produtos) e por Produto (listando seus fornecedores com comparativo de custos), com suporte a filtros rápidos.

**Why this priority**: Proporciona visibilidade gerencial para tomada de decisão na aquisição de materiais.

**Independent Test**: Pode ser testado acessando as visões de relatório, realizando buscas por nome/CNPJ/SKU e expandindo as linhas para verificar a exibição dos itens relacionados.

**Acceptance Scenarios**:

1. **Given** a visão de relatório de Fornecedores, **When** clico em uma linha da tabela de fornecedores, **Then** um painel expansível é exibido listando todos os produtos fornecidos por ele com seus respectivos preços de custo e prazos.
2. **Given** a visão de relatório de Produtos, **When** digito o nome ou código de um produto na barra de busca, **Then** a grade filtra instantaneamente exibindo o produto e a lista comparativa de fornecedores que o comercializam.

---

### User Story 4 - Segurança de Acesso e Remoção Transacional (Priority: P2)

Como Administrador do sistema, desejo garantir que apenas usuários autorizados possam excluir registros e que a remoção de fornecedores trate transacionalmente os vínculos associados sem deixar dados órfãos.

**Why this priority**: Garante a integridade referencial do banco de dados e impede acessos/exclusões indevidas.

**Independent Test**: Pode ser testado tentando realizar exclusões com perfis sem permissão e verificando se a exclusão de um fornecedor sem pendências remove automaticamente seus vínculos associativos de forma transacional.

**Acceptance Scenarios**:

1. **Given** um usuário comum (não administrador), **When** tenta acionar o botão de exclusão de um fornecedor ou produto, **Then** o sistema nega o acesso e exibe aviso de permissão insuficiente.
2. **Given** um fornecedor sem pendências ativas, **When** o Administrador confirma a sua exclusão, **Then** o fornecedor e todos os seus vínculos na tabela associativa são removidos atomicamente.

---

### Edge Cases

- **Preço de Custo Inválido**: O sistema impede a gravação e notifica o usuário se o preço de custo informado for menor ou igual a zero (`preco_custo <= 0`).
- **Fornecedor com Pendências Ativas**: Se houver tentativa de exclusão de um fornecedor que possua pendências operacionais ativas (RN03), o sistema cancela a operação e informa o motivo da restrição.
- **Busca Sem Resultados**: Se uma pesquisa por filtro não encontrar nenhum registro correspondente, o sistema exibe um estado visual amigável informando "Nenhum produto ou fornecedor encontrado".

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: O sistema MUST permitir o cadastro, leitura, edição e exclusão (CRUD) de Fornecedores com os campos Razão Social, CNPJ (único), Telefone, E-mail e Data de Cadastro.
- **FR-002**: O sistema MUST permitir o cadastro, leitura, edição e exclusão (CRUD) de Produtos com os campos Nome, Código SKU (único), Descrição, Categoria e Data de Cadastro.
- **FR-003**: O sistema MUST permitir associar e desassociar Produtos e Fornecedores (relacionamento N:N), registrando obrigatoriamente Preço de Custo e Prazo de Entrega (dias).
- **FR-004**: O sistema MUST impedir duplicidade de vínculos para o mesmo par Produto-Fornecedor, tratando re-associações como atualização dos atributos de vínculo.
- **FR-005**: O sistema MUST validar os dados de entrada, garantindo formato de e-mail válido, CNPJ válido, unicidade de CNPJ/SKU e `preco_custo > 0`.
- **FR-006**: O sistema MUST fornecer relatório expansível de Fornecedores exibindo a lista detalhada de produtos fornecidos e seus custos.
- **FR-007**: O sistema MUST fornecer relatório expansível de Produtos exibindo a lista comparativa de fornecedores e seus prazos/custos.
- **FR-008**: O sistema MUST disponibilizar mecanismo de busca e filtragem dinâmica por Nome, CNPJ ou Código SKU.
- **FR-009**: O sistema MUST restringir operações de exclusão exclusivamente a usuários autenticados com perfil "Administrador".
- **FR-010**: O sistema MUST executar exclusões permitidas e remoções de vínculo em Transações de Banco de Dados (ACID) com rollback em caso de falha.

### Key Entities

- **Fornecedor**: Representa a empresa fornecedora de produtos (`id_fornecedor`, `razao_social`, `cnpj`, `telefone`, `email`, `data_cadastro`).
- **Produto**: Representa o item do catálogo (`id_produto`, `sku_codigo`, `nome`, `descricao`, `categoria`, `data_cadastro`).
- **ProdutoFornecedor**: Entidade associativa do relacionamento N:N (`id_fornecedor`, `id_produto`, `preco_custo`, `prazo_entrega_dias`).

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: Gestores conseguem cadastrar produtos, fornecedores e realizar a associação completa em menos de 2 minutos por fluxo.
- **SC-002**: Relatórios cruzados de N:N (RF05/RF06) carregam e exibem seus dados detalhados em menos de 3 segundos para bases contendo até 10.000 registros.
- **SC-003**: 100% das tentativas de inserção de CNPJ ou SKU duplicados, ou preços de custo inválidos (<= 0), são bloqueadas na camada de validação.
- **SC-004**: Zero ocorrências de registros órfãos ou inconsistências referenciais na tabela associativa após exclusões autorizadas.

## Assumptions

- O banco de dados relacional PostgreSQL estará devidamente configurado e suportará Chaves Primárias Compostas.
- A autenticação e o perfil do usuário ("Administrador" vs "Usuário") serão validados através da API RESTful stateless.
