package knox.ghostgrid.server.service;

import knox.ghostgrid.server.model.Message;
import knox.ghostgrid.server.model.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class GameStateService {

    private final MapService mapService;
    private final ConcurrentHashMap<String, Position> playerRegistry = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Position, List<Message>> messageRegistry = new ConcurrentHashMap<>();
    private final Random random = new Random();

    @Autowired
    public GameStateService(MapService mapService) {
        this.mapService = mapService;
    }

    public Position getOrCreatePlayerPosition(String username) {
        return playerRegistry.computeIfAbsent(username, k -> findRandomSpawnPoint());
    }

    private Position findRandomSpawnPoint() {
        int rows = mapService.getRows();
        int cols = mapService.getCols();
        while (true) {
            int r = random.nextInt(rows);
            int c = random.nextInt(cols);
            if (mapService.isWalkable(r, c)) {
                return new Position(r, c);
            }
        }
    }

    public Position movePlayer(String username, String direction) {
        Position current = getOrCreatePlayerPosition(username);
        int newRow = current.getRow();
        int newCol = current.getCol();

        switch (direction.toLowerCase()) {
            case "up": newRow--; break;
            case "down": newRow++; break;
            case "left": newCol--; break;
            case "right": newCol++; break;
            case "wait": case "stay": break;
            default: throw new IllegalArgumentException("Invalid direction: " + direction);
        }

        if (mapService.isWalkable(newRow, newCol)) {
            Position newPos = new Position(newRow, newCol);
            playerRegistry.put(username, newPos);
            return newPos;
        } else {
            // Movement blocked
            return null;
        }
    }

    public List<List<String>> getView(Position pos) {
        List<List<String>> view = new ArrayList<>();
        for (int r = pos.getRow() - 2; r <= pos.getRow() + 2; r++) {
            List<String> row = new ArrayList<>();
            for (int c = pos.getCol() - 2; c <= pos.getCol() + 2; c++) {
                if (r == pos.getRow() && c == pos.getCol()) {
                    row.add("PLAYER");
                } else {
                    row.add(mapService.getTileType(r, c));
                }
            }
            view.add(row);
        }
        return view;
    }

    public List<Message> getMessages(Position pos) {
        return messageRegistry.getOrDefault(pos, Collections.emptyList());
    }

    public void addMessage(Position pos, String user, String text) {
        messageRegistry.computeIfAbsent(pos, k -> new CopyOnWriteArrayList<>())
                .add(0, new Message(user, text));
    }

    public Position getTargetPosition(Position current, String direction) {
        int r = current.getRow();
        int c = current.getCol();
        switch (direction.toLowerCase()) {
            case "up": r--; break;
            case "down": r++; break;
            case "left": c--; break;
            case "right": c++; break;
            default: return null;
        }
        return new Position(r, c);
    }
}
