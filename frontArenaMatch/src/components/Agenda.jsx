import { useState, useEffect } from 'react';
import { Calendar, dateFnsLocalizer } from 'react-big-calendar';
import format from 'date-fns/format';
import parse from 'date-fns/parse';
import startOfWeek from 'date-fns/startOfWeek';
import getDay from 'date-fns/getDay';
import ptBR from 'date-fns/locale/pt-BR';
import 'react-big-calendar/lib/css/react-big-calendar.css';
import api from '../services/api';
import './Agenda.css';

const locales = { 'pt-BR': ptBR };
const localizer = dateFnsLocalizer({ format, parse, startOfWeek, getDay, locales });

export default function Agenda() {
  const [reservas, setReservas] = useState([]);
  const [quadras, setQuadras] = useState([]);
  
  const [nomeLocatario, setNomeLocatario] = useState('');
  const [quadraId, setQuadraId] = useState('');
  const [dataHora, setDataHora] = useState('');

  // ESTADOS QUE CONSERTAM OS BOTÕES DO CALENDÁRIO
  const [currentDate, setCurrentDate] = useState(new Date());
  const [currentView, setCurrentView] = useState('week');

  const carregarDados = async () => {
    try {
      const resQuadras = await api.get('/quadras');
      setQuadras(resQuadras.data);

      const resReservas = await api.get('/reservas');
      const eventosFormatados = resReservas.data.map(res => ({
        id: res.id, // Guardamos o ID real do banco para poder deletar
        title: `${res.quadra.nome} - ${res.nomeLocatario}`,
        start: new Date(res.dataHoraInicio),
        end: new Date(new Date(res.dataHoraInicio).getTime() + 60 * 60 * 1000), // +1 hora
      }));
      setReservas(eventosFormatados);
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => { carregarDados(); }, []);

  const handleCriarReserva = async (e) => {
    e.preventDefault();
    try {
      await api.post('/reservas', { nomeLocatario, quadraId: parseInt(quadraId), dataHoraInicio: dataHora });
      setNomeLocatario(''); setDataHora('');
      carregarDados();
    } catch (error) {
      alert("❌ " + (error.response?.data?.message || "Erro ao criar reserva."));
    }
  };

  // Permite clicar em um evento do calendário para excluí-lo
  const handleExcluirReserva = async (evento) => {
    if (window.confirm(`Deseja cancelar a reserva: ${evento.title}?`)) {
      try {
        await api.delete(`/reservas/${evento.id}`);
        carregarDados();
      } catch (error) {
        alert("Erro ao excluir reserva.");
      }
    }
  };

  return (
    <div className="agenda-container">
      <form className="form-reserva" onSubmit={handleCriarReserva}>
        <div className="input-group">
          <label>Nome do Locatário</label>
          <input type="text" value={nomeLocatario} onChange={e => setNomeLocatario(e.target.value)} required />
        </div>
        <div className="input-group">
          <label>Selecione a Quadra</label>
          <select value={quadraId} onChange={e => setQuadraId(e.target.value)} required>
            <option value="">Selecione...</option>
            {quadras.map(q => (
              <option key={q.id} value={q.id} disabled={q.emManutencao}>
                {q.nome} {q.emManutencao ? "(Manutenção)" : ""}
              </option>
            ))}
          </select>
        </div>
        <div className="input-group">
          <label>Data e Hora</label>
          <input type="datetime-local" value={dataHora} onChange={e => setDataHora(e.target.value)} required />
        </div>
        <button type="submit" className="btn-salvar">Agendar</button>
      </form>

      <div className="calendario-wrapper">
        <Calendar
          localizer={localizer}
          events={reservas}
          startAccessor="start"
          endAccessor="end"
          
          // CONEXÕES OBRIGATÓRIAS PARA OS BOTÕES FUNCIONAREM
          date={currentDate}
          onNavigate={(newDate) => setCurrentDate(newDate)}
          view={currentView}
          onView={(newView) => setCurrentView(newView)}
          onSelectEvent={handleExcluirReserva}

          views={['month', 'week', 'day', 'agenda']}
          culture="pt-BR"
          messages={{
            next: "Próximo",
            previous: "Anterior",
            today: "Hoje",
            month: "Mês",
            week: "Semana",
            day: "Dia",
            agenda: "Agenda",
            noEventsInRange: "Nenhuma reserva neste período."
          }}
        />
      </div>
    </div>
  );
}