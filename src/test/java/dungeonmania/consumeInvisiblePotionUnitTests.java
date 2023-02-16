package dungeonmania;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.assertEquals;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;

import static dungeonmania.TestUtils.getInventory;

public class consumeInvisiblePotionUnitTests {
    @Test
    @DisplayName("Test battles do not occur")
    public void testInvisibleBattle() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("testInvisibilityPotion",
                "c_battleTests_basicMercenaryMercenaryDies");
        DungeonResponse actualDungonRes = dmc.tick(Direction.DOWN);

        assertEquals(1, getInventory(actualDungonRes, "invisibility_potion").size());
        String potionId = getInventory(actualDungonRes, "invisibility_potion").get(0).getId();
        actualDungonRes = dmc.tick(potionId);
        assertEquals(0, actualDungonRes.getBattles().size());
    }

    @Test
    @DisplayName("Test potion effects wear off after duration is complete")
    public void testInvisibleDuration() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("testInvisibilityPotion",
                "c_battleTests_basicMercenaryMercenaryDies");
        DungeonResponse actualDungonRes = dmc.tick(Direction.DOWN);
        assertEquals(1, getInventory(actualDungonRes, "invisibility_potion").size());
        String potionId = getInventory(actualDungonRes, "invisibility_potion").get(0).getId();
        actualDungonRes = dmc.tick(potionId);
        assertEquals(0, actualDungonRes.getBattles().size());
        actualDungonRes = dmc.tick(Direction.DOWN);
        assertEquals(1, actualDungonRes.getBattles().size());
    }
}
