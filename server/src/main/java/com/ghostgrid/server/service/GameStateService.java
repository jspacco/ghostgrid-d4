package com.ghostgrid.server.service;

import com.ghostgrid.server.model.Message;
import com.ghostgrid.server.model.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class GameStateService {

    private static final Logger log = LoggerFactory.getLogger(GameStateService.class);

    private final MapService mapService;
    private final Random random = new Random();

    // player username -> position
    private final ConcurrentHashMap<String, Position> players = new ConcurrentHashMap<>();
    
    // message_box position -> list of messages
    private final ConcurrentHashMap<Position, List<Message>> messages = new ConcurrentHashMap<>();

    public GameStateService(MapService mapService) {
        this.mapService = mapService;
    }

    public Position getPlayerPosition(String username) {
        return players.computeIfAbsent(username, k -> spawnPlayer());
    }

    private Position spawnPlayer() {
        int rows = mapService.getRows();
        int cols = mapService.getCols();
        
        while (true) {
            int r = random.nextInt(rows);
            int c = random.nextInt(cols);
            if (mapService.isWalkable(r, c)) {
                log.info("Spawning player at ({}, {})", r, c);
                return new Position(r, c);
            }
        }
    }

    public Position movePlayer(String username, String direction) {
        Position current = getPlayerPosition(username);
        int newRow = current.getRow();
        int newCol = current.getCol();

        switch (direction.toLowerCase()) {
            case "up" -> newRow--;
            case "down" -> newRow++;
            case "left" -> newCol--;
            case "right" -> newCol++;
            case "stay" -> {}
            default -> throw new IllegalArgumentException("Invalid direction: " + direction);
        }

        if (mapService.isWalkable(newRow, newCol)) {
            Position newPos = new Position(newRow, newCol);
            players.put(username, newPos);
            return newPos;
        } else if ("stay".equals(direction.toLowerCase())) {
            return current;
        } else {
            throw new IllegalStateException("Ouch! You hit a " + mapService.getTileType(newRow, newCol) + ".");
        }
    }

    public String[][] getView(Position pos) {
        String[][] view = new String[5][5];
        int startRow = pos.getRow() - 2;
        int startCol = pos.getCol() - 2;

        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 5; c++) {
                if (r == 2 && c == 2) {
                    view[r][c] = "PLAYER";
                } else {
                    view[r][c] = mapService.getTileType(startRow + r, startCol + c);
                }
            }
        }
        return view;
    }

    public List<Message> getMessages(Position boxPos) {
        return messages.getOrDefault(boxPos, Collections.emptyList());
    }

    public void addMessage(Position boxPos, String user, String text) {
        Message msg = new Message(user, text, ZonedDateTime.now());
        messages.computeIfAbsent(boxPos, k -> new CopyOnWriteArrayList<>()).add(0, msg);
    }

    public Position getTargetPosition(Position current, String direction) {
        int r = current.getRow();
        int c = current.getCol();
        switch (direction.toLowerCase()) {
            case "up" -> r--;
            case "down" -> r++;
            case "left" -> c--;
            case "right" -> c++;
            default -> throw new IllegalArgumentException("Invalid direction: " + direction);
        }
        return new Position(r, c);
    }
}
