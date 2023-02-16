package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static dungeonmania.TestUtils.getPlayer;
import static dungeonmania.TestUtils.getEntities;
import static dungeonmania.TestUtils.getInventory;
import static dungeonmania.TestUtils.getGoals;
import static dungeonmania.TestUtils.countEntityOfType;
import static dungeonmania.TestUtils.getValueFromConfigFile;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.RoundResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class TimeTravelTests {
    @Test
    @DisplayName("Test changes the position of moving entities")
    public void testTimeTravelEntityPositions() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("testTimeTurner",
                "c_movementTest_testMovementDown");
        DungeonResponse initDungeon = dmc.tick(Direction.DOWN);
        assertTrue(getEntities(initDungeon, "time_turner").size() == 0);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        DungeonResponse rewindedRes = dmc.rewind(1);
        assertTrue(getEntities(rewindedRes, "player").size() > 1);
    }

    @Test
    @DisplayName("Test preserve inventory")
    public void testPreserveInventory() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("testTimeTurner",
                "c_movementTest_testMovementDown");
        DungeonResponse initDungeon = dmc.tick(Direction.DOWN);
        assertTrue(getEntities(initDungeon, "time_turner").size() == 0);
        dmc.tick(Direction.DOWN);
        DungeonResponse collectRes = dmc.tick(Direction.DOWN);
        int collectSunStones = getInventory(collectRes, "sun_stone").size();
        DungeonResponse rewindedRes = dmc.rewind(1);
        assertEquals(collectSunStones, getInventory(rewindedRes, "sun_stone").size());
    }
}