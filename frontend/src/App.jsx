import React, { useState } from 'react';
import { Login } from './components/Login';
import { CadastroBase } from './components/CadastroBase';
import { WizardAssoc } from './components/WizardAssoc';
import { RelatorioExpansivel } from './components/RelatorioExpansivel';

export function App() {
  const [user, setUser] = useState(null);
  const [activeView, setActiveView] = useState('cadastro');

  if (!user) {
    return <Login onLoginSuccess={(userObj) => setUser(userObj)} />;
  }

  return (
    <div className="app-container">
      <header>
        <div className="brand">
          <div className="brand-icon">S</div>
          <div>
            <h1>Gestão de Produtos e Fornecedores</h1>
            <p style={{ fontSize: '12px', color: '#94a3b8' }}>Arquitetura SDD N:N com Spring Boot 3 & React SPA (SQLite)</p>
          </div>
        </div>
        <div style={{ display: 'flex', alignItems: 'center', gap: '16px' }}>
          <div className="role-badge">
            <span>🛡️ Usuário: {user.username} (ADMIN)</span>
          </div>
          <button
            className="btn btn-danger"
            style={{ padding: '6px 14px', fontSize: '12px' }}
            onClick={() => setUser(null)}
          >
            Sair
          </button>
        </div>
      </header>

      <nav className="nav-tabs">
        <button
          className={`nav-tab ${activeView === 'cadastro' ? 'active' : ''}`}
          onClick={() => setActiveView('cadastro')}
        >
          📦 Cadastros Base
        </button>
        <button
          className={`nav-tab ${activeView === 'associacao' ? 'active' : ''}`}
          onClick={() => setActiveView('associacao')}
        >
          🔗 Assistente de Associação N:N
        </button>
        <button
          className={`nav-tab ${activeView === 'relatorios' ? 'active' : ''}`}
          onClick={() => setActiveView('relatorios')}
        >
          📊 Relatórios Expansíveis
        </button>
      </nav>

      <main>
        {activeView === 'cadastro' && <CadastroBase />}
        {activeView === 'associacao' && <WizardAssoc />}
        {activeView === 'relatorios' && <RelatorioExpansivel />}
      </main>
    </div>
  );
}
