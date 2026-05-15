import React, { useState } from 'react';

interface LoginProps {
  onLogin: (username: string) => void;
}

const Login: React.FC<LoginProps> = ({ onLogin }) => {
  const [username, setUsername] = useState('');

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (username.trim()) {
      onLogin(username.trim());
    }
  };

  return (
    <div className="login-screen">
      <div className="login-card">
        <h1>GHOST GRID</h1>
        <p>Enter the world of invisible spirits.</p>
        <form onSubmit={handleSubmit}>
          <input 
            type="text" 
            placeholder="Choose your Ghost Name" 
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            autoFocus
          />
          <button type="submit">Enter the Grid</button>
        </form>
      </div>
    </div>
  );
};

export default Login;
