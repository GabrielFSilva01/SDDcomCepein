const hostname = typeof window !== 'undefined' && window.location.hostname ? window.location.hostname : 'localhost';
const BASE_URL = `http://${hostname}:8080/api`;

export const api = {
  // Fornecedores
  async getFornecedores(query = '') {
    const url = query ? `${BASE_URL}/fornecedores?query=${encodeURIComponent(query)}` : `${BASE_URL}/fornecedores`;
    const res = await fetch(url);
    if (!res.ok) throw new Error('Erro ao buscar fornecedores. Verifique se o Backend (Spring Boot na porta 8080) está rodando.');
    return res.json();
  },

  async createFornecedor(data) {
    const res = await fetch(`${BASE_URL}/fornecedores`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data)
    });
    const json = await res.json();
    if (!res.ok) throw new Error(json.message || json.error || 'Erro ao cadastrar fornecedor');
    return json;
  },

  async updateFornecedor(id, data) {
    const res = await fetch(`${BASE_URL}/fornecedores/${id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data)
    });
    const json = await res.json();
    if (!res.ok) throw new Error(json.message || json.error || 'Erro ao atualizar fornecedor');
    return json;
  },

  async deleteFornecedor(id, role = 'ADMIN') {
    const res = await fetch(`${BASE_URL}/fornecedores/${id}`, {
      method: 'DELETE',
      headers: { 'X-User-Role': role }
    });
    if (!res.ok) {
      const json = await res.json().catch(() => ({}));
      throw new Error(json.message || json.error || 'Erro ao excluir fornecedor');
    }
  },

  async getFornecedorProdutos(id) {
    const res = await fetch(`${BASE_URL}/fornecedores/${id}/produtos`);
    if (!res.ok) throw new Error('Erro ao carregar relatório do fornecedor');
    return res.json();
  },

  // Produtos
  async getProdutos(query = '') {
    const url = query ? `${BASE_URL}/produtos?query=${encodeURIComponent(query)}` : `${BASE_URL}/produtos`;
    const res = await fetch(url);
    if (!res.ok) throw new Error('Erro ao buscar produtos. Verifique se o Backend (Spring Boot na porta 8080) está rodando.');
    return res.json();
  },

  async createProduto(data) {
    const res = await fetch(`${BASE_URL}/produtos`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data)
    });
    const json = await res.json();
    if (!res.ok) throw new Error(json.message || json.error || 'Erro ao cadastrar produto');
    return json;
  },

  async updateProduto(id, data) {
    const res = await fetch(`${BASE_URL}/produtos/${id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data)
    });
    const json = await res.json();
    if (!res.ok) throw new Error(json.message || json.error || 'Erro ao atualizar produto');
    return json;
  },

  async deleteProduto(id, role = 'ADMIN') {
    const res = await fetch(`${BASE_URL}/produtos/${id}`, {
      method: 'DELETE',
      headers: { 'X-User-Role': role }
    });
    if (!res.ok) {
      const json = await res.json().catch(() => ({}));
      throw new Error(json.message || json.error || 'Erro ao excluir produto');
    }
  },

  async getProdutoFornecedores(id) {
    const res = await fetch(`${BASE_URL}/produtos/${id}/fornecedores`);
    if (!res.ok) throw new Error('Erro ao carregar relatório do produto');
    return res.json();
  },

  // Vínculos N:N
  async salvarVinculo(idProduto, idFornecedor, data) {
    const res = await fetch(`${BASE_URL}/produtos/${idProduto}/fornecedores/${idFornecedor}`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data)
    });
    const json = await res.json();
    if (!res.ok) throw new Error(json.message || json.error || 'Erro ao associar produto e fornecedor');
    return json;
  },

  async removerVinculo(idProduto, idFornecedor, role = 'ADMIN') {
    const res = await fetch(`${BASE_URL}/produtos/${idProduto}/fornecedores/${idFornecedor}`, {
      method: 'DELETE',
      headers: { 'X-User-Role': role }
    });
    if (!res.ok) {
      const json = await res.json().catch(() => ({}));
      throw new Error(json.message || json.error || 'Erro ao desassociar vínculo');
    }
  }
};
