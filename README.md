# Ghost Grid: A Study in API-First Architecture

Welcome to the Ghost Grid demonstration. This project is designed to show the full lifecycle of a modern web application—from architectural negotiation to automated implementation.

## 1. Project Philosophy
Instead of jumping straight into code, this project uses an API-First approach. We define the "Contract" (the rules of engagement) before building the "Engine" (the server) or the "Interface" (the client). This allows different teams—or AI agents—to work in parallel without breaking the system.

## 2. Repository Structure

This is a monorepo organized by Responsibility:

- ghost-grid/
  - design/ : THE ARCHITECT'S OFFICE. Contains rest-api.md (the law), server-design.md (the engine blueprint), and client-design.md (the UI blueprint).
  - server/ : THE ENGINE. The Spring Boot backend that manages world logic, players, and messages.
  - client/ : THE INTERFACE. The React frontend that renders the 5x5 view for the user.
  - config/ : THE WORLD DATA. Contains map.txt, the ASCII file that defines the game world.
  - gemini.md : THE AI GUARDRAILS. The specific instructions used to guide the Gemini CLI.

## 3. How the Pieces Fit Together

1. The Map (config/map.txt): You can change the entire game world by simply editing this text file. No code changes required.
2. The Server (server/): Reads the map file, watches for players, and serves the API. It doesn't care if the client is a website, a mobile app, or a CLI.
3. The Client (client/): Asks the server "What do I see?" and receives a 5x5 grid. It is "dumb"—it only knows what the server shows it.
4. The API (design/rest-api.md): This is the glue. As long as both the server and client follow this document, the system works.

## 4. Learning Objectives
As you explore this project, pay attention to:
- Separation of Concerns: Notice how the game logic lives in the server, while the visual logic lives in the client.
- Statelessness: The server doesn't "know" who you are until you send your username in the URL.
- Documentation as Code: See how the .md files in the design/ folder aren't just notes—they are the blueprints that generated the application.