# Technical Research: Gestão de Produtos e Fornecedores

**Feature**: [spec.md](file:///c:/Users/conta/Downloads/Ferreira/TesteSddComCepein/SDDcomCepein/specs/001-gestao-produtos-fornecedores/spec.md) | **Date**: 2026-08-26

## 1. Mapeamento JPA do Relacionamento N:N com Atributos de Vínculo

### Decision
Mapear a tabela associativa `produtos_fornecedores` utilizando uma entidade JPA dedicada `ProdutoFornecedor` com chave primária composta representada por uma classe `@Embeddable` (`ProdutoFornecedorId`).

### Rationale
A anotação padrão `@ManyToMany` do JPA/Hibernate é restrita a tabelas associativas simples (que contêm apenas as Chaves Estrangeiras). Como o modelo do projeto exige atributos adicionais no vínculo (`preco_custo` e `prazo_entrega_dias`), a abordagem recomendada pelo Hibernate e exigida pela Constituição (Princípio I) é a criação de 3 entidades JPA:
- `Fornecedor` (`@Entity`)
- `Produto` (`@Entity`)
- `ProdutoFornecedor` (`@Entity`) contendo `@EmbeddedId ProdutoFornecedorId` e mapeando `@ManyToOne` com `@MapsId`.

### Alternatives Considered
- **`@ManyToMany` Direto**: Rejeitado porque impede o mapeamento e atualização dos campos `preco_custo` e `prazo_entrega_dias`.
- **ID Surrogate Único (`@GeneratedValue id`)**: Rejeitado em favor da chave primária composta nativa (`id_fornecedor`, `id_produto`), que assegura a restrição relacional de unicidade do vínculo no banco de dados sem depender apenas de índices de constraint adicionais.

---

## 2. Desempenho e Prevenção de N+1 Queries em Relatórios Cruzados

### Decision
Utilizar consultas otimizadas via Spring Data JPA (`@Query` com `JOIN FETCH` ou `@EntityGraph`) e projeção direta em Data Transfer Objects (DTOs) na camada de repositório para a construção das Visões A e B dos relatórios (RF05 e RF06).

### Rationale
O Requisito Não Funcional RNF02 e o Princípio IV da Constituição estabelecem que as consultas de relatórios cruzados N:N devem responder em menos de 3 segundos para bases com até 10.000 registros. A navegação preguiçosa (`FetchType.LAZY`) convencional gera 1 consulta inicial mais N consultas adicionais para cada item expandido (N+1 queries). O `JOIN FETCH` consolida a busca em uma única instrução SQL efetuada no PostgreSQL.

### Alternatives Considered
- **In-Memory Joining (Carregamento total e junção no Frontend)**: Rejeitado por consumir muita memória no navegador e transferir dados desnecessários via rede.
- **Eager Loading Global (`FetchType.EAGER`)**: Rejeitado por degradar a performance de consultas simples do cadastro base onde os relacionamentos não são necessários.

---

## 3. Gestão de Transações e Tratamento de Erros de Negócio

### Decision
Envelopar todas as operações de associação, edição e exclusão no Spring Boot com `@Transactional` e tratar exceções com um `@RestControllerAdvice` centralizado retornado respostas padronizadas no formato RFC 7807 (Problem Details).

### Rationale
Assegura que exclusões de fornecedores ou atualizações de vínculos ocorram de forma atômica (Princípio III da Constituição). Caso ocorra falha ao remover um vínculo associativo, a transação sofre rollback automático. O `@RestControllerAdvice` converte violações de validação (`MethodArgumentNotValidException`) e erros de integridade em códigos HTTP apropriados (400 Bad Request, 404 Not Found, 500 Internal Error) sem expor detalhes internos da infraestrutura.

### Alternatives Considered
- **Tratamento manual com blocos try-catch em cada Controller**: Rejeitado por duplicar código e ferir a padronização RESTful.
