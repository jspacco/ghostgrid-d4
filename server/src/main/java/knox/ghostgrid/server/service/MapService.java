package knox.ghostgrid.server.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class MapService {

    @Value("${ghostgrid.map-path:server/config/map.txt}")
    private String mapPath;

    private char[][] grid;
    private int rows;
    private int cols;

    @PostConstruct
    public void init() throws IOException {
        loadMap();
    }

    private void loadMap() throws IOException {
        Path path = Paths.get(mapPath);
        if (!Files.exists(path)) {
            // Fallback to absolute path or other locations if needed, 
            // but design doc says override then default.
            // Let's assume the path is relative to project root or absolute as provided.
            System.err.println("Map file not found at: " + mapPath + ". Attempting to load from classpath fallback.");
            // Classpath fallback logic could go here, but let's stick to the path for now.
        }

        List<String> lines = Files.readAllLines(path);
        rows = lines.size();
        cols = lines.get(0).length();
        grid = new char[rows][cols];

        for (int i = 0; i < rows; i++) {
            grid[i] = lines.get(i).toCharArray();
        }
        System.out.println("Map loaded: " + rows + "x" + cols);
    }

    public String getTileType(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            return "out_of_bounds";
        }
        char symbol = grid[row][col];
        switch (symbol) {
            case '#': return "wall";
            case '.': return "floor";
            case 'M': return "message_box";
            default: return "unknown";
        }
    }

    public boolean isWalkable(int row, int col) {
        return "floor".equals(getTileType(row, col));
    }

    public int getRows() { return rows; }
    public int getCols() { return cols; }
}
