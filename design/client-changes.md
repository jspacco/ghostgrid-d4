# Client Change Log

## [Initial Implementation of Ghost Grid React Client]
- Bootstrapped a modern React SPA using Vite and TypeScript.
- Implemented `api.ts` service for communication with the Spring Boot backend.
- Created a 5x5 responsive grid UI that dynamically renders the player's surroundings.
- Developed a movement D-Pad and conditional "Interact" buttons for adjacent message boxes.
- Implemented a modal-based message interface for reading and writing messages.
- Added support for persistent username via `localStorage`.
- Handled edge cases such as already-active users and hitting walls with user-friendly error messages.
- Styled the application with a "dark mode" aesthetic using Vanilla CSS and Lucide icons.

### Files Created or Modified:
- `client/` (New directory with Vite project)
- `client/src/api.ts`
- `client/src/App.tsx`
- `client/src/App.css`
- `client/src/index.css`
- `design/client-changes.md`

