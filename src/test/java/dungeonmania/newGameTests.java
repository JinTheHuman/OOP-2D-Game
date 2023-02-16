package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.response.models.*;
import dungeonmania.util.Position;
import static dungeonmania.TestUtils.*;

public class newGameTests {
    @Test
    @DisplayName("Test basic game creation")
    public void testBasicNewGame() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_spiderTest_basicMovement", "c_spiderTest_basicMovement");
        
        List<EntityResponse> allEntities = getEntities(initDungonRes, "");
        assertEquals(allEntities.size(), 4);

        EntityResponse player = getPlayer(initDungonRes).get();
        assertEquals(player.getPosition(), new Position(1, 1));

        List<EntityResponse> wallResponse = getEntities(initDungonRes, "wall");
        assertEquals(wallResponse.size(), 1);
        assertEquals(wallResponse.get(0).getPosition(), new Position(1, 0));

        List<EntityResponse> spiderResponse = getEntities(initDungonRes, "spider");
        assertEquals(spiderResponse.size(), 1);
        assertEquals(spiderResponse.get(0).getPosition(), new Position(5, 5));

        List<EntityResponse> exitResponse = getEntities(initDungonRes, "exit");
        assertEquals(exitResponse.size(), 1);
        assertEquals(exitResponse.get(0).getPosition(), new Position(5, 1));


        List<ItemResponse> inventory = getInventory(initDungonRes, "");
        assertEquals(inventory.size(), 0);
    }

    @Test
    @DisplayName("Test game creation with invalid dungeonName")
    public void testInvalidDungeonName() {
        DungeonManiaController dmc = new DungeonManiaController();
        assertThrows(IllegalArgumentException.class, () -> dmc.newGame("invalid dungeon name", "c_spiderTest_basicMovement"));
    }

    @Test
    @DisplayName("Test game creation with invalid configName")
    public void testInvalidConfigName() {
        DungeonManiaController dmc = new DungeonManiaController();
        assertThrows(IllegalArgumentException.class, () -> dmc.newGame("d_spiderTest_basicMovement", "invalid config name"));
    }
}
