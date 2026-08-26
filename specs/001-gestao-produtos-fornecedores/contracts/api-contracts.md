# API Contracts: Gestão de Produtos e Fornecedores

**Feature**: [spec.md](file:///c:/Users/conta/Downloads/Ferreira/TesteSddComCepein/SDDcomCepein/specs/001-gestao-produtos-fornecedores/spec.md) | **Protocol**: HTTPS / RESTful JSON

---

## 1. Gestão de Fornecedores (`/api/fornecedores`)

### GET `/api/fornecedores`
Lista fornecedores com suporte a filtro por nome ou CNPJ.

- **Query Parameters**:
  - `query` (optional, string): Filtro por Razão Social ou CNPJ.
- **Response 200 OK**:
```json
[
  {
    "idFornecedor": 1,
    "razaoSocial": "Distribuidora Tech Ltda",
    "cnpj": "12.345.678/0001-90",
    "telefone": "(11) 98765-4321",
    "email": "contato@distribuidoratech.com",
    "dataCadastro": "2026-08-26T10:00:00Z"
  }
]
```

### POST `/api/fornecedores`
Cria um novo fornecedor.

- **Request Body**:
```json
{
  "razaoSocial": "Distribuidora Tech Ltda",
  "cnpj": "12.345.678/0001-90",
  "telefone": "(11) 98765-4321",
  "email": "contato@distribuidoratech.com"
}
```
- **Response 201 Created**: (Retorna o objeto criado com `idFornecedor`).
- **Response 400 Bad Request**: (Caso o CNPJ seja inválido/duplicado ou campos obrigatórios faltem).

### GET `/api/fornecedores/{id}/produtos` (Relatório RF05 / Visão A)
Retorna o fornecedor específico e a lista de produtos fornecidos com preços e prazos.

- **Response 200 OK**:
```json
{
  "idFornecedor": 1,
  "razaoSocial": "Distribuidora Tech Ltda",
  "cnpj": "12.345.678/0001-90",
  "produtos": [
    {
      "idProduto": 10,
      "skuCodigo": "MON-27-4K",
      "nome": "Monitor 27 Polegadas 4K",
      "categoria": "Periféricos",
      "precoCusto": 1250.00,
      "prazoEntregaDias": 3
    }
  ]
}
```

---

## 2. Gestão de Produtos (`/api/produtos`)

### GET `/api/produtos`
Lista produtos com filtro por nome ou SKU.

- **Query Parameters**:
  - `query` (optional, string): Filtro por Nome ou Código SKU.
- **Response 200 OK**:
```json
[
  {
    "idProduto": 10,
    "skuCodigo": "MON-27-4K",
    "nome": "Monitor 27 Polegadas 4K",
    "descricao": "Monitor Ultra HD IPS 60Hz",
    "categoria": "Periféricos",
    "dataCadastro": "2026-08-26T10:00:00Z"
  }
]
```

### POST `/api/produtos`
Cria um novo produto no catálogo.

- **Request Body**:
```json
{
  "skuCodigo": "MON-27-4K",
  "nome": "Monitor 27 Polegadas 4K",
  "descricao": "Monitor Ultra HD IPS 60Hz",
  "categoria": "Periféricos"
}
```
- **Response 201 Created**
- **Response 400 Bad Request**: (Caso o SKU seja duplicado).

### GET `/api/produtos/{id}/fornecedores` (Relatório RF06 / Visão B)
Retorna o produto e a lista comparativa de fornecedores que o comercializam.

- **Response 200 OK**:
```json
{
  "idProduto": 10,
  "skuCodigo": "MON-27-4K",
  "nome": "Monitor 27 Polegadas 4K",
  "fornecedores": [
    {
      "idFornecedor": 1,
      "razaoSocial": "Distribuidora Tech Ltda",
      "cnpj": "12.345.678/0001-90",
      "precoCusto": 1250.00,
      "prazoEntregaDias": 3
    },
    {
      "idFornecedor": 2,
      "razaoSocial": "Suprimentos Global S/A",
      "cnpj": "98.765.432/0001-10",
      "precoCusto": 1200.00,
      "prazoEntregaDias": 7
    }
  ]
}
```

---

## 3. Gestão de Vínculos N:N (`/api/produtos/{id_produto}/fornecedores/{id_fornecedor}`)

### POST `/api/produtos/{id_produto}/fornecedores/{id_fornecedor}`
Cria ou atualiza o vínculo entre um produto e um fornecedor, armazenando preço e prazo.

- **Request Body**:
```json
{
  "precoCusto": 1250.00,
  "prazoEntregaDias": 5
}
```
- **Response 200 OK / 201 Created**:
```json
{
  "idFornecedor": 1,
  "idProduto": 10,
  "precoCusto": 1250.00,
  "prazoEntregaDias": 5,
  "mensagem": "Vínculo salvo com sucesso"
}
```
- **Response 400 Bad Request**: Caso `precoCusto <= 0` ou `prazoEntregaDias < 0`.

### DELETE `/api/produtos/{id_produto}/fornecedores/{id_fornecedor}`
Remove a associação entre o produto e o fornecedor (não deleta as entidades principais).

- **Header Obrigatório**: `X-User-Role: ADMIN` (Simulação/Token de perfil Administrador para RBAC)
- **Response 204 No Content**
- **Response 403 Forbidden**: Caso o usuário não possua permissão de Administrador.
