import { useState, useEffect } from 'react';
import api from '../services/api';
import './GestaoQuadras.css';

export default function GestaoQuadras() {
  const [quadras, setQuadras] = useState([]);
  const [nome, setNome] = useState('');
  const [precoHora, setPrecoHora] = useState('');
  const [notasTemp, setNotasTemp] = useState({}); // Guarda a nota digitada temporariamente
  const [avaliacoesQuadra, setAvaliacoesQuadra] = useState({}); // Guarda a média das quadras

  const carregarQuadras = async () => {
    try {
      const res = await api.get('/quadras');
      setQuadras(res.data);
      // Para cada quadra, busca as avaliações no microsserviço via backend principal
      res.data.forEach(q => carregarAvaliacoes(q.id));
    } catch (error) {
      console.error(error);
    }
  };

  const carregarAvaliacoes = async (quadraId) => {
    try {
      const res = await api.get(`/quadras/${quadraId}/avaliacoes`);
      const avaliacoes = res.data;
      
      if (avaliacoes.length > 0) {
        const media = avaliacoes.reduce((acc, curr) => acc + curr.nota, 0) / avaliacoes.length;
        setAvaliacoesQuadra(prev => ({ ...prev, [quadraId]: media.toFixed(1) }));
      } else {
        setAvaliacoesQuadra(prev => ({ ...prev, [quadraId]: 'Sem notas' }));
      }
    } catch (error) {
      console.error(`Erro ao buscar notas da quadra ${quadraId}`, error);
    }
  };

  // eslint-disable-next-line react-hooks/exhaustive-deps
  useEffect(() => {
    const inicializar = async () => {
      await carregarQuadras();
    };
    inicializar();
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

  const handleEnviarAvaliacao = async (quadraId) => {
    const nota = notasTemp[quadraId];
    if (!nota || nota < 1 || nota > 5) {
      alert("Por favor, digite uma nota entre 1 e 5.");
      return;
    }
    
    try {
      await api.post(`/quadras/${quadraId}/avaliacoes`, { nota: parseInt(nota) });
      setNotasTemp(prev => ({ ...prev, [quadraId]: '' })); // Limpa o campo
      carregarAvaliacoes(quadraId); // Recarrega a média
      alert("Avaliação registrada com sucesso!");
    } catch (error) {
      console.error(error);
      alert("Erro ao enviar avaliação.");
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
            
            {/* NOVO: Seção do Microsserviço de Avaliações */}
            <div className="avaliacao-section">
              <span className="media-nota">⭐ {avaliacoesQuadra[q.id] || 'Carregando...'}</span>
              <div className="avaliar-controls">
                <input 
                  type="number" 
                  min="1" 
                  max="5" 
                  placeholder="1 a 5"
                  value={notasTemp[q.id] || ''}
                  onChange={e => setNotasTemp(prev => ({ ...prev, [q.id]: e.target.value }))}
                />
                <button onClick={() => handleEnviarAvaliacao(q.id)}>Avaliar</button>
              </div>
            </div>
            {/* FIM DA SEÇÃO */}

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