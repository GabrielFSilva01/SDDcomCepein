<!--
Sync Impact Report
- Version change: 1.0.0 -> 1.1.0
- Modified principles: Added Principle VI (Documentação Viva e Atualização Contínua do README.md)
- Added sections: Principle VI
- Removed sections: None
- Templates requiring updates:
  - .specify/templates/plan-template.md (✅ aligned)
  - .specify/templates/spec-template.md (✅ aligned)
  - .specify/templates/tasks-template.md (✅ aligned)
- Follow-up TODOs: None
-->

# Sistema de Gestão de Produtos e Fornecedores Constitution

## Core Principles

### I. Integridade Relacional e Modelagem N:N (Tabela Associativa Explícita)
Toda relação Muitos-para-Muitos entre Produtos e Fornecedores MUST ser estruturada por meio de uma tabela associativa dedicada (`produtos_fornecedores`) utilizando Chave Primária Composta pelos identificadores das duas entidades (`id_fornecedor`, `id_produto`). Atributos específicos do vínculo, como Preço de Custo (`preco_custo`) e Prazo de Entrega (`prazo_entrega_dias`), MUST residir estritamente na tabela associativa. No backend Java 21 com Spring Boot, MUST-SE utilizar 3 entidades JPA distintas (mapeando a entidade associativa com `@EmbeddedId` ou `@IdClass`), sendo estritamente proibida a anotação simplificada `@ManyToMany` sem suporte a atributos adicionais de vínculo. O sistema MUST impedir duplicidade de vínculos para o mesmo par produto-fornecedor (RN01, RN02).

### II. Arquitetura Cliente-Servidor Stateless & Contrato RESTful
O sistema MUST adotar uma arquitetura Cliente-Servidor estritamente desacoplada, composta por um Frontend Single Page Application (SPA em React.js ou Vue.js) e um Backend API RESTful stateless em Java 21 com Spring Boot e suporte a banco relacional embarcado/SQLite para execução e testes ágeis. As rotas HTTP MUST seguir rigorosamente os contratos REST padronizados (`/api/fornecedores`, `/api/produtos`, `/api/produtos/{id_produto}/fornecedores/{id_fornecedor}`). Retornos da API MUST respeitar os status HTTP apropriados: 200/201 para sucesso, 400 para erros de validação, 404 para registros não encontrados e 500 para falhas internas. Operações com dependências cascateadas MUST ser envelopadas em Transações ACID.

### III. Validação Rigorosa, Transacionalidade & Segurança RBAC (Autenticação Padrão)
O backend MUST realizar validação estrita de todos os dados de entrada via Spring Boot Validation (`@NotNull`, `@NotBlank`), garantindo formato válido de e-mail, unicidade de CNPJ e código SKU, e `preco_custo` obrigatoriamente maior que zero. O acesso ao sistema MUST exigir autenticação (login padrão: `admin`, senha padrão: `admin123` para testes rápidos). Operações destrutivas (exclusão de registros) MUST ser restritas a perfis com papel "Administrador" (RBAC). É PROIBIDO excluir fornecedores com pendências ativas (RN03); quando a exclusão for permitida, a remoção dos vínculos associados MUST ocorrer de forma automática e transacional.

### IV. Desempenho, Otimização de Consultas & SLA de Disponibilidade
As operações de consulta e geração de relatórios cruzados (Fornecedores com seus Produtos e Produtos com seus Fornecedores - RF05, RF06) MUST responder em tempo inferior a 3 segundos para bases contendo até 10.000 registros (RNF02). As consultas SQL/JPA MUST evitar problemas de N+1 queries utilizando DTOs otimizados e Join Fetch no banco relacional. O sistema MUST ser projetado para garantir disponibilidade 24/7 com taxa de SLA de 99,9% (RNF05).

### V. Usabilidade, Design Anti-AI-Slop (Hallmark) & Módulos SPA
A interface de usuário MUST ser 100% responsiva (adaptando-se a computadores e dispositivos móveis) e seguir as diretrizes Anti-AI-Slop (skill Hallmark): tipografia bem pareada, hierarchy clara, ausência de placeholders genéricos, estados interativos completos e excelente polimento visual. O frontend MUST ser organizado em módulos visuais bem delimitados: (1) Módulo de Login com credenciais padrão (`admin` / `admin123`), (2) Módulo de Cadastro Base para Produtos e Fornecedores, (3) Módulo de Associação em formato Wizard com busca em lista suspensa, e (4) Dashboards de Relatório com tabelas de dados expansíveis.

### VI. Documentação Viva e Atualização Contínua do README.md
A cada interação, execução de comando, adição de funcionalidade ou alteração técnica realizada no projeto, o arquivo `README.md` na raiz do repositório MUST ser atualizado imediatamente. A documentação MUST manter um registro atualizado das credenciais de teste (`admin`/`admin123`), da arquitetura do banco SQLite/relacional, endpoints REST, instrução de execução e histórico de evolução do sistema.

## Diretrizes Técnicas e Tecnologias da Stack

O projeto adota oficialmente a seguinte pilha tecnológica padronizada:
- **Backend**: Java 21 com Spring Boot, Spring Web (`@RestController`), Spring Data JPA (`JpaRepository`), Spring Boot Validation, Driver SQLite / H2 embedded e Lombok.
- **Frontend**: Single Page Application (React.js + Vite) com design refinado (Hallmark Anti-AI-Slop), gestão de estado dinâmico e suporte a componentes expansíveis.
- **Banco de Dados**: Banco Relacional SQLite / H2 embarcado para testes ágeis instantâneos com suporte a migrations.
- **Protocolo de Comunicação**: HTTPS / RESTful API transmitindo dados prioritariamente em JSON.

## Workflow de Desenvolvimento e Garantia de Qualidade

1. **Gestão de Schema & Migrations**: Alterações na estrutura do banco de dados MUST ser feitas por scripts de migração ou DDL versionados.
2. **Garantia de Integridade e Regras**: Toda regra de negócio (RN01, RN02, RN03) e requisito funcional (RF01 a RF07) MUST ser coberto por testes automatizados.
3. **Atualização do README.md**: A cada alteração no repositório, o arquivo `README.md` MUST ser mantido em estado 100% sincronizado e representativo da aplicação.
4. **Verificação dos Templates Spec Kit**: Todas as especificações em `specs/` MUST ser validadas contra as regras descritas nesta Constituição.

## Governance

- **Supremacia da Constituição**: Esta Constituição estabelece as diretrizes arquiteturais e regras não negociáveis do projeto. Qualquer decisão de código ou design que conflite com este documento MUST ser recusada ou submetida ao processo formal de emenda.
- **Processo de Emenda**: Alterações nas regras de negócio, inclusões de novas pilhas de tecnologia ou mudanças nos SLAs requerem revisão documental, justificativa técnica e atualização da versão deste documento.
- **Politica de Versionamento**:
  - **MAJOR**: Remanejamentos ou quebras retroincompatíveis de regras essenciais.
  - **MINOR**: Adição de novos princípios (ex: Princípio VI - README vivo), novos requisitos funcionais ou ampliação da stack tecnológica.
  - **PATCH**: Correções gramaticais, pequenos ajustes de redação ou esclarecimentos sem alteração do escopo de engenharia.

**Version**: 1.1.0 | **Ratified**: 2026-08-26 | **Last Amended**: 2026-08-26
