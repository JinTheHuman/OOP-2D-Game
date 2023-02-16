package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

public class interactTests {
    @Test
    public void testBribe() throws IllegalArgumentException,
    InvalidActionException {
        // test bribe of mercenary
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("merc_bribe",
        "c_movementTest_testMovementDown");
        EntityResponse initPlayer = getPlayer(initDungonRes).get();

        DungeonResponse res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(3, getInventory(res, "treasure").size());
        res = dmc.interact("Entity5");

        assertEquals(2, getInventory(res, "treasure").size());
    }

    @Test
    public void testBribeRadius() throws IllegalArgumentException,
    InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("merc_bribe_bigRadius",
        "c_bigRadiusBribe");
        EntityResponse initPlayer = getPlayer(initDungonRes).get();

        DungeonResponse res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(3, getInventory(res, "treasure").size());
        res = dmc.interact("Entity5");

        assertEquals(0, getInventory(res, "treasure").size());
    }

    @Test
    public void testBreakZombieSpawner() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("break_zombiespawner", "c_movementTest_testMovementDown");
        dmc.tick(Direction.DOWN);
        DungeonResponse res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "sword").size());
        res = dmc.interact("Entity3");
        assertEquals(0, countEntityOfType(res, "zombie_toast_spawner"));
    }

    @Test
    public void testEntityIdError() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("merc_bribe", "c_movementTest_testMovementDown");
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        DungeonResponse res = dmc.tick(Direction.RIGHT);
        assertEquals(3, getInventory(res, "treasure").size());
        assertThrows(IllegalArgumentException.class, () -> dmc.interact("Entity50"));
    }

    @Test
    public void testWrongBribeRadiusError() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("merc_bribe_wrongRadius", "c_movementTest_testMovementDown");
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);
        DungeonResponse res = dmc.tick(Direction.LEFT);
        assertEquals(3, getInventory(res, "treasure").size());
        assertThrows(IllegalArgumentException.class, () -> dmc.interact("Entity3"));
    }

    @Test
    public void testNotNextToSpawnerError() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("break_zombiespawner", "c_movementTest_testMovementDown");
        DungeonResponse res = dmc.tick(Direction.DOWN);
        assertEquals(1, getInventory(res, "sword").size());
        assertThrows(InvalidActionException.class, () -> dmc.interact("Entity3"));
    }

    @Test
    public void testNoGold() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("merc_bribe_noGold", "c_movementTest_testMovementDown");
        dmc.tick(Direction.RIGHT);
        DungeonResponse res = dmc.tick(Direction.RIGHT);
        assertEquals(0, getInventory(res, "treasure").size());
        assertThrows(InvalidActionException.class, () -> dmc.interact("Entity2"));
    }

    @Test
    public void testNoWeapon() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("break_zombiespawner", "c_movementTest_testMovementDown");
        DungeonResponse res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(0, getInventory(res, "sword").size());
        assertThrows(InvalidActionException.class, () -> dmc.interact("Entity3"));
    }

    @Test
    public void testSceptre() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("testSceptre", "sceptreConfig");
        DungeonResponse res = dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        assertDoesNotThrow(() -> dmc.build("sceptre"));
        assertDoesNotThrow(() -> dmc.interact("Entity5"));

        // test that the effects wear off and u can battle him
        dmc.tick(Direction.DOWN);

    }

    @Test
    public void testBribeAssassin() throws IllegalArgumentException,
    InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("assassin_bribe",
        "bribe_assassin");
        EntityResponse initPlayer = getPlayer(initDungonRes).get();

        DungeonResponse res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(3, getInventory(res, "treasure").size());
        res = dmc.interact("Entity5");

        assertEquals(2, getInventory(res, "treasure").size());
    }

    @Test
    public void testJin() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("jin",
        "bribe_assassin");

        DungeonResponse res = dmc.tick(Direction.RIGHT);
        assertDoesNotThrow(() -> dmc.interact("Entity2"));
    }

    @Test 
    public void testJames() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("james",
        "bribe_assassin");

        assertEquals(8, countEntityOfType(initDungonRes, "zombie_toast"));
        DungeonResponse res = dmc.interact("Entity2");
        assertEquals(0, countEntityOfType(res, "zombie_toast"));

    }
}
