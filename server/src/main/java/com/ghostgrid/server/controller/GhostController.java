package com.ghostgrid.server.controller;

import com.ghostgrid.server.model.*;
import com.ghostgrid.server.service.GameStateService;
import com.ghostgrid.server.service.MapService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/ghosts")
@CrossOrigin(origins = "*")
public class GhostController {

    private static final Logger log = LoggerFactory.getLogger(GhostController.class);

    private final GameStateService gameStateService;
    private final MapService mapService;

    public GhostController(GameStateService gameStateService, MapService mapService) {
        this.gameStateService = gameStateService;
        this.mapService = mapService;
    }

    @GetMapping("/move")
    public ResponseEntity<?> move(
            @RequestParam String user,
            @RequestParam String dir) {
        try {
            Position newPos = gameStateService.movePlayer(user, dir);
            String[][] view = gameStateService.getView(newPos);
            
            String msgText;
            if ("stay".equals(dir)) {
                msgText = "Looked around successfully.";
            } else {
                msgText = "Moved " + dir + " successfully.";
            }

            MoveResponse response = new MoveResponse(
                    user,
                    newPos,
                    "success",
                    msgText,
                    view
            );
            
            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("status", "error", "message", e.getMessage()));
        } catch (Exception e) {
            log.error("Error during move", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", "error", "message", "An unexpected error occurred."));
        }
    }

    @GetMapping("/interact")
    public ResponseEntity<?> readMessages(
            @RequestParam String user,
            @RequestParam String dir) {
        try {
            Position currentPos = gameStateService.getPlayerPosition(user);
            Position targetPos = gameStateService.getTargetPosition(currentPos, dir);
            String tileType = mapService.getTileType(targetPos.getRow(), targetPos.getCol());

            if (!"message_box".equals(tileType)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("status", "error", "message", "There is nothing to interact with there."));
            }

            InteractResponse response = new InteractResponse(
                    "message_box",
                    targetPos,
                    gameStateService.getMessages(targetPos)
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error during interact read", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", "error", "message", "An unexpected error occurred."));
        }
    }

    @PostMapping("/interact")
    public ResponseEntity<?> postMessage(
            @RequestParam String user,
            @RequestParam String dir,
            @RequestBody Map<String, String> body) {
        try {
            Position currentPos = gameStateService.getPlayerPosition(user);
            Position targetPos = gameStateService.getTargetPosition(currentPos, dir);
            String tileType = mapService.getTileType(targetPos.getRow(), targetPos.getCol());

            if (!"message_box".equals(tileType)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("status", "error", "message", "There is nothing to interact with there."));
            }

            String text = body.get("text");
            if (text == null || text.isBlank()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("status", "error", "message", "Message text cannot be empty."));
            }

            gameStateService.addMessage(targetPos, user, text);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("status", "success", "message", "Message successfully posted."));
        } catch (Exception e) {
            log.error("Error during interact write", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", "error", "message", "An unexpected error occurred."));
        }
    }
}
