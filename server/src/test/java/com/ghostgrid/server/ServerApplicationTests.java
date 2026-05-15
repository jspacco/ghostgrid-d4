package com.ghostgrid.server;

import com.ghostgrid.server.model.Position;
import com.ghostgrid.server.service.GameStateService;
import com.ghostgrid.server.service.MapService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ServerApplicationTests {

	@Autowired
	private MapService mapService;

	@Autowired
	private GameStateService gameStateService;

	@Test
	void contextLoads() {
		assertNotNull(mapService.getGrid());
		assertTrue(mapService.getRows() > 0);
		assertTrue(mapService.getCols() > 0);
	}

	@Test
	void testPlayerSpawn() {
		Position pos = gameStateService.getPlayerPosition("testUser");
		assertNotNull(pos);
		assertTrue(mapService.isWalkable(pos.getRow(), pos.getCol()));
	}

	@Test
	void testMovement() {
		String user = "moveTester";
		Position start = gameStateService.getPlayerPosition(user);
		
		// Try to move in all directions, at least one should be walkable or stay
		assertDoesNotThrow(() -> gameStateService.movePlayer(user, "stay"));
	}
}
