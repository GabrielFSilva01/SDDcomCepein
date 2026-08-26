import React, { useState, useEffect } from 'react';
import { api } from '../services/api';

export function RelatorioExpansivel() {
  const [visao, setVisao] = useState('fornecedores'); // 'fornecedores' ou 'produtos'
  const [fornecedores, setFornecedores] = useState([]);
  const [produtos, setProdutos] = useState([]);
  const [expandedId, setExpandedId] = useState(null);
  const [expandedData, setExpandedData] = useState(null);
  const [loadingDetails, setLoadingDetails] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    loadMainList();
  }, [visao]);

  const loadMainList = async () => {
    setExpandedId(null);
    setExpandedData(null);
    try {
      if (visao === 'fornecedores') {
        const data = await api.getFornecedores();
        setFornecedores(data);
      } else {
        const data = await api.getProdutos();
        setProdutos(data);
      }
    } catch (err) {
      setError(err.message);
    }
  };

  const handleToggleExpand = async (id) => {
    if (expandedId === id) {
      setExpandedId(null);
      setExpandedData(null);
      return;
    }

    setExpandedId(id);
    setLoadingDetails(true);
    setError(null);

    try {
      if (visao === 'fornecedores') {
        const report = await api.getFornecedorProdutos(id);
        setExpandedData(report);
      } else {
        const report = await api.getProdutoFornecedores(id);
        setExpandedData(report);
      }
    } catch (err) {
      setError(err.message);
    } finally {
      setLoadingDetails(false);
    }
  };

  const handleRemoverVinculo = async (idProduto, idFornecedor) => {
    if (!window.confirm('Desassociar este vínculo?')) return;
    try {
      await api.removerVinculo(idProduto, idFornecedor, 'ADMIN');
      handleToggleExpand(expandedId); // reload expanded
    } catch (err) {
      setError(err.message);
    }
  };

  return (
    <div className="card-section">
      <div className="section-header">
        <h3 className="section-title">Dashboards de Relatório Expansíveis (N:N)</h3>
        <div style={{ display: 'flex', gap: '8px' }}>
          <button
            className={`btn ${visao === 'fornecedores' ? 'btn-primary' : ''}`}
            style={{ background: visao === 'fornecedores' ? '' : 'rgba(255,255,255,0.05)', color: '#fff' }}
            onClick={() => setVisao('fornecedores')}
          >
            Visão A: Fornecedores → Produtos
          </button>
          <button
            className={`btn ${visao === 'produtos' ? 'btn-primary' : ''}`}
            style={{ background: visao === 'produtos' ? '' : 'rgba(255,255,255,0.05)', color: '#fff' }}
            onClick={() => setVisao('produtos')}
          >
            Visão B: Produtos → Fornecedores
          </button>
        </div>
      </div>

      {error && <div className="alert-message alert-error">{error}</div>}

      <div className="table-wrapper">
        <table>
          <thead>
            {visao === 'fornecedores' ? (
              <tr>
                <th>Expandir</th>
                <th>Razão Social</th>
                <th>CNPJ</th>
                <th>Contato</th>
              </tr>
            ) : (
              <tr>
                <th>Expandir</th>
                <th>SKU</th>
                <th>Nome do Produto</th>
                <th>Categoria</th>
              </tr>
            )}
          </thead>
          <tbody>
            {visao === 'fornecedores' ? (
              fornecedores.map((f) => (
                <React.Fragment key={f.idFornecedor}>
                  <tr className="expandable-row" onClick={() => handleToggleExpand(f.idFornecedor)}>
                    <td style={{ fontWeight: 'bold', color: '#a855f7' }}>
                      {expandedId === f.idFornecedor ? '▼ Ocultar' : '▶ Expandir Produtos'}
                    </td>
                    <td style={{ fontWeight: '600' }}>{f.razaoSocial}</td>
                    <td><span className="badge badge-purple">{f.cnpj}</span></td>
                    <td>{f.email}</td>
                  </tr>
                  {expandedId === f.idFornecedor && (
                    <tr>
                      <td colSpan="4" className="expanded-detail">
                        {loadingDetails ? (
                          <div style={{ color: '#94a3b8' }}>Carregando produtos vinculados...</div>
                        ) : expandedData && expandedData.produtos && expandedData.produtos.length > 0 ? (
                          <div>
                            <h4 style={{ fontSize: '14px', marginBottom: '10px', color: '#c084fc' }}>
                              Produtos Fornecidos por {expandedData.razaoSocial}:
                            </h4>
                            <table>
                              <thead>
                                <tr>
                                  <th>SKU</th>
                                  <th>Produto</th>
                                  <th>Categoria</th>
                                  <th>Preço de Custo</th>
                                  <th>Prazo Entrega</th>
                                  <th>Ação</th>
                                </tr>
                              </thead>
                              <tbody>
                                {expandedData.produtos.map((p) => (
                                  <tr key={p.idProduto}>
                                    <td><span className="badge badge-purple">{p.skuCodigo}</span></td>
                                    <td>{p.nome}</td>
                                    <td>{p.categoria || 'Geral'}</td>
                                    <td style={{ color: '#10b981', fontWeight: 'bold' }}>
                                      R$ {p.precoCusto.toFixed(2)}
                                    </td>
                                    <td>{p.prazoEntregaDias} dias</td>
                                    <td>
                                      <button
                                        className="btn btn-danger"
                                        style={{ padding: '4px 10px', fontSize: '12px' }}
                                        onClick={(e) => { e.stopPropagation(); handleRemoverVinculo(p.idProduto, f.idFornecedor); }}
                                      >
                                        Desvincular
                                      </button>
                                    </td>
                                  </tr>
                                ))}
                              </tbody>
                            </table>
                          </div>
                        ) : (
                          <div style={{ color: '#94a3b8' }}>Nenhum produto vinculado a este fornecedor.</div>
                        )}
                      </td>
                    </tr>
                  )}
                </React.Fragment>
              ))
            ) : (
              produtos.map((p) => (
                <React.Fragment key={p.idProduto}>
                  <tr className="expandable-row" onClick={() => handleToggleExpand(p.idProduto)}>
                    <td style={{ fontWeight: 'bold', color: '#6366f1' }}>
                      {expandedId === p.idProduto ? '▼ Ocultar' : '▶ Expandir Fornecedores'}
                    </td>
                    <td><span className="badge badge-purple">{p.skuCodigo}</span></td>
                    <td style={{ fontWeight: '600' }}>{p.nome}</td>
                    <td><span className="badge badge-green">{p.categoria || 'Geral'}</span></td>
                  </tr>
                  {expandedId === p.idProduto && (
                    <tr>
                      <td colSpan="4" className="expanded-detail">
                        {loadingDetails ? (
                          <div style={{ color: '#94a3b8' }}>Carregando fornecedores vinculados...</div>
                        ) : expandedData && expandedData.fornecedores && expandedData.fornecedores.length > 0 ? (
                          <div>
                            <h4 style={{ fontSize: '14px', marginBottom: '10px', color: '#818cf8' }}>
                              Comparativo de Fornecedores para {expandedData.nome}:
                            </h4>
                            <table>
                              <thead>
                                <tr>
                                  <th>Fornecedor</th>
                                  <th>CNPJ</th>
                                  <th>Preço de Custo</th>
                                  <th>Prazo Entrega</th>
                                  <th>Ação</th>
                                </tr>
                              </thead>
                              <tbody>
                                {expandedData.fornecedores.map((forn) => (
                                  <tr key={forn.idFornecedor}>
                                    <td style={{ fontWeight: '600' }}>{forn.razaoSocial}</td>
                                    <td><span className="badge badge-purple">{forn.cnpj}</span></td>
                                    <td style={{ color: '#10b981', fontWeight: 'bold' }}>
                                      R$ {forn.precoCusto.toFixed(2)}
                                    </td>
                                    <td>{forn.prazoEntregaDias} dias</td>
                                    <td>
                                      <button
                                        className="btn btn-danger"
                                        style={{ padding: '4px 10px', fontSize: '12px' }}
                                        onClick={(e) => { e.stopPropagation(); handleRemoverVinculo(p.idProduto, forn.idFornecedor); }}
                                      >
                                        Desvincular
                                      </button>
                                    </td>
                                  </tr>
                                ))}
                              </tbody>
                            </table>
                          </div>
                        ) : (
                          <div style={{ color: '#94a3b8' }}>Nenhum fornecedor vinculado a este produto.</div>
                        )}
                      </td>
                    </tr>
                  )}
                </React.Fragment>
              ))
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}
