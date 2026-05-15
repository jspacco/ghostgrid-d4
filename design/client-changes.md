# Client Change Log

## [Initial Implementation of Ghost Grid React Client]
- Implemented a React-based SPA with TypeScript.
- Created `apiService.ts` for unified communication with the Spring Boot backend.
- Developed a 5x5 dynamic `GameBoard` that renders visibility data from the server.
- Built a `Login` system that persists the ghost name in `localStorage`.
- Implemented a D-Pad `Controls` component for movement and contextual interaction.
- Developed a `MessageInterface` modal for reading and posting messages to `message_box` tiles.
- Applied custom Vanilla CSS styling for a dark-themed, "haunted" aesthetic.
- Ensured "dumb client" architecture where all game logic resides on the server.

### Files Created or Modified:
- `client/package.json`
- `client/tsconfig.json`
- `client/public/index.html`
- `client/src/index.tsx`
- `client/src/App.tsx`
- `client/src/services/apiService.ts`
- `client/src/components/Login.tsx`
- `client/src/components/GameBoard.tsx`
- `client/src/components/Controls.tsx`
- `client/src/components/MessageInterface.tsx`
- `client/src/styles/App.css`

## [Spawn Endpoint Integration]
- Updated `apiService.ts` to include `spawn` method using `POST /ghosts/spawn`.
- Modified `App.tsx` to call `handleSpawn` on initial login or page load.
- Implemented fallback logic in `App.tsx`: if `spawn` fails because the user is "already active", it automatically calls `move('stay')` to synchronize state.
- Updated `design/client-design.md` to reflect the new explicit spawn protocol.

### Files Created or Modified:
- `client/src/services/apiService.ts`
- `client/src/App.tsx`
- `design/client-design.md`
- `design/client-changes.md`
