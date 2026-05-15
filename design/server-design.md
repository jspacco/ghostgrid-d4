# SERVER DESIGN DOCUMENT: GHOST GRID ENGINE (v1.0)

## 1. ARCHITECTURAL OVERVIEW
The server is a Spring Boot 3.x application built using the Gradle build tool. It implements the logic for a 2D grid world, managing player movement, world visibility, and persistent message objects. It serves as the authoritative engine for the Ghost Grid REST API.

## 2. BUILD SYSTEM & STRUCTURE
- **Build Tool**: Gradle (Groovy).
- **Project Metadata**: Group ID: knox.ghostgrid, Artifact ID: server.
- **Key Dependencies**: 'spring-boot-starter-web'.

## 3. CONFIGURATION & MAP LOADING
The world layout is decoupled from the code and defined by an external ASCII map file.

### A. application.yaml Configuration
The server must read the map file path from the following configuration property:
`ghostgrid.map-path`

**Path Resolution Logic**:
1. **Override**: If a path is specified in `application.yaml` under `ghostgrid.map-path`, the server must use that specific path.
2. **Default**: If no override is provided, the engine defaults to looking for the 'config' folder relative to the project root: `server/config/map.txt`.

### B. Map Parsing Logic
The server reads the text file at startup (@PostConstruct) and converts it into a 2D data structure.
- '#' (Wall): Impassable tile.
- '.' (Floor): Walkable tile.
- 'M' (Message Box): Impassable, but allows GET/POST interaction.
- The grid dimensions are determined by the line lengths and total lines in the file.

## 4. STATE MANAGEMENT (IN-MEMORY)
The server maintains a shared world state using thread-safe collections to support multiple concurrent users without a database.

### A. Player Registry
- **Data Structure**: `ConcurrentHashMap<String, Position>`.
- **Logic**: Maps a "username" to their current (row, col) coordinates.

### B. Message Registry
- **Data Structure**: `ConcurrentHashMap<Position, List<Message>>`.
- **Logic**: Maps the specific coordinates of a 'message_box' to a list of message objects. 
- **Message Object**: Contains {user, text, timestamp}.
- **Ordering**: New messages are prepended (index 0) so that clients receive the most recent history first.

## 5. ENGINE LOGIC & VALIDATION

### A. Spawning Logic (/ghosts/spawn)
- **Endpoint**: `POST /ghosts/spawn?user=[username]`
- **Validation**: If the username already exists in the `Player Registry`, return a 400 Bad Request error ("User is already active").
- **Assignment**: Assign the user a valid random 'floor' tile coordinate.
- **Initialization**: Store the user's initial position and return the 5x5 visibility window.

### B. The 5x5 Visibility Engine
For every movement, look, or spawn request, the server generates a 5x5 sub-grid centered on the player (where the player is index [2,2]).
- **Boundary Protection**: For any coordinate in the 5x5 window that falls outside the map dimensions, the server must return "out_of_bounds" for that tile.

### C. Movement Rules (/ghosts/move)
- **Valid Moves**: Players can only move into 'floor' tiles.
- **Blocked Moves**: Attempts to move into 'wall', 'message_box', or 'out_of_bounds' must fail (triggering the API's 400 error).
- **Auto-Registration**: If a user is seen for the first time on a `/move` request without having spawned, they should be automatically spawned (legacy support).

### C. Interaction Rules
- Interaction is only valid if the tile exactly 1 square away in the user's specified direction is a 'message_box'.
- If the target tile is not a message_box, the interaction must fail (triggering the API's 404 error).

## 6. TECHNICAL CROSS-CUTTING CONCERNS
- **CORS**: A global `@CrossOrigin` configuration must be applied.
- **Thread Safety**: All service-level methods must be thread-safe for multi-player concurrency.
- **Error Handling**: Logic failures must return JSON objects matching the "status/message" structure of the REST API.