# Ghost Grid — Gemini CLI Instructions

## On Every Startup
Before taking any action, you must read the following files in the design/ folder in this specific order to establish context:

1. design/ghost-grid-api.md — The authoritative REST API specification. This is the "contract." Do not deviate from the field names, paths, or HTTP codes defined here.
2. design/server-design.md — The backend implementation requirements (Spring Boot, Map loading, State management).
3. design/client-design.md — The frontend implementation requirements (React, 5x5 Grid UI, Random Spawning).
4. design/server-changes.md — The historical record of all completed backend work.
5. design/client-changes.md — The historical record of all completed frontend work.
6. README.md — For high-level project philosophy and repository structure.

Do not write code or modify files until you have confirmed the current state of the project by reading both changes.md files.

---

## Project Structure
The project is a monorepo containing a Java backend and a React frontend.

Project Layout:
ghost-grid/
├── server/                  # Spring Boot Backend (Java 17+)
├── client/                  # React Frontend
├── design/                  # Design Documents & Change Logs
│   ├── ghost-grid-api.md
│   ├── server-design.md
│   ├── client-design.md
│   ├── server-changes.md
│   └── client-changes.md
└── config/
    └── map.txt              # The authoritative ASCII map file

---

## Implementation Rules
- No Database: All server state must be in-memory using ConcurrentHashMap as specified in the design doc.
- Externalized Map: The server must load its grid from config/map.txt based on the path in application.yaml.
- Random Spawning: If a new user connects, the server must automatically assign them a valid random 'floor' tile coordinate.
- Dumb Client: The React client must not calculate game logic; it only renders the 5x5 view array provided by the server.
- Global CORS: The server must allow requests from the client's origin (standard React development ports).

---

## After Every Task
Once a task is complete, you must document your work and commit the changes before moving to the next task.

1. Update Change Logs:
   - If you modified the backend, append to design/server-changes.md.
   - If you modified the frontend, append to design/client-changes.md.
   
   Format for entries:
   ## [Title of Work Completed]
   - Description of changes and logic implemented.
   - Any architectural decisions made.
   - List of files created or modified.

2. Commit Changes:
   Execute: git add -A && git commit -m "[Server/Client]: [Title of Work]"

## Final Guardrail
If a design document is ambiguous or contradicts a previous implementation, stop and ask for clarification before proceeding.