import React, { useState } from 'react';

export function Login({ onLoginSuccess }) {
  const [username, setUsername] = useState('admin');
  const [password, setPassword] = useState('admin123');
  const [error, setError] = useState(null);

  const handleSubmit = (e) => {
    e.preventDefault();
    setError(null);
    if (username === 'admin' && password === 'admin123') {
      onLoginSuccess({ username: 'admin', role: 'ADMIN' });
    } else {
      setError('Credenciais inválidas. Use usuario "admin" e senha "admin123".');
    }
  };

  return (
    <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '75vh' }}>
      <div className="card-section" style={{ width: '100%', maxWidth: '440px', padding: '36px' }}>
        <div style={{ textAlign: 'center', marginBottom: '28px' }}>
          <div className="brand-icon" style={{ margin: '0 auto 16px auto', width: '56px', height: '56px', fontSize: '28px' }}>
            S
          </div>
          <h2 style={{ fontSize: '22px', fontWeight: '700', marginBottom: '6px' }}>Acesso ao Sistema</h2>
          <p style={{ color: '#94a3b8', fontSize: '13px' }}>Gestão de Produtos e Fornecedores SDD</p>
        </div>

        <div style={{ background: 'rgba(99, 102, 241, 0.12)', border: '1px solid rgba(99, 102, 241, 0.3)', borderRadius: '10px', padding: '12px 16px', marginBottom: '24px', fontSize: '13px' }}>
          <strong style={{ color: '#a5b4fc' }}>🔑 Credenciais de Teste Rápido:</strong>
          <div style={{ color: '#cbd5e1', marginTop: '4px' }}>
            Usuário: <code style={{ color: '#c084fc', background: 'rgba(0,0,0,0.3)', padding: '2px 6px', borderRadius: '4px' }}>admin</code><br />
            Senha: <code style={{ color: '#c084fc', background: 'rgba(0,0,0,0.3)', padding: '2px 6px', borderRadius: '4px' }}>admin123</code>
          </div>
        </div>

        {error && <div className="alert-message alert-error">{error}</div>}

        <form onSubmit={handleSubmit}>
          <div className="form-group" style={{ marginBottom: '18px' }}>
            <label>Usuário</label>
            <input
              type="text"
              className="input-field"
              required
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              placeholder="Digite seu usuário"
            />
          </div>

          <div className="form-group" style={{ marginBottom: '24px' }}>
            <label>Senha</label>
            <input
              type="password"
              className="input-field"
              required
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="Digite sua senha"
            />
          </div>

          <button type="submit" className="btn btn-primary" style={{ width: '100%', padding: '12px' }}>
            Entrar no Sistema
          </button>
        </form>
      </div>
    </div>
  );
}
