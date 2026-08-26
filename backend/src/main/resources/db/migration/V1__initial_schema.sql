-- Tabela de Fornecedores
CREATE TABLE IF NOT EXISTS fornecedores (
    id_fornecedor BIGSERIAL PRIMARY KEY,
    razao_social VARCHAR(255) NOT NULL,
    cnpj VARCHAR(18) NOT NULL UNIQUE,
    telefone VARCHAR(20),
    email VARCHAR(255) NOT NULL,
    data_cadastro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_fornecedores_cnpj ON fornecedores(cnpj);
CREATE INDEX IF NOT EXISTS idx_fornecedores_razao ON fornecedores(razao_social);

-- Tabela de Produtos
CREATE TABLE IF NOT EXISTS produtos (
    id_produto BIGSERIAL PRIMARY KEY,
    sku_codigo VARCHAR(50) NOT NULL UNIQUE,
    nome VARCHAR(255) NOT NULL,
    descricao TEXT,
    categoria VARCHAR(100),
    data_cadastro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_produtos_sku ON produtos(sku_codigo);
CREATE INDEX IF NOT EXISTS idx_produtos_nome ON produtos(nome);

-- Tabela Associativa Produtos_Fornecedores (Relacionamento N:N com atributos de custo e prazo)
CREATE TABLE IF NOT EXISTS produtos_fornecedores (
    id_fornecedor BIGINT NOT NULL,
    id_produto BIGINT NOT NULL,
    preco_custo DECIMAL(12, 2) NOT NULL CHECK (preco_custo > 0),
    prazo_entrega_dias INT NOT NULL CHECK (prazo_entrega_dias >= 0),
    PRIMARY KEY (id_fornecedor, id_produto),
    CONSTRAINT fk_pf_fornecedor FOREIGN KEY (id_fornecedor) REFERENCES fornecedores(id_fornecedor) ON DELETE CASCADE,
    CONSTRAINT fk_pf_produto FOREIGN KEY (id_produto) REFERENCES produtos(id_produto) ON DELETE CASCADE
);
