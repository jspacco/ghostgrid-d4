# SERVER DESIGN DOCUMENT: GHOST GRID ENGINE (v1.0)

## 1. ARCHITECTURAL OVERVIEW
The server is a Spring Boot 3.x application that implements the logic for a 2D grid world. It manages player movement, world visibility, and persistent message objects. It serves as the authoritative engine for the Ghost Grid REST API.

## 2. CONFIGURATION & MAP LOADING
The world layout is decoupled from the code and defined by an external ASCII map file.

### A. application.yaml Configuration
The server must read the file path from the following configuration property:
ghostgrid:
  map-path: "src/main/resources/config/map.txt"

### B. Map Parsing Logic
The server reads the text file at startup (@PostConstruct) and converts it into a 2D data structure.
- '#' (Wall): Impassable tile.
- '.' (Floor): Walkable tile.
- 'M' (Message Box): Impassable, but allows GET/POST interaction.
- The grid dimensions are determined by the line lengths and total lines in the file.

### C. Resource Handling
The engine should attempt to load the file from the absolute path first (allowing for external swaps), with a fallback to the ClassPath (for default distribution).

## 3. STATE MANAGEMENT (IN-MEMORY)
The server maintains a shared world state using thread-safe collections to support multiple concurrent users without a database.

### A. Player Registry
- Data Structure: ConcurrentHashMap<String, Position>
- Logic: Maps a "username" to their current (row, col) coordinates. If a user is seen for the first time, they are initialized at (0, 0).

### B. Message Registry
- Data Structure: ConcurrentHashMap<Position, List<Message>>
- Logic: Maps the specific coordinates of a 'message_box' to a list of message objects. 
- Message Object: Contains {user, text, timestamp}.
- Ordering: New messages are prepended (index 0) so that clients receive the most recent history first.

## 4. ENGINE LOGIC & VALIDATION

### A. The 5x5 Visibility Engine
For every movement or "look" request, the server generates a 5x5 sub-grid centered on the player (where the player is index [2,2]).
- Boundary Protection: For any coordinate in the 5x5 window that falls outside the dimensions defined by the map file, the server must return "out_of_bounds" for that tile.

### B. Movement Rules
- Valid Moves: Players can only move into 'floor' tiles.
- Blocked Moves: Attempts to move into 'wall', 'message_box', or 'out_of_bounds' must fail (triggering the API's 400 error).

### C. Interaction Rules
- Interaction is only valid if the tile exactly 1 square away in the user's specified direction is a 'message_box'.
- If the target tile is 'floor' or 'wall', the interaction logic must fail (triggering the API's 404 error).

## 5. TECHNICAL CROSS-CUTTING CONCERNS
- CORS: A global @CrossOrigin configuration must be applied to allow various client implementations to connect.
- Thread Safety: All service-level methods must be thread-safe to handle synchronous requests from multiple players simultaneously.
- Error Handling: Logic failures must be caught and returned as JSON objects matching the "status/message" structure of the REST API.