import React, { useState, useEffect } from 'react';
import { apiService, MoveResponse, Position } from './services/apiService';
import Login from './components/Login';
import GameBoard from './components/GameBoard';
import Controls from './components/Controls';
import MessageInterface from './components/MessageInterface';
import './styles/App.css';

const App: React.FC = () => {
  const [user, setUser] = useState<string | null>(localStorage.getItem('ghostUser'));
  const [view, setView] = useState<string[][] | null>(null);
  const [coordinates, setCoordinates] = useState<Position | null>(null);
  const [statusMessage, setStatusMessage] = useState<string>('Welcome to Ghost Grid');
  const [isInteracting, setIsInteracting] = useState<string | null>(null); // direction

  useEffect(() => {
    if (user) {
      handleSpawn();
    }
  }, [user]);

  const handleLogin = (username: string) => {
    setUser(username);
    localStorage.setItem('ghostUser', username);
  };

  const handleSpawn = async () => {
    if (!user) return;
    try {
      const data = await apiService.spawn(user);
      setView(data.view);
      setCoordinates(data.coordinates);
      setStatusMessage(data.message);
    } catch (err: any) {
      if (err.message === 'User is already active') {
        // If already active, just sync current state
        handleMove('stay');
      } else {
        setStatusMessage(err.message);
      }
    }
  };

  const handleMove = async (dir: string) => {
    if (!user) return;
    try {
      const data = await apiService.move(user, dir);
      setView(data.view);
      setCoordinates(data.coordinates);
      setStatusMessage(data.message);
    } catch (err: any) {
      setStatusMessage(err.message);
    }
  };

  const handleInteract = (dir: string) => {
    setIsInteracting(dir);
  };

  if (!user) {
    return <Login onLogin={handleLogin} />;
  }

  return (
    <div className="app-container">
      <header>
        <h1>Ghost Grid</h1>
        <div className="user-info">
          <span>Ghost: <strong>{user}</strong></span>
          {coordinates && (
            <span>Pos: ({coordinates.row}, {coordinates.col})</span>
          )}
        </div>
      </header>

      <main>
        <div className="status-bar">{statusMessage}</div>
        
        {view && <GameBoard view={view} />}
        
        <Controls 
            onMove={handleMove} 
            onInteract={handleInteract} 
            view={view}
        />

        {isInteracting && (
            <MessageInterface 
                user={user} 
                direction={isInteracting} 
                onClose={() => setIsInteracting(null)} 
            />
        )}
      </main>
    </div>
  );
};

export default App;
