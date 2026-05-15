import React, { useState, useEffect } from 'react';
import { apiService, Message } from '../services/apiService';

interface MessageInterfaceProps {
  user: string;
  direction: string;
  onClose: () => void;
}

const MessageInterface: React.FC<MessageInterfaceProps> = ({ user, direction, onClose }) => {
  const [messages, setMessages] = useState<Message[]>([]);
  const [newMessage, setNewMessage] = useState('');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    fetchMessages();
  }, [direction]);

  const fetchMessages = async () => {
    try {
      setLoading(true);
      const data = await apiService.getMessages(user, direction);
      setMessages(data.messages);
      setError(null);
    } catch (err: any) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  const handlePost = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!newMessage.trim()) return;

    try {
      await apiService.postMessage(user, direction, newMessage);
      setNewMessage('');
      fetchMessages(); // Refresh list
    } catch (err: any) {
      setError(err.message);
    }
  };

  return (
    <div className="modal-overlay">
      <div className="message-modal">
        <header>
          <h2>Message Box ({direction})</h2>
          <button className="close-btn" onClick={onClose}>×</button>
        </header>

        <div className="message-list">
          {loading ? (
            <p>Loading messages...</p>
          ) : error ? (
            <p className="error">{error}</p>
          ) : messages.length === 0 ? (
            <p className="empty">No messages yet. Be the first!</p>
          ) : (
            messages.map((msg, i) => (
              <div key={i} className="message-item">
                <span className="msg-user">{msg.user}</span>
                <p className="msg-text">{msg.text}</p>
                <span className="msg-date">{new Date(msg.timestamp).toLocaleString()}</span>
              </div>
            ))
          )}
        </div>

        <form className="message-form" onSubmit={handlePost}>
          <input 
            type="text" 
            placeholder="Write a message..." 
            value={newMessage}
            onChange={(e) => setNewMessage(e.target.value)}
          />
          <button type="submit">Post</button>
        </form>
      </div>
    </div>
  );
};

export default MessageInterface;
