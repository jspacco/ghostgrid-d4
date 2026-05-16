const BASE_URL = 'http://localhost:8080';

export interface Position {
  row: number;
  col: number;
}

export interface Message {
  user: string;
  text: string;
  timestamp: string;
}

export interface ViewResponse {
  user: string;
  coordinates: Position;
  status: string;
  message: string;
  view: string[][];
}

export interface InteractResponse {
  object_type: string;
  coordinates: Position;
  messages: Message[];
}

export const api = {
  spawn: async (user: string): Promise<ViewResponse> => {
    const response = await fetch(`${BASE_URL}/ghosts/spawn?user=${encodeURIComponent(user)}`, {
      method: 'POST',
    });
    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.message || 'Failed to spawn');
    }
    return response.json();
  },

  move: async (user: string, dir: string): Promise<ViewResponse> => {
    const response = await fetch(
      `${BASE_URL}/ghosts/move?user=${encodeURIComponent(user)}&dir=${dir}`
    );
    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.message || 'Failed to move');
    }
    return response.json();
  },

  interactRead: async (user: string, dir: string): Promise<InteractResponse> => {
    const response = await fetch(
      `${BASE_URL}/ghosts/interact?user=${encodeURIComponent(user)}&dir=${dir}`
    );
    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.message || 'Failed to interact');
    }
    return response.json();
  },

  interactWrite: async (user: string, dir: string, text: string): Promise<{ status: string; message: string }> => {
    const response = await fetch(
      `${BASE_URL}/ghosts/interact?user=${encodeURIComponent(user)}&dir=${dir}`,
      {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ text }),
      }
    );
    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.message || 'Failed to post message');
    }
    return response.json();
  },
};
