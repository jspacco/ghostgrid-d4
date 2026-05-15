import React from 'react';

interface ControlsProps {
  onMove: (dir: string) => void;
  onInteract: (dir: string) => void;
  view: string[][] | null;
}

const Controls: React.FC<ControlsProps> = ({ onMove, onInteract, view }) => {
  const canInteract = (dir: string): boolean => {
    if (!view) return false;
    let r = 2, c = 2;
    switch (dir) {
      case 'up': r = 1; break;
      case 'down': r = 3; break;
      case 'left': c = 1; break;
      case 'right': c = 3; break;
    }
    return view[r][c] === 'message_box';
  };

  return (
    <div className="controls">
      <div className="d-pad">
        <div className="pad-row">
          <button onClick={() => onMove('up')} className="move-btn">↑</button>
        </div>
        <div className="pad-row">
          <button onClick={() => onMove('left')} className="move-btn">←</button>
          <button onClick={() => onMove('stay')} className="move-btn center">●</button>
          <button onClick={() => onMove('right')} className="move-btn">→</button>
        </div>
        <div className="pad-row">
          <button onClick={() => onMove('down')} className="move-btn">↓</button>
        </div>
      </div>

      <div className="interaction-buttons">
        <h3>Interact</h3>
        <div className="interact-grid">
            <button disabled={!canInteract('up')} onClick={() => onInteract('up')}>Up</button>
            <button disabled={!canInteract('left')} onClick={() => onInteract('left')}>Left</button>
            <button disabled={!canInteract('right')} onClick={() => onInteract('right')}>Right</button>
            <button disabled={!canInteract('down')} onClick={() => onInteract('down')}>Down</button>
        </div>
      </div>
    </div>
  );
};

export default Controls;
