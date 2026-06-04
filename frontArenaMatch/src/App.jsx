import { useState } from 'react';
import GestaoQuadras from './components/GestaoQuadras';
import Agenda from './components/Agenda';
import './App.css';

function App() {
  const [abaAtiva, setAbaAtiva] = useState('agenda');

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
             Reservas
          </button>
          <button 
            className={`menu-btn ${abaAtiva === 'quadras' ? 'active' : ''}`}
            onClick={() => setAbaAtiva('quadras')}
          >
             Gestão de Quadras
          </button>
        </nav>

        <div className="user-profile">
          <div className="avatar">AD</div>
          <div className="user-info">
            <strong>Admin</strong>
            <span>Plano Premium</span>
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