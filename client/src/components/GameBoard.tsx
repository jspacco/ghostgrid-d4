import React from 'react';

interface GameBoardProps {
  view: string[][];
}

const GameBoard: React.FC<GameBoardProps> = ({ view }) => {
  return (
    <div className="game-board">
      {view.map((row, rowIndex) => (
        <div key={rowIndex} className="grid-row">
          {row.map((tile, colIndex) => (
            <div 
                key={`${rowIndex}-${colIndex}`} 
                className={`tile ${tile.toLowerCase()}`}
                title={tile}
            >
              {tile === 'PLAYER' && <span className="player-icon">G</span>}
            </div>
          ))}
        </div>
      ))}
    </div>
  );
};

export default GameBoard;
