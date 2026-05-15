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

### Files Created or Modified:
- `server/pom.xml`
- `server/src/main/resources/application.yaml`
- `server/src/main/java/com/ghostgrid/server/ServerApplication.java`
- `server/src/main/java/com/ghostgrid/server/model/Position.java`
- `server/src/main/java/com/ghostgrid/server/model/Message.java`
- `server/src/main/java/com/ghostgrid/server/model/MoveResponse.java`
- `server/src/main/java/com/ghostgrid/server/model/InteractResponse.java`
- `server/src/main/java/com/ghostgrid/server/service/MapService.java`
- `server/src/main/java/com/ghostgrid/server/service/GameStateService.java`
- `server/src/main/java/com/ghostgrid/server/controller/GhostController.java`
- `server/src/test/java/com/ghostgrid/server/ServerApplicationTests.java`
- `config/map.txt`
