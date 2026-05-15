# Server Change Log

## [Initial Implementation of Ghost Grid Engine]
- Implemented a Spring Boot 3.x backend for the Ghost Grid.
- Created `MapService` to load and parse ASCII maps from `config/map.txt`.
- Implemented `GameStateService` using `ConcurrentHashMap` for in-memory player and message state.
- Developed `GhostController` exposing the `/ghosts/move` and `/ghosts/interact` REST endpoints.
- Implemented random spawning logic for new users on 'floor' tiles.
- Built a 5x5 visibility engine to provide centered views to clients.
- Added global CORS support for client connectivity.
- Implemented manual boilerplate (getters, setters, loggers) instead of Lombok for better build stability.
- Verified core logic with JUnit tests.

## [Spawn Endpoint Implementation]
- Implemented `POST /ghosts/spawn` endpoint in `GhostController` for explicit player registration.
- Added `playerExists` and `spawnPlayer` methods to `GameStateService`.
- Enforced unique username validation for spawning (returns 400 Bad Request if user already active).
- Added `GhostControllerTest` integration tests to verify successful spawn and failure on duplicate user.
- Updated `design/ghost-grid-api.md` to remove Java-specific data structures and add the spawn contract.
- Updated `design/server-design.md` to include in-memory architecture specifications and spawn logic requirements.

### Files Created or Modified:
- `design/ghost-grid-api.md`
- `design/server-design.md`
- `server/src/main/java/knox/ghostgrid/server/service/GameStateService.java`
- `server/src/main/java/knox/ghostgrid/server/controller/GhostController.java`
- `server/src/test/java/knox/ghostgrid/server/controller/GhostControllerTest.java`
- `design/server-changes.md`
