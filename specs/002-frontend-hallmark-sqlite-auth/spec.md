# Feature Specification: Frontend Anti-AI-Slop (Hallmark), SQLite Embedded & Autenticação Padrão

**Feature Branch**: `002-frontend-hallmark-sqlite-auth`

**Created**: 2026-08-26

**Status**: Draft

**Input**: Atualização da interface de usuário utilizando os princípios de design Anti-AI-Slop (skill Hallmark), inclusão de tela de login com credenciais padrão (`admin`/`admin123`), transição do backend para banco de dados embarcado SQLite para testes rápidos, e atualização viva da documentação em `README.md`.

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Autenticação Rápida com Credenciais Padrão (Priority: P1)

Como Usuário/Administrador de testes, quero realizar login na aplicação com um usuário e senha padrão (`admin` / `admin123`), para acessar rapidamente o sistema sem complicação na fase de testes.

**Why this priority**: Permite liberar o acesso aos módulos cadastrais e relatórios mantendo a validação de segurança RBAC.

**Independent Test**: Informar `admin` e `admin123` na tela de login e verificar redirecionamento para o dashboard principal; tentar credenciais incorretas e verificar mensagem de erro.

**Acceptance Scenarios**:

1. **Given** que estou na tela de Login, **When** preencho usuário `admin` e senha `admin123` e clico em Entrar, **Then** recebo confirmação de acesso e sou redirecionado para os cadastros com o perfil Administrador ativo.
2. **Given** que estou na tela de Login, **When** preencho senha incorreta, **Then** o sistema exibe alerta visual de erro e bloqueia a entrada.

---

### User Story 2 - Interface Frontend Refinada Anti-AI-Slop (Hallmark) (Priority: P1)

Como Usuário do sistema, desejo utilizar uma interface moderna, elegante, sem visual genérico ("AI-slop"), com resposta a múltiplos estados interativos e tipografia legível.

**Why this priority**: Garante que o visual pareça construído sob medida, oferecendo alta usabilidade e estética profissional.

**Independent Test**: Navegar pelas abas e formulários verificando os efeitos visuais, contraste legível, estados de hover/focus e adaptabilidade responsiva em telas móveis e desktop.

**Acceptance Scenarios**:

1. **Given** que navego pela aplicação, **When** alterno entre as abas e passo o cursor sobre botões e tabelas, **Then** a interface responde suavemente com feedback visual refinado.

---

### User Story 3 - Execução Local com Banco SQLite Embutido (Priority: P1)

Como Desenvolvedor/Testador, quero que o backend funcione com banco de dados SQLite embarcado (`sdd_database.db`), para que a aplicação possa rodar instantaneamente sem depender de um banco PostgreSQL externo.

**Why this priority**: Agiliza a inicialização e os testes locais sem necessidade de contêineres ou instalações adicionais.

**Independent Test**: Iniciar a aplicação Java Spring Boot e verificar a criação automática do banco SQLite local e persistência dos dados entre reinicializações.

**Acceptance Scenarios**:

1. **Given** que o backend é iniciado, **When** realizo cadastros e associações N:N, **Then** os dados são gravados no arquivo de banco SQLite local `sdd_database.db`.

---

### User Story 4 - Documentação Viva no README.md (Priority: P1)

Como Desenvolvedor/Stakeholder, desejo que o arquivo `README.md` seja automaticamente mantido e atualizado a cada interação, para que a documentação reflita o estado real do projeto.

**Why this priority**: Atende ao Princípio VI da Constituição do Projeto.

**Independent Test**: Verificar se o arquivo `README.md` na raiz contém todas as instruções de execução, credenciais padrão, arquitetura e rotas da API.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: O sistema MUST disponibilizar tela de Login aceitando usuário `admin` e senha `admin123` para acesso rápido.
- **FR-002**: O sistema MUST aplicar as diretrizes de design Hallmark (Anti-AI-Slop): tema visual escuro/glassmorphism polido, tipografia pareada, estados `:hover`, `:focus-visible`, `:active` e layout responsivo.
- **FR-003**: O backend Spring Boot MUST utilizar banco de dados embarcado SQLite (`jdbc:sqlite:sdd_database.db` / H2 modo SQLite) para testes locais de alta velocidade.
- **FR-004**: O repositório MUST ter o arquivo `README.md` continuamente atualizado com a documentação do projeto.

### Key Entities

- **UsuarioSessao**: Representa a sessão ativa do usuário autenticado (`usuario`, `perfil`, `autenticado`).

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: Usuários conseguem autenticar-se na tela de Login em menos de 5 segundos utilizando o login padrão `admin`/`admin123`.
- **SC-002**: A aplicação inicializa o banco SQLite embarcado local em menos de 3 segundos sem erros de conexão.
- **SC-003**: 100% dos botões e formulários possuem feedback visual refinado (Anti-AI-Slop).
- **SC-004**: O arquivo `README.md` permanece atualizado após cada interação.

## Assumptions

- O arquivo SQLite `sdd_database.db` será mantido no diretório backend durante os testes.
