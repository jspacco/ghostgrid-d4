# GHOST GRID MASTER DESIGN DOCUMENT (v1.0)

## 1. PROJECT OVERVIEW
The Ghost Grid is a synchronous, multiplayer backend where players navigate a 2D grid world. While players are invisible to each other, they interact by leaving persistent messages in "Message Boxes" scattered throughout the map.

## 2. WORLD ARCHITECTURE
* **Coordinate System:** (row, col) with (0,0) at the Top-Left.
* **Movement:** Up, Down, Left, Right, or Stay (1 square per move).
* **Visibility:** A 5x5 grid centered on the player (player is always at view[2][2]).
* **Tile Legend:**
    - `floor`: Walkable.
    - `wall`: Impassable.
    - `message_box`: Impassable; interactive from an adjacent tile.
    - `out_of_bounds`: Beyond map limits defined by the ASCII map file.

## 3. SERVER IMPLEMENTATION & STATE
### A. Configuration
- **Map Loading:** The server reads an ASCII file (e.g., `map.txt`) defined in `application.yaml` under `ghostgrid.map-path`.
- **Symbols:** '#' (Wall), '.' (Floor), 'M' (Message Box).
- **Resource Handling:** Loads from absolute path first, with a ClassPath fallback.

### B. State Management (In-Memory)
- **Player Registry:** `ConcurrentHashMap<String, Position>` (username -> coordinates). 
- **Spawning:** New users are automatically initialized at a valid random 'floor' tile.
- **Message Registry:** `ConcurrentHashMap<Position, List<Message>>` (box coordinates -> messages). 
- **Message Ordering:** Newest messages are prepended to index 0.

## 4. REST API SPECIFICATION

### A. MOVE / LOOK
Updates player location and returns the 5x5 surrounding view.
- **Method:** GET
- **Path:** `/ghosts/move`
- **Params:** `user=[string]`, `dir=[up|down|left|right|stay]`
- **Success (200 OK):**
  {
    "user": "spacco",
    "coordinates": { "row": 5, "col": 5 },
    "status": "success",
    "message": "Moved south successfully.",
    "view": [
      ["wall", "wall", "floor", "floor", "floor"],
      ["floor", "floor", "floor", "floor", "floor"],
      ["floor", "floor", "PLAYER", "floor", "floor"],
      ["floor", "message_box", "floor", "wall", "wall"],
      ["out_of_bounds", "out_of_bounds", "wall", "wall", "wall"]
    ]
  }
- **Failure (400 Bad Request):** { "status": "error", "message": "Ouch! You hit a wall." }

### B. INTERACT (READ)
Retrieves the message list from a message_box in a specific direction.
- **Method:** GET
- **Path:** `/ghosts/interact`
- **Params:** `user=[string]`, `dir=[up|down|left|right]`
- **Success (200 OK):**
  {
    "object_type": "message_box",
    "coordinates": { "row": 6, "col": 5 },
    "messages": [
      { "user": "ghost_king", "text": "The exit is to the east.", "timestamp": "2026-05-13T12:00:00Z" }
    ]
  }

### C. INTERACT (WRITE)
Leaves a new message in the box in a specific direction.
- **Method:** POST
- **Path:** `/ghosts/interact`
- **Params:** `user=[string]`, `dir=[up|down|left|right]`
- **Body:** { "text": "Your message here." }
- **Success (201 Created):** { "status": "success", "message": "Message successfully posted." }
- **Failure (404 Not Found):** { "status": "error", "message": "There is nothing to interact with there." }

## 5. TECHNICAL CONCERNS
- **CORS:** Global `@CrossOrigin` enabled for client connectivity.
- **Thread Safety:** All service methods must be thread-safe.
- **Error Handling:** Logic failures must return JSON objects with "status" and "message" fields.