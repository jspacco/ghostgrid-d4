package knox.ghostgrid.server.controller;

import knox.ghostgrid.server.model.InteractResponse;
import knox.ghostgrid.server.model.Message;
import knox.ghostgrid.server.model.MoveResponse;
import knox.ghostgrid.server.model.Position;
import knox.ghostgrid.server.service.GameStateService;
import knox.ghostgrid.server.service.MapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ghosts")
@CrossOrigin(origins = "*")
public class GhostController {

    private final GameStateService gameStateService;
    private final MapService mapService;

    @Autowired
    public GhostController(GameStateService gameStateService, MapService mapService) {
        this.gameStateService = gameStateService;
        this.mapService = mapService;
    }

    @PostMapping("/spawn")
    public ResponseEntity<Object> spawn(@RequestParam String user) {
        if (gameStateService.playerExists(user)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("status", "error", "message", "User is already active"));
        }
        Position pos = gameStateService.spawnPlayer(user);
        List<List<String>> view = gameStateService.getView(pos);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MoveResponse(
                user, pos, "success", "Spawned successfully.", view));
    }

    @GetMapping("/move")
    public ResponseEntity<MoveResponse> move(
            @RequestParam String user,
            @RequestParam String dir) {

        Position newPos = gameStateService.movePlayer(user, dir);
        if (newPos != null) {
            List<List<String>> view = gameStateService.getView(newPos);
            return ResponseEntity.ok(new MoveResponse(
                    user, newPos, "success", "Moved " + dir + " successfully.", view));
        } else {
            Position current = gameStateService.getOrCreatePlayerPosition(user);
            List<List<String>> view = gameStateService.getView(current);
            // The API spec says 400 for failure, but let's check the design doc.
            // design/ghost-grid-api.md: "Failure (400 Bad Request): { "status": "error", "message": "Ouch! You hit a wall." }"
            // However, it's often better to return the current view so the client can recover.
            // I'll stick to the spec.
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MoveResponse(
                    user, current, "error", "Ouch! You hit a wall or something solid.", view));
        }
    }

    @GetMapping("/interact")
    public ResponseEntity<InteractResponse> readMessages(
            @RequestParam String user,
            @RequestParam String dir) {

        Position current = gameStateService.getOrCreatePlayerPosition(user);
        Position target = gameStateService.getTargetPosition(current, dir);

        if (target != null && "message_box".equals(mapService.getTileType(target.getRow(), target.getCol()))) {
            List<Message> messages = gameStateService.getMessages(target);
            return ResponseEntity.ok(new InteractResponse("message_box", target, messages));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new InteractResponse("error", "There is nothing to interact with there."));
        }
    }

    @PostMapping("/interact")
    public ResponseEntity<Map<String, String>> postMessage(
            @RequestParam String user,
            @RequestParam String dir,
            @RequestBody Map<String, String> body) {

        Position current = gameStateService.getOrCreatePlayerPosition(user);
        Position target = gameStateService.getTargetPosition(current, dir);

        if (target != null && "message_box".equals(mapService.getTileType(target.getRow(), target.getCol()))) {
            String text = body.get("text");
            gameStateService.addMessage(target, user, text);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("status", "success", "message", "Message successfully posted."));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("status", "error", "message", "There is nothing to interact with there."));
        }
    }
}
