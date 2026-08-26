import React, { useState, useEffect } from 'react';
import { api } from '../services/api';

export function WizardAssoc() {
  const [produtos, setProdutos] = useState([]);
  const [fornecedores, setFornecedores] = useState([]);
  const [selectedProduto, setSelectedProduto] = useState('');
  const [selectedFornecedor, setSelectedFornecedor] = useState('');
  const [precoCusto, setPrecoCusto] = useState('');
  const [prazoEntregaDias, setPrazoEntregaDias] = useState('');
  
  const [message, setMessage] = useState(null);
  const [error, setError] = useState(null);

  useEffect(() => {
    loadOptions();
  }, []);

  const loadOptions = async () => {
    try {
      const [prods, forns] = await Promise.all([
        api.getProdutos(),
        api.getFornecedores()
      ]);
      setProdutos(prods);
      setFornecedores(forns);
    } catch (err) {
      setError('Erro ao carregar opções para associação: ' + err.message);
    }
  };

  const handleVincular = async (e) => {
    e.preventDefault();
    setMessage(null);
    setError(null);

    if (!selectedProduto || !selectedFornecedor) {
      setError('Selecione um Produto e um Fornecedor');
      return;
    }

    try {
      const result = await api.salvarVinculo(selectedProduto, selectedFornecedor, {
        precoCusto: parseFloat(precoCusto),
        prazoEntregaDias: parseInt(prazoEntregaDias, 10)
      });
      setMessage(`Vínculo N:N estabelecido com sucesso! (Preço: R$ ${parseFloat(precoCusto).toFixed(2)}, Prazo: ${prazoEntregaDias} dias)`);
      setPrecoCusto('');
      setPrazoEntregaDias('');
    } catch (err) {
      setError(err.message);
    }
  };

  return (
    <div className="card-section">
      <div className="section-header">
        <h3 className="section-title">Assistente de Associação N:N (Produto x Fornecedor)</h3>
      </div>
      <p style={{ color: '#94a3b8', fontSize: '14px', marginBottom: '20px' }}>
        Selecione um Produto e um Fornecedor para registrar ou atualizar a relação de fornecimento com preço de custo e prazo de entrega.
      </p>

      {message && <div className="alert-message alert-success">{message}</div>}
      {error && <div className="alert-message alert-error">{error}</div>}

      <form onSubmit={handleVincular}>
        <div className="form-grid">
          <div className="form-group">
            <label>Selecione o Produto *</label>
            <select
              className="input-field"
              required
              value={selectedProduto}
              onChange={(e) => setSelectedProduto(e.target.value)}
            >
              <option value="">-- Escolha um Produto --</option>
              {produtos.map((p) => (
                <option key={p.idProduto} value={p.idProduto}>
                  [{p.skuCodigo}] {p.nome}
                </option>
              ))}
            </select>
          </div>

          <div className="form-group">
            <label>Selecione o Fornecedor *</label>
            <select
              className="input-field"
              required
              value={selectedFornecedor}
              onChange={(e) => setSelectedFornecedor(e.target.value)}
            >
              <option value="">-- Escolha um Fornecedor --</option>
              {fornecedores.map((f) => (
                <option key={f.idFornecedor} value={f.idFornecedor}>
                  {f.razaoSocial} ({f.cnpj})
                </option>
              ))}
            </select>
          </div>

          <div className="form-group">
            <label>Preço de Custo (R$) *</label>
            <input
              type="number"
              step="0.01"
              min="0.01"
              className="input-field"
              required
              placeholder="Ex: 150.00"
              value={precoCusto}
              onChange={(e) => setPrecoCusto(e.target.value)}
            />
          </div>

          <div className="form-group">
            <label>Prazo de Entrega (dias) *</label>
            <input
              type="number"
              min="0"
              className="input-field"
              required
              placeholder="Ex: 5"
              value={prazoEntregaDias}
              onChange={(e) => setPrazoEntregaDias(e.target.value)}
            />
          </div>
        </div>

        <button type="submit" className="btn btn-primary">
          Vincular Produto ao Fornecedor
        </button>
      </form>
    </div>
  );
}
