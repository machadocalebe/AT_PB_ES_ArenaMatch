/* eslint-disable */
import { useState, useEffect } from 'react';
import { Calendar, dateFnsLocalizer } from 'react-big-calendar';
import withDragAndDrop from 'react-big-calendar/lib/addons/dragAndDrop';
import format from 'date-fns/format';
import parse from 'date-fns/parse';
import startOfWeek from 'date-fns/startOfWeek';
import getDay from 'date-fns/getDay';
import ptBR from 'date-fns/locale/pt-BR';
import 'react-big-calendar/lib/css/react-big-calendar.css';
import 'react-big-calendar/lib/addons/dragAndDrop/styles.css';
import api from '../services/api';
import './Agenda.css';

const locales = { 'pt-BR': ptBR };
const localizer = dateFnsLocalizer({ format, parse, startOfWeek, getDay, locales });
const DnDCalendar = withDragAndDrop(Calendar);

export default function Agenda() {
  const [reservas, setReservas] = useState([]);
  const [quadras, setQuadras] = useState([]);
  
  const [nomeLocatario, setNomeLocatario] = useState('');
  const [quadraId, setQuadraId] = useState('');
  const [dataHora, setDataHora] = useState('');

  const [currentDate, setCurrentDate] = useState(new Date());
  const [currentView, setCurrentView] = useState('week');

  const carregarDados = async () => {
    try {
      const resQuadras = await api.get('/quadras');
      setQuadras(resQuadras.data);

      const resReservas = await api.get('/reservas');
      const eventosFormatados = resReservas.data.map(res => ({
        id: res.id,
        title: `${res.quadra.nome} - ${res.nomeLocatario}`,
        start: new Date(res.dataHoraInicio),
        end: new Date(new Date(res.dataHoraInicio).getTime() + 60 * 60 * 1000), // +1 hora
        nomeLocatario: res.nomeLocatario,
        quadraId: res.quadra.id
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

  const onEventDrop = async ({ event, start, end }) => {
    try {
      const dataHoraInicio = start.toISOString().slice(0, 19);
      await api.put(`/reservas/${event.id}`, { 
        nomeLocatario: event.nomeLocatario,
        quadraId: event.quadraId,
        dataHoraInicio
      });
      carregarDados();
    } catch (error) {
      alert("❌ Erro ao mover reserva: " + (error.response?.data?.message || error.message));
    }
  };

  const handleExcluirReserva = async (evento) => {
    if (window.confirm(`Deseja cancelar a reserva: ${evento.title}?`)) {
      try {
        await api.delete(`/reservas/${evento.id}`);
        carregarDados();
      } catch (error) {
        console.error(error);
        alert("Erro ao excluir reserva.");
      }
    }
  };

  return (
    <div className="agenda-container animate-fade-in">
      <header className="agenda-header">
        <h1>Agenda de Quadras</h1>
        <p>Gerencie seus horários com facilidade. Arraste e solte para reagendar.</p>
      </header>

      <div className="agenda-layout">
        <aside className="agenda-sidebar">
          <div className="glass-card new-reservation-card">
            <h2>Nova Reserva</h2>
            <form className="form-reserva" onSubmit={handleCriarReserva}>
              <div className="input-group">
                <label>Locatário</label>
                <input type="text" placeholder="Nome completo" value={nomeLocatario} onChange={e => setNomeLocatario(e.target.value)} required />
              </div>
              <div className="input-group">
                <label>Quadra</label>
                <select value={quadraId} onChange={e => setQuadraId(e.target.value)} required>
                  <option value="" disabled>Selecione uma quadra...</option>
                  {quadras.map(q => (
                    <option key={q.id} value={q.id} disabled={q.emManutencao}>
                      {q.nome} {q.emManutencao ? "(Manutenção)" : ""}
                    </option>
                  ))}
                </select>
              </div>
              <div className="input-group">
                <label>Horário Inicial</label>
                <input type="datetime-local" value={dataHora} onChange={e => setDataHora(e.target.value)} required />
              </div>
              <button type="submit" className="btn-apple-primary">Agendar Horário</button>
            </form>
          </div>
        </aside>

        <main className="agenda-main glass-card">
          <DnDCalendar
            localizer={localizer}
            events={reservas}
            startAccessor="start"
            endAccessor="end"
            
            date={currentDate}
            onNavigate={(newDate) => setCurrentDate(newDate)}
            view={currentView}
            onView={(newView) => setCurrentView(newView)}
            onSelectEvent={handleExcluirReserva}
            onEventDrop={onEventDrop}
            resizable={false}

            views={['month', 'week', 'day']}
            culture="pt-BR"
            messages={{
              next: "Próximo",
              previous: "Anterior",
              today: "Hoje",
              month: "Mês",
              week: "Semana",
              day: "Dia",
              noEventsInRange: "Nenhuma reserva neste período."
            }}
            className="apple-calendar"
          />
        </main>
      </div>
    </div>
  );
}
