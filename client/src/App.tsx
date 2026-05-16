import React, { useState } from 'react';
import { Ghost, ChevronUp, ChevronDown, ChevronLeft, ChevronRight, X, Mail } from 'lucide-react';
import { api } from './api';
import type { ViewResponse, InteractResponse } from './api';
import './App.css';

const App: React.FC = () => {
  const [username, setUsername] = useState<string>(localStorage.getItem('ghost_user') || '');
  const [isLoggedIn, setIsLoggedIn] = useState<boolean>(false);
  const [gameState, setGameState] = useState<ViewResponse | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [interactTarget, setInteractTarget] = useState<{ dir: string; data: InteractResponse } | null>(null);
  const [newMessage, setNewMessage] = useState<string>('');

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!username.trim()) return;
    
    try {
      localStorage.setItem('ghost_user', username);
      const data = await api.spawn(username);
      setGameState(data);
      setIsLoggedIn(true);
      setError(null);
    } catch (err: any) {
      if (err.message.includes('already active')) {
        // Fallback to move/stay if already active
        try {
          const data = await api.move(username, 'wait');
          setGameState(data);
          setIsLoggedIn(true);
          setError(null);
        } catch (innerErr: any) {
          setError(innerErr.message);
        }
      } else {
        setError(err.message);
      }
    }
  };

  const handleMove = async (dir: string) => {
    try {
      const data = await api.move(username, dir);
      setGameState(data);
      setError(null);
    } catch (err: any) {
      setError(err.message);
    }
  };

  const handleInteract = async (dir: string) => {
    try {
      const data = await api.interactRead(username, dir);
      setInteractTarget({ dir, data });
      setError(null);
    } catch (err: any) {
      setError(err.message);
    }
  };

  const handlePostMessage = async () => {
    if (!interactTarget || !newMessage.trim()) return;
    try {
      await api.interactWrite(username, interactTarget.dir, newMessage);
      // Refresh messages
      const data = await api.interactRead(username, interactTarget.dir);
      setInteractTarget({ ...interactTarget, data });
      setNewMessage('');
      setError(null);
    } catch (err: any) {
      setError(err.message);
    }
  };

  if (!isLoggedIn) {
    return (
      <div className="app-container">
        <div className="login-card">
          <h1>Ghost Grid</h1>
          <form onSubmit={handleLogin}>
            <input
              type="text"
              placeholder="Enter Username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              autoFocus
            />
            <br />
            <button type="submit">Enter the Grid</button>
          </form>
          {error && <p className="status-error">{error}</p>}
        </div>
      </div>
    );
  }

  const adjacentMessageBoxes = () => {
    if (!gameState) return [];
    const view = gameState.view;
    const dirs = [];
    if (view[1][2] === 'message_box') dirs.push('up');
    if (view[3][2] === 'message_box') dirs.push('down');
    if (view[2][1] === 'message_box') dirs.push('left');
    if (view[2][3] === 'message_box') dirs.push('right');
    return dirs;
  };

  return (
    <div className="app-container">
      <div className="game-container">
        <div className="status-bar">
          <div className={error ? 'status-error' : ''}>
            {error || gameState?.message || `Welcome, ${username}!`}
          </div>
          <div style={{ fontSize: '0.7rem', color: '#888', marginTop: '4px' }}>
            Pos: ({gameState?.coordinates.row}, {gameState?.coordinates.col})
          </div>
        </div>

        <div className="grid-container">
          {gameState?.view.map((row, rIdx) => 
            row.map((tile, cIdx) => (
              <div 
                key={`${rIdx}-${cIdx}`} 
                className={`tile ${tile === 'PLAYER' ? 'player' : tile}`}
              >
                {tile === 'PLAYER' && <Ghost className="player-icon" size={32} />}
                {tile === 'message_box' && <Mail size={24} />}
              </div>
            ))
          )}
        </div>

        <div className="controls">
          <div className="d-pad">
            <button className="up" onClick={() => handleMove('up')} title="Move Up"><ChevronUp /></button>
            <button className="left" onClick={() => handleMove('left')} title="Move Left"><ChevronLeft /></button>
            <button className="down" onClick={() => handleMove('down')} title="Move Down"><ChevronDown /></button>
            <button className="right" onClick={() => handleMove('right')} title="Move Right"><ChevronRight /></button>
          </div>

          <div className="interact-controls">
            {adjacentMessageBoxes().map(dir => (
              <button 
                key={dir} 
                className="interact-btn"
                onClick={() => handleInteract(dir)}
              >
                Interact {dir.toUpperCase()}
              </button>
            ))}
          </div>
        </div>
      </div>

      {interactTarget && (
        <div className="modal-overlay">
          <div className="modal-content">
            <button className="close-btn" onClick={() => setInteractTarget(null)}>
              <X size={24} />
            </button>
            <h3>Message Box ({interactTarget.data.coordinates.row}, {interactTarget.data.coordinates.col})</h3>
            
            <div className="message-list">
              {interactTarget.data.messages.length === 0 ? (
                <p style={{ textAlign: 'center', color: '#888' }}>No messages yet.</p>
              ) : (
                interactTarget.data.messages.map((msg, i) => (
                  <div key={i} className="message-item">
                    <div className="message-header">
                      <strong>{msg.user}</strong> • {new Date(msg.timestamp).toLocaleString()}
                    </div>
                    <div className="message-text">{msg.text}</div>
                  </div>
                ))
              )}
            </div>

            <div className="write-message">
              <textarea
                placeholder="Leave a message..."
                value={newMessage}
                onChange={(e) => setNewMessage(e.target.value)}
                rows={3}
              />
              <button onClick={handlePostMessage}>Post Message</button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default App;
