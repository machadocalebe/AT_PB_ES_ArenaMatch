import { useState, useEffect } from 'react';
import GestaoQuadras from './components/GestaoQuadras';
import Agenda from './components/Agenda';
import Login from './components/Login';
import './App.css';

function App() {
  const [abaAtiva, setAbaAtiva] = useState('agenda');
  const [token, setToken] = useState(localStorage.getItem('token'));
  const username = localStorage.getItem('username');

  const handleLogout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('username');
    setToken(null);
  };

  if (!token) {
    return <Login setToken={setToken} />;
  }

  return (
    <div className="app-container">
      {/* Barra Lateral (Sidebar) */}
      <aside className="sidebar">
        <div className="logo-area">
          <span className="logo-icon">🎾</span>
          <h2>ArenaMatch</h2>
        </div>

        <nav className="menu-nav">
          <button 
            className={`menu-btn ${abaAtiva === 'agenda' ? 'active' : ''}`}
            onClick={() => setAbaAtiva('agenda')}
          >
             🗓️ Reservas
          </button>
          <button 
            className={`menu-btn ${abaAtiva === 'quadras' ? 'active' : ''}`}
            onClick={() => setAbaAtiva('quadras')}
          >
             🏟️ Gestão de Quadras
          </button>
        </nav>

        <div className="user-profile">
          <div className="avatar">{username ? username.substring(0, 2).toUpperCase() : 'AD'}</div>
          <div className="user-info">
            <strong>{username || 'Usuário'}</strong>
            <button className="btn-link" style={{textAlign: 'left', padding: 0, marginTop: '4px'}} onClick={handleLogout}>Sair</button>
          </div>
        </div>
      </aside>

      {/* Área Principal de Conteúdo */}
      <main className="main-content">
        {abaAtiva === 'agenda' ? <Agenda /> : <GestaoQuadras />}
      </main>
    </div>
  );
}

export default App;