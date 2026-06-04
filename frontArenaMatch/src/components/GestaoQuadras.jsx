import { useState, useEffect } from 'react';
import api from '../services/api';
import './GestaoQuadras.css';

export default function GestaoQuadras() {
  const [quadras, setQuadras] = useState([]);
  const [nome, setNome] = useState('');
  const [precoHora, setPrecoHora] = useState('');

  const carregarQuadras = () => {
    api.get('/quadras').then(res => setQuadras(res.data)).catch(console.error);
  };

  useEffect(() => carregarQuadras(), []);

  const handleCriarQuadra = async (e) => {
    e.preventDefault();
    try {
      await api.post('/quadras', { nome, precoHora: parseFloat(precoHora) });
      setNome(''); setPrecoHora('');
      carregarQuadras();
    } catch (error) {
      alert("Erro ao criar quadra!");
    }
  };

  const handleManutencao = async (id) => {
    try {
      await api.patch(`/quadras/${id}/manutencao`);
      carregarQuadras();
    } catch (error) { alert("Erro ao alterar manutenção!"); }
  };

  const handleExcluir = async (id) => {
    if (window.confirm("Tem certeza que deseja excluir esta quadra?")) {
      try {
        await api.delete(`/quadras/${id}`);
        carregarQuadras();
      } catch (error) {
        alert(error.response?.data?.message || "Erro ao excluir quadra. Ela possui reservas atreladas?");
      }
    }
  };

  return (
    <div className="gestao-container">
      <form className="form-quadra" onSubmit={handleCriarQuadra}>
        <div className="input-group">
          <label>Nome da Quadra</label>
          <input type="text" value={nome} onChange={e => setNome(e.target.value)} required />
        </div>
        <div className="input-group">
          <label>Preço/Hora</label>
          <input type="number" value={precoHora} onChange={e => setPrecoHora(e.target.value)} required step="0.01" />
        </div>
        <button type="submit" className="btn-salvar">+ Adicionar Quadra</button>
      </form>

      <div className="quadras-grid">
        {quadras.map(q => (
          <div key={q.id} className={`quadra-card ${q.emManutencao ? 'quadra-manutencao' : ''}`}>
            <div className="quadra-header">
              <h4>{q.nome}</h4>
              {q.emManutencao ? <span>⚠️ Manutenção</span> : <span>✅ Ativa</span>}
            </div>
            <p className="preco">R$ {q.precoHora.toFixed(2)} / hora</p>
            
            <div className="acoes-card">
              <button className="btn-acao btn-toggle" onClick={() => handleManutencao(q.id)}>
                {q.emManutencao ? "Liberar" : "Bloquear"}
              </button>
              <button className="btn-acao btn-excluir" onClick={() => handleExcluir(q.id)}>
                Excluir
              </button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}