import { useState } from 'react';
import api from '../services/api';
import './Login.css';

export default function Login({ setToken }) {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [isRegister, setIsRegister] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (isRegister) {
        await api.post('/auth/register', { username, password });
        alert('Registrado com sucesso! Faça login agora.');
        setIsRegister(false);
      } else {
        const res = await api.post('/auth/login', { username, password });
        const token = res.data.token;
        localStorage.setItem('token', token);
        localStorage.setItem('username', username);
        setToken(token);
      }
    } catch (error) {
      alert(error.response?.data?.erro || error.response?.data || 'Erro na autenticação');
    }
  };

  return (
    <div className="login-container">
      <div className="login-card glass-card animate-fade-in">
        <div className="login-header">
          <span className="logo-icon">🎾</span>
          <h2>ArenaMatch</h2>
          <p>{isRegister ? 'Crie sua conta' : 'Acesse sua conta'}</p>
        </div>
        
        <form onSubmit={handleSubmit} className="login-form">
          <div className="input-group">
            <label>Usuário</label>
            <input 
              type="text" 
              placeholder="Ex: admin" 
              value={username} 
              onChange={e => setUsername(e.target.value)} 
              required 
            />
          </div>
          <div className="input-group">
            <label>Senha</label>
            <input 
              type="password" 
              placeholder="••••••••" 
              value={password} 
              onChange={e => setPassword(e.target.value)} 
              required 
            />
          </div>
          <button type="submit" className="btn-apple-primary btn-block">
            {isRegister ? 'Registrar' : 'Entrar'}
          </button>
        </form>

        <div className="login-footer">
          <button type="button" className="btn-link" onClick={() => setIsRegister(!isRegister)}>
            {isRegister ? 'Já tenho conta, fazer Login' : 'Não tem conta? Registre-se'}
          </button>
        </div>
      </div>
    </div>
  );
}
