import React, { useState, useEffect } from 'react';
import { api } from '../services/api';

export function CadastroBase() {
  const [activeTab, setActiveTab] = useState('fornecedores');
  const [fornecedores, setFornecedores] = useState([]);
  const [produtos, setProdutos] = useState([]);
  const [query, setQuery] = useState('');
  const [message, setMessage] = useState(null);
  const [error, setError] = useState(null);

  // Form states Fornecedor
  const [razaoSocial, setRazaoSocial] = useState('');
  const [cnpj, setCnpj] = useState('');
  const [telefone, setTelefone] = useState('');
  const [email, setEmail] = useState('');

  // Form states Produto
  const [skuCodigo, setSkuCodigo] = useState('');
  const [nome, setNome] = useState('');
  const [descricao, setDescricao] = useState('');
  const [categoria, setCategoria] = useState('');

  const loadData = async () => {
    try {
      if (activeTab === 'fornecedores') {
        const data = await api.getFornecedores(query);
        setFornecedores(data);
      } else {
        const data = await api.getProdutos(query);
        setProdutos(data);
      }
    } catch (err) {
      setError(err.message);
    }
  };

  useEffect(() => {
    loadData();
  }, [activeTab, query]);

  const handleSalvarFornecedor = async (e) => {
    e.preventDefault();
    setMessage(null);
    setError(null);
    try {
      await api.createFornecedor({ razaoSocial, cnpj, telefone, email });
      setMessage('Fornecedor cadastrado com sucesso!');
      setRazaoSocial('');
      setCnpj('');
      setTelefone('');
      setEmail('');
      loadData();
    } catch (err) {
      setError(err.message);
    }
  };

  const handleSalvarProduto = async (e) => {
    e.preventDefault();
    setMessage(null);
    setError(null);
    try {
      await api.createProduto({ skuCodigo, nome, descricao, categoria });
      setMessage('Produto cadastrado com sucesso!');
      setSkuCodigo('');
      setNome('');
      setDescricao('');
      setCategoria('');
      loadData();
    } catch (err) {
      setError(err.message);
    }
  };

  const handleExcluirFornecedor = async (id) => {
    if (!window.confirm('Confirma a exclusão deste fornecedor?')) return;
    setMessage(null);
    setError(null);
    try {
      await api.deleteFornecedor(id, 'ADMIN');
      setMessage('Fornecedor e seus vínculos excluídos com sucesso!');
      loadData();
    } catch (err) {
      setError(err.message);
    }
  };

  const handleExcluirProduto = async (id) => {
    if (!window.confirm('Confirma a exclusão deste produto?')) return;
    setMessage(null);
    setError(null);
    try {
      await api.deleteProduto(id, 'ADMIN');
      setMessage('Produto e seus vínculos excluídos com sucesso!');
      loadData();
    } catch (err) {
      setError(err.message);
    }
  };

  return (
    <div>
      <div style={{ display: 'flex', gap: '12px', marginBottom: '20px' }}>
        <button
          className={`nav-tab ${activeTab === 'fornecedores' ? 'active' : ''}`}
          onClick={() => { setActiveTab('fornecedores'); setQuery(''); setMessage(null); setError(null); }}
        >
          Fornecedores
        </button>
        <button
          className={`nav-tab ${activeTab === 'produtos' ? 'active' : ''}`}
          onClick={() => { setActiveTab('produtos'); setQuery(''); setMessage(null); setError(null); }}
        >
          Produtos
        </button>
      </div>

      {message && <div className="alert-message alert-success">{message}</div>}
      {error && <div className="alert-message alert-error">{error}</div>}

      {activeTab === 'fornecedores' ? (
        <>
          <div className="card-section">
            <h3 className="section-title" style={{ marginBottom: '16px' }}>Novo Fornecedor</h3>
            <form onSubmit={handleSalvarFornecedor}>
              <div className="form-grid">
                <div className="form-group">
                  <label>Razão Social *</label>
                  <input
                    type="text"
                    className="input-field"
                    required
                    value={razaoSocial}
                    onChange={(e) => setRazaoSocial(e.target.value)}
                    placeholder="Ex: Distribuidora Tech Ltda"
                  />
                </div>
                <div className="form-group">
                  <label>CNPJ *</label>
                  <input
                    type="text"
                    className="input-field"
                    required
                    value={cnpj}
                    onChange={(e) => setCnpj(e.target.value)}
                    placeholder="12.345.678/0001-90"
                  />
                </div>
                <div className="form-group">
                  <label>Telefone</label>
                  <input
                    type="text"
                    className="input-field"
                    value={telefone}
                    onChange={(e) => setTelefone(e.target.value)}
                    placeholder="(11) 98765-4321"
                  />
                </div>
                <div className="form-group">
                  <label>E-mail *</label>
                  <input
                    type="email"
                    className="input-field"
                    required
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    placeholder="contato@empresa.com"
                  />
                </div>
              </div>
              <button type="submit" className="btn btn-primary">Cadastrar Fornecedor</button>
            </form>
          </div>

          <div className="card-section">
            <div className="section-header">
              <h3 className="section-title">Fornecedores Cadastrados ({fornecedores.length})</h3>
              <input
                type="text"
                className="input-field"
                style={{ width: '280px' }}
                placeholder="Buscar por nome ou CNPJ..."
                value={query}
                onChange={(e) => setQuery(e.target.value)}
              />
            </div>
            <div className="table-wrapper">
              <table>
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>Razão Social</th>
                    <th>CNPJ</th>
                    <th>Telefone</th>
                    <th>E-mail</th>
                    <th>Ações</th>
                  </tr>
                </thead>
                <tbody>
                  {fornecedores.length === 0 ? (
                    <tr><td colSpan="6" style={{ textAlign: 'center', color: '#94a3b8' }}>Nenhum fornecedor encontrado.</td></tr>
                  ) : (
                    fornecedores.map((f) => (
                      <tr key={f.idFornecedor}>
                        <td>#{f.idFornecedor}</td>
                        <td style={{ fontWeight: '600' }}>{f.razaoSocial}</td>
                        <td><span className="badge badge-purple">{f.cnpj}</span></td>
                        <td>{f.telefone || '-'}</td>
                        <td>{f.email}</td>
                        <td>
                          <button className="btn btn-danger" onClick={() => handleExcluirFornecedor(f.idFornecedor)}>Excluir</button>
                        </td>
                      </tr>
                    ))
                  )}
                </tbody>
              </table>
            </div>
          </div>
        </>
      ) : (
        <>
          <div className="card-section">
            <h3 className="section-title" style={{ marginBottom: '16px' }}>Novo Produto</h3>
            <form onSubmit={handleSalvarProduto}>
              <div className="form-grid">
                <div className="form-group">
                  <label>Código SKU *</label>
                  <input
                    type="text"
                    className="input-field"
                    required
                    value={skuCodigo}
                    onChange={(e) => setSkuCodigo(e.target.value)}
                    placeholder="Ex: CAD-001"
                  />
                </div>
                <div className="form-group">
                  <label>Nome do Produto *</label>
                  <input
                    type="text"
                    className="input-field"
                    required
                    value={nome}
                    onChange={(e) => setNome(e.target.value)}
                    placeholder="Ex: Cadeira de Escritório"
                  />
                </div>
                <div className="form-group">
                  <label>Categoria</label>
                  <input
                    type="text"
                    className="input-field"
                    value={categoria}
                    onChange={(e) => setCategoria(e.target.value)}
                    placeholder="Ex: Móveis"
                  />
                </div>
                <div className="form-group">
                  <label>Descrição</label>
                  <input
                    type="text"
                    className="input-field"
                    value={descricao}
                    onChange={(e) => setDescricao(e.target.value)}
                    placeholder="Detalhes do produto"
                  />
                </div>
              </div>
              <button type="submit" className="btn btn-primary">Cadastrar Produto</button>
            </form>
          </div>

          <div className="card-section">
            <div className="section-header">
              <h3 className="section-title">Produtos Cadastrados ({produtos.length})</h3>
              <input
                type="text"
                className="input-field"
                style={{ width: '280px' }}
                placeholder="Buscar por nome ou SKU..."
                value={query}
                onChange={(e) => setQuery(e.target.value)}
              />
            </div>
            <div className="table-wrapper">
              <table>
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>SKU</th>
                    <th>Nome</th>
                    <th>Categoria</th>
                    <th>Descrição</th>
                    <th>Ações</th>
                  </tr>
                </thead>
                <tbody>
                  {produtos.length === 0 ? (
                    <tr><td colSpan="6" style={{ textAlign: 'center', color: '#94a3b8' }}>Nenhum produto encontrado.</td></tr>
                  ) : (
                    produtos.map((p) => (
                      <tr key={p.idProduto}>
                        <td>#{p.idProduto}</td>
                        <td><span className="badge badge-purple">{p.skuCodigo}</span></td>
                        <td style={{ fontWeight: '600' }}>{p.nome}</td>
                        <td><span className="badge badge-green">{p.categoria || 'Geral'}</span></td>
                        <td>{p.descricao || '-'}</td>
                        <td>
                          <button className="btn btn-danger" onClick={() => handleExcluirProduto(p.idProduto)}>Excluir</button>
                        </td>
                      </tr>
                    ))
                  )}
                </tbody>
              </table>
            </div>
          </div>
        </>
      )}
    </div>
  );
}
