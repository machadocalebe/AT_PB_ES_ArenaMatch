/* eslint-disable */
import { useState, useEffect } from 'react';
import api from '../services/api';
import './GestaoQuadras.css';

export default function GestaoQuadras() {
  const [quadras, setQuadras] = useState([]);
  const [nome, setNome] = useState('');
  const [precoHora, setPrecoHora] = useState('');
  
  // Guardam notas e dados de avaliação para cada quadra
  const [avaliacoesQuadra, setAvaliacoesQuadra] = useState({}); 

  const carregarQuadras = async () => {
    try {
      const res = await api.get('/quadras');
      setQuadras(res.data);
      res.data.forEach(q => carregarAvaliacoes(q.id));
    } catch (error) {
      console.error(error);
    }
  };

  const carregarAvaliacoes = async (quadraId) => {
    try {
      const res = await api.get(`/quadras/${quadraId}/avaliacoes`);
      setAvaliacoesQuadra(prev => ({ ...prev, [quadraId]: res.data }));
    } catch (error) {
      console.error(`Erro ao buscar notas da quadra ${quadraId}`, error);
    }
  };

  useEffect(() => {
    carregarQuadras();
  }, []);

  const handleCriarQuadra = async (e) => {
    e.preventDefault();
    try {
      await api.post('/quadras', { nome, precoHora: parseFloat(precoHora) });
      setNome(''); setPrecoHora('');
      carregarQuadras();
    } catch (error) {
      console.error(error);
      alert("Erro ao criar quadra!");
    }
  };

  const handleManutencao = async (id) => {
    try {
      await api.patch(`/quadras/${id}/manutencao`);
      carregarQuadras();
    } catch (error) { 
      console.error(error);
      alert("Erro ao alterar manutenção!"); 
    }
  };

  const handleExcluir = async (id) => {
    if (window.confirm("Tem certeza que deseja excluir esta quadra?")) {
      try {
        await api.delete(`/quadras/${id}`);
        carregarQuadras();
      } catch (error) {
        alert(error.response?.data?.message || "Erro ao excluir quadra.");
      }
    }
  };

  const handleEnviarAvaliacao = async (quadraId, e) => {
    e.preventDefault();
    const form = e.target;
    const autor = form.autor.value;
    const nota = parseInt(form.nota.value);
    const comentario = form.comentario.value;

    if (!autor || nota < 1 || nota > 5 || !comentario) {
      alert("Preencha todos os campos corretamente (Nota de 1 a 5).");
      return;
    }
    
    try {
      await api.post(`/quadras/${quadraId}/avaliacoes`, { autor, nota, comentario });
      form.reset();
      alert("Avaliação registrada com sucesso! Como processamos isso de forma assíncrona no RabbitMQ, em breve aparecerá.");
      // Dá um tempo curto para o RabbitMQ processar e recarrega
      setTimeout(() => carregarAvaliacoes(quadraId), 1000);
    } catch (error) {
      console.error(error);
      alert("Erro ao enviar avaliação.");
    }
  };

  // Helper para calcular a média
  const calcularMedia = (lista) => {
    if (!lista || lista.length === 0) return 'Sem notas';
    const media = lista.reduce((acc, curr) => acc + curr.nota, 0) / lista.length;
    return `${media.toFixed(1)} ⭐`;
  };

  return (
    <div className="gestao-container animate-fade-in">
      <header className="gestao-header">
        <h1>Gestão de Quadras</h1>
        <p>Cadastre, gerencie e avalie as quadras disponíveis no complexo.</p>
      </header>

      <div className="gestao-layout">
        <aside className="gestao-sidebar">
          <div className="glass-card new-reservation-card">
            <h2>Nova Quadra</h2>
            <form className="form-reserva" onSubmit={handleCriarQuadra}>
              <div className="input-group">
                <label>Nome da Quadra</label>
                <input type="text" placeholder="Ex: Quadra Ouro" value={nome} onChange={e => setNome(e.target.value)} required />
              </div>
              <div className="input-group">
                <label>Preço/Hora (R$)</label>
                <input type="number" step="0.01" placeholder="100.00" value={precoHora} onChange={e => setPrecoHora(e.target.value)} required />
              </div>
              <button type="submit" className="btn-apple-primary">Adicionar Quadra</button>
            </form>
          </div>
        </aside>

        <main className="quadras-grid">
          {quadras.map(q => (
            <div key={q.id} className="quadra-card">
              <div className="quadra-header">
                <h3>{q.nome}</h3>
                <p className="preco">R$ {q.precoHora.toFixed(2)} / hora</p>
                <span className={`status-badge ${q.emManutencao ? 'manutencao' : 'disponivel'}`}>
                  {q.emManutencao ? 'Interditada' : 'Disponível'}
                </span>
                <p style={{marginTop: '0.5rem', fontWeight: 600, color: '#0071e3'}}>
                  Média: {calcularMedia(avaliacoesQuadra[q.id])}
                </p>
              </div>

              <div className="avaliacoes-section">
                <h4>Comentários Recentes ({avaliacoesQuadra[q.id]?.length || 0})</h4>
                <div style={{maxHeight: '120px', overflowY: 'auto', marginBottom: '1rem'}}>
                  {avaliacoesQuadra[q.id]?.map((av, index) => (
                    <div key={index} className="avaliacao-item">
                      <strong>{av.nota} ⭐</strong> - {av.comentario}
                      <br/><small style={{color: '#86868b'}}>Por: {av.autor || 'Anônimo'}</small>
                    </div>
                  ))}
                  {!avaliacoesQuadra[q.id] || avaliacoesQuadra[q.id].length === 0 ? (
                    <p style={{fontSize: '0.85rem', color: '#86868b'}}>Nenhuma avaliação ainda.</p>
                  ) : null}
                </div>
                
                <form className="nova-avaliacao-form" onSubmit={(e) => handleEnviarAvaliacao(q.id, e)}>
                  <input name="autor" type="text" placeholder="Seu Nome" required />
                  <input name="nota" type="number" min="1" max="5" placeholder="Nota (1-5)" required />
                  <textarea name="comentario" placeholder="Deixe seu comentário" required rows="2"></textarea>
                  <button type="submit">Enviar Avaliação</button>
                </form>
              </div>

              <div style={{display: 'flex', gap: '0.5rem', marginTop: '1rem'}}>
                <button 
                  className={`btn-apple-primary`} 
                  style={{flex: 1, backgroundColor: q.emManutencao ? '#34c759' : '#ff9500'}} 
                  onClick={() => handleManutencao(q.id)}>
                  {q.emManutencao ? "Liberar" : "Bloquear"}
                </button>
                <button className="btn-apple-danger" style={{flex: 1}} onClick={() => handleExcluir(q.id)}>
                  Excluir
                </button>
              </div>
            </div>
          ))}
        </main>
      </div>
    </div>
  );
}
