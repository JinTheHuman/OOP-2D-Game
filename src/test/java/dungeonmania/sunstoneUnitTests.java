package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.RoundResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import static dungeonmania.TestUtils.*;

public class sunstoneUnitTests {
    @Test
    @DisplayName("Test sun stone can be collected")
    public void testCollectSunStone() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse actualDungonRes = dmc.newGame("testSunStone",
                "c_sunStoneTestTreasure");

        assertTrue(getInventory(actualDungonRes, "sun_stone").size() == 0);
        actualDungonRes = dmc.tick(Direction.DOWN);
        assertTrue(getInventory(actualDungonRes, "sun_stone").size() == 1);
    }

    @Test
    @DisplayName("Test multiple sun stone can be collected")
    public void testCollectMultipleSunStone() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse actualDungonRes = dmc.newGame("testSunStone",
                "c_sunStoneTestTreasure");

        assertTrue(getInventory(actualDungonRes, "sun_stone").size() == 0);
        dmc.tick(Direction.DOWN);
        actualDungonRes = dmc.tick(Direction.DOWN);
        assertTrue(getInventory(actualDungonRes, "sun_stone").size() == 2);
    }

    @Test
    @DisplayName("Test sun stone can be used to open doors")
    public void testOpenDoorsUsingSunStone() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("testSunStone",
                "c_sunStoneTestTreasure");
        dmc.tick(Direction.DOWN);
        DungeonResponse actualDungonRes = dmc.tick(Direction.DOWN);
        assertTrue(getInventory(actualDungonRes, "sun_stone").size() == 2);
        actualDungonRes = dmc.tick(Direction.DOWN);
        assertTrue(getInventory(actualDungonRes, "sun_stone").size() == 2);
        actualDungonRes = dmc.tick(Direction.DOWN);
        Position pos = getEntities(actualDungonRes, "player").get(0).getPosition();
        assertEquals(new Position(1, 5), pos);
    }

    @Test
    @DisplayName("Test sun stone can be used as a replacement when building")
    public void testBuildUsingReplacementSunStone() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("testSunStone",
                "c_sunStoneTestTreasure");
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.RIGHT);
        DungeonResponse actualDungonRes = dmc.tick(Direction.RIGHT);
        assertTrue(getInventory(actualDungonRes, "wood").size() == 2);
        actualDungonRes = dmc.build("shield");
        assertTrue(getInventory(actualDungonRes, "sun_stone").size() == 1);
        assertTrue(getInventory(actualDungonRes, "shield").size() == 1);
    }

    @Test
    @DisplayName("Test sun stone can not be used to bribe")
    public void testBribeUsingSunStone() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("testSunStoneMerc",
                "c_sunStoneTestTreasure");
        DungeonResponse actualDungonRes = dmc.tick(Direction.DOWN);
        assertThrows(InvalidActionException.class, () -> dmc.interact(getEntities(actualDungonRes, "mercenary").get(0).getId()));
    }

    @Test
    @DisplayName("Test sun stone counts towards the treasure goal")
    public void testSunStoneWithTreasureGoal() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("testSunStone",
                "c_sunStoneTestTreasure");
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        DungeonResponse actualDungonRes = dmc.tick(Direction.RIGHT);
        assertEquals("", actualDungonRes.getGoals());
    }
}
