# CLIENT DESIGN DOCUMENT: GHOST GRID REACT (v1.0)

## 1. ARCHITECTURAL OVERVIEW
A React-based single-page application (SPA) that acts as a frontend for the Ghost Grid REST API. The client maintains no local game logic; it is a "dumb" renderer that translates server responses into a visual grid and captures user input.

## 2. INITIALIZATION & AUTHENTICATION
- User Entry: On first load, the app displays a "Login" screen asking for a username.
- Identity: Once entered, the username is stored in React state (or localStorage) and appended as the 'user' parameter to every subsequent API call.
- Random Spawn: The client's first action after "login" is to call `POST /ghosts/spawn?user=USERNAME`. If the user is already active (e.g., on a page refresh), the client falls back to `GET /ghosts/move?user=USERNAME&dir=stay` to synchronize state.
- Server Requirement: The server must handle the explicit `/spawn` request by placing new users on a random 'floor' tile.

## 3. UI COMPONENTS & LAYOUT

### A. The Game Board (The 5x5 Grid)
- A 5x5 CSS Grid or Table.
- The player is always visually locked to the center square [2,2].
- Tile Rendering:
    - 'floor': Light grey square.
    - 'wall': Dark grey/black square.
    - 'message_box': An icon or colored square (e.g., Gold).
    - 'out_of_bounds': Empty/Translucent space.
    - 'PLAYER': An icon or "G" character at the center.

### B. Controls (Movement & Interaction)
- D-Pad: Four buttons (Up, Down, Left, Right) that trigger the `/ghosts/move` GET request.
- Interaction Trigger: When a 'message_box' is adjacent to the player (Up, Down, Left, or Right), an "Interact" button appears or becomes active for that specific direction.

### C. The Message Interface (Modal/Overlay)
- Read Mode: Clicking "Interact" fetches the message list via `GET /ghosts/interact` and displays them in a scrollable list (newest first).
- Write Mode: A text input and "Post" button that sends a `POST /ghosts/interact` request with the message body.

## 4. CLIENT STATE MANAGEMENT
- Current View: A 2D array (5x5) updated after every move.
- Current Coordinates: Displayed for user reference.
- Status Message: A "Log" area showing the server's "message" field (e.g., "Ouch! You hit a wall").

## 5. TECHNICAL REQUIREMENTS
- Fetch API: Use standard `fetch()` or `axios` to communicate with the Spring Boot server (default: localhost:8080).
- Error Handling: If the server returns a 400 or 404, the client should display the JSON error message in a "Warning" toast or status bar rather than crashing.
- Styling: Use clean CSS (or Tailwind) to ensure the $5 \times 5$ grid stays proportional regardless of screen size.

## 6. DEVELOPMENT NOTE
The client must strictly follow the `ghost-grid-api.md`. It should never assume it knows the map layout; it must rely entirely on the `view` array returned by the server.