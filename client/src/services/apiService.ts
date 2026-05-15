const BASE_URL = 'http://localhost:8080/ghosts';

export interface Position {
  row: number;
  col: number;
}

export interface Message {
  user: String;
  text: String;
  timestamp: string;
}

export interface MoveResponse {
  user: string;
  coordinates: Position;
  status: 'success' | 'error';
  message: string;
  view: string[][];
}

export interface InteractResponse {
  object_type: string;
  coordinates: Position;
  messages: Message[];
  status?: string;
  message?: string;
}

export const apiService = {
  move: async (user: string, dir: string): Promise<MoveResponse> => {
    const response = await fetch(`${BASE_URL}/move?user=${user}&dir=${dir}`);
    const data = await response.json();
    if (!response.ok) {
        throw new Error(data.message || 'Movement failed');
    }
    return data;
  },

  getMessages: async (user: string, dir: string): Promise<InteractResponse> => {
    const response = await fetch(`${BASE_URL}/interact?user=${user}&dir=${dir}`);
    const data = await response.json();
    if (!response.ok) {
        throw new Error(data.message || 'Failed to read messages');
    }
    return data;
  },

  postMessage: async (user: string, dir: string, text: string): Promise<{ status: string; message: string }> => {
    const response = await fetch(`${BASE_URL}/interact?user=${user}&dir=${dir}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ text }),
    });
    const data = await response.json();
    if (!response.ok) {
        throw new Error(data.message || 'Failed to post message');
    }
    return data;
  },
};
