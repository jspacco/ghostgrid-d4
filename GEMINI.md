# Ghost Grid — Gemini CLI Instructions (D4 Process)

## The D4 Mandate
This project follows **Design Doc Driven Development (D4)**. Your role is to implement the "syntax" while the human architect manages the "surface, fit, and edge cases" through the design documents. You must treat these documents as the authoritative source of truth.

## On Every Startup
Read the following files in this specific order to establish architectural context. Do not write any code until this sequence is complete:

1. README.md — High-level project philosophy and D4 overview.
2. design/ghost-grid-api.md — The authoritative REST API "Contract."
3. design/server-design.md — Backend implementation requirements.
4. design/client-design.md — Frontend implementation requirements.
5. design/server-changes.md — Historical record of backend work.
6. design/client-changes.md — Historical record of frontend work.

---

## Project Structure
Project Layout:
ghost-grid/
├── server/                  # Spring Boot Backend (Java 17+)
│   ├── config/              # Local configuration and map.txt
│   ├── src/                 # Java source code
│   └── build.gradle         # Gradle build file (MANDATORY)
├── client/                  # React Frontend
├── design/                  # D4 Design Documents & Change Logs
└── GEMINI.md                # This instruction file

---

## Implementation Rules
- Build System: The server MUST use **Gradle**. Do not create a pom.xml.
- No Database: All server state must be in-memory using ConcurrentHashMap.
- Externalized Map: The server must load its grid from server/config/map.txt.
- Random Spawning: New users must be assigned a valid random 'floor' tile coordinate on their first request.
- Dumb Client: The React client must not calculate game logic; it only renders the 5x5 view array provided by the server.
- Global CORS: Enable @CrossOrigin for all endpoints to allow React-to-Spring communication.

---

## After Every Task (The D4 Audit)
Once a task is complete, you must document your work to maintain the D4 chain of custody:

1. Update Change Logs:
   - Backend changes -> design/server-changes.md
   - Frontend changes -> design/client-changes.md
   
   Format:
   ## [Task Title]
   - Description of logic implemented.
   - Architectural decisions/edge cases handled.
   - Files created or modified.

2. Commit Changes:
   Execute: git add -A && git commit -m "[Server/Client]: [D4 Task Name]"

## Final Guardrail
You are a literal-minded implementation agent. If a D4 design document is ambiguous or contradicts a previous implementation, STOP and ask the Human Architect for clarification.