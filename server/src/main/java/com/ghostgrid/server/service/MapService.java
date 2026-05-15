package com.ghostgrid.server.service;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class MapService {

    private static final Logger log = LoggerFactory.getLogger(MapService.class);

    @Value("${ghostgrid.map-path}")
    private String mapPath;

    private char[][] grid;
    private int rows;
    private int cols;

    @PostConstruct
    public void init() {
        loadMap();
    }

    private void loadMap() {
        log.info("Loading map from: {}", mapPath);
        Resource resource = new FileSystemResource(mapPath);
        
        if (!resource.exists()) {
            log.warn("Map file not found at {}. Trying classpath...", mapPath);
        }

        try (InputStream is = resource.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            
            List<String> lines = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }

            if (lines.isEmpty()) {
                throw new RuntimeException("Map file is empty");
            }

            rows = lines.size();
            cols = lines.get(0).length();
            grid = new char[rows][cols];

            for (int r = 0; r < rows; r++) {
                String rowStr = lines.get(r);
                for (int c = 0; c < cols; c++) {
                    if (c < rowStr.length()) {
                        grid[r][c] = rowStr.charAt(c);
                    } else {
                        grid[r][c] = '#';
                    }
                }
            }
            log.info("Map loaded successfully: {}x{}", rows, cols);
        } catch (IOException e) {
            log.error("Failed to load map file", e);
            throw new RuntimeException("Could not load map file", e);
        }
    }

    public char[][] getGrid() {
        return grid;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public String getTileType(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            return "out_of_bounds";
        }
        char symbol = grid[row][col];
        return switch (symbol) {
            case '#' -> "wall";
            case '.' -> "floor";
            case 'M' -> "message_box";
            default -> "out_of_bounds";
        };
    }

    public boolean isWalkable(int row, int col) {
        return "floor".equals(getTileType(row, col));
    }
}
